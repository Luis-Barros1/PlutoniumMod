package com.plutonium.plutoniummod.system.radiation;

import net.minecraft.world.entity.player.Player;

public class RadiationDecay {

    private static final double MAX_RADIATION = 100.0;

    public static void updateRadiationLevel (Player player, RadiationData data, int exposureLevel) {
        double currentLevel = data.getRadiationLevel();
        double currentResistance = data.getRadiationResistance();

        // Clamp de resistência para evitar valores fora de [0,1]
        if (currentResistance < 0.0) currentResistance = 0.0;
        if (currentResistance > 1.0) currentResistance = 1.0;

        // Cap máximo determinado pelo nível de exposição (tier)
        double exposureCap = exposureLevel > 0 ? Math.min(MAX_RADIATION, exposureLevel * 20.0) : 0.0;

        if (exposureLevel > 0 && data.canReceiveRadiation()) {
            if (currentLevel < exposureCap) {
                // Ganho proporcional ao nível de exposição, mitigado por resistência
                double gain = ((double) exposureLevel /5) * (1.0 - currentResistance);
                if (gain < 0.0) gain = 0.0;
                currentLevel += gain;
                if (currentLevel > exposureCap) currentLevel = exposureCap; // sobe até o cap, no máximo
            } else {
                // Se já está acima do cap da exposição atual (vinda de uma exposição anterior), decai suavemente até o cap
                double decayRateWhileExposed = 0.8; // mesmo decaimento "normal" quando pode receber radiação
                currentLevel -= decayRateWhileExposed;
                if (currentLevel < exposureCap) currentLevel = exposureCap; // não passa para baixo do cap enquanto ainda há exposição
            }
        } else {
            // Decaimento natural (mais rápido quando não pode receber radiação)
            double decayRate = data.canReceiveRadiation() ? 0.8 : 1;
            currentLevel -= decayRate;
            if (currentLevel < 0.0) currentLevel = 0.0;
        }

        data.setRadiationLevel(currentLevel);
    }
}
