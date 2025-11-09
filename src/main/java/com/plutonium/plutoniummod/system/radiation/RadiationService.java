package com.plutonium.plutoniummod.system.radiation;

import com.plutonium.plutoniummod.registry.ModEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RadiationService {
    private static final Logger LOGGER = LogManager.getLogger();

    private RadiationService() {
    }

    public static void disableRadiation(Player player) {
        RadiationData data = new RadiationData(player);
        data.setCanReceiveRadiation(false);
        data.setRadiationLevel(0.0);
        data.setRadiationResistance(1.0);

        boolean removedAny = player.removeAllEffects();
        if (!removedAny) {
            player.removeEffect(ModEffects.RADIATION_SICKNESS.get());
            player.removeEffect(MobEffects.DIG_SLOWDOWN);
            player.removeEffect(MobEffects.POISON);
            player.removeEffect(MobEffects.WITHER);
            player.removeEffect(MobEffects.GLOWING);
        }

        LOGGER.info("Radiation disabled for player {}", player.getScoreboardName());
    }

    public static void enableRadiation(Player player) {
        RadiationData data = new RadiationData(player);
        data.setCanReceiveRadiation(true);
        data.setRadiationResistance(0.0);

        LOGGER.info("Radiation enabled for player {}", player.getScoreboardName());
    }
}
