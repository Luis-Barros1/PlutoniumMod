package com.plutonium.plutoniummod.system;
import com.plutonium.plutoniummod.entity.Radiacao;
import com.plutonium.plutoniummod.registry.ModEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RadiationService {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void calculaRadiacaoAltura(Player player) {
        double y = player.blockPosition().getY();

        if (y >= 200) incrementaRadiacao(player, new Radiacao(10, 3));
        if (y >= 100 && y < 200) incrementaRadiacao(player, new Radiacao(2, 2));
        if (y > 40 && y < 100) incrementaRadiacao(player, new Radiacao());
        if (y <= 40) reduzRadiacao(player, new Radiacao());
        if (y < 20) reduzRadiacao(player, new Radiacao(2.0));
    }

    public static void reduzRadiacao(Player player, Radiacao rad) {
        double radiacao = calculaRadiacao(player.getPersistentData().getDouble("Radiation_Level"), rad.getVelocidade() * (-1));
        if (radiacao <= 0) return;
        player.getPersistentData().putDouble("Radiation_Level", radiacao);
    }

    public static void incrementaRadiacao(Player player, Radiacao rad) {
        double radiacao = calculaRadiacao(player.getPersistentData().getDouble("Radiation_Level"), rad.getVelocidade());
        int intensidade = rad.getIntensidade();

        alteraLimiteRadiacao(player, intensidade);
        double limiteRadiacao = player.getPersistentData().getDouble("Radiation_Cap");

        if (canReceiveRadiation(player)) return;
        if (radiacao >= limiteRadiacao) return;
        player.getPersistentData().putDouble("Radiation_Level", radiacao);
        LOGGER.info("Player pode receber: {}", player.getPersistentData().getBoolean("CanReceiveRadiation"));
    }

    private static double calculaRadiacao(Double radiacaoAtual, double velocidade) {
        double config = 60.0;
        return (radiacaoAtual + (velocidade*config)/100);
    }

    private static void alteraLimiteRadiacao(Player player, int Intensidade) {
        switch (Intensidade) {
            case 1:
                player.getPersistentData().putDouble(("Radiation_Cap"), 50);
                break;
            case 2:
                player.getPersistentData().putDouble(("Radiation_Cap"), 100);
                break;
            case 3:
                player.getPersistentData().putDouble(("Radiation_Cap"), 150);
        }
    }

    public static void aplicaEfeitosRadiacao(Player player) {
        double radiacaoAtualPlayer = player.getPersistentData().getDouble("Radiation_Level");

        if (radiacaoAtualPlayer < 50) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 0 , false, false ));
            player.addEffect(new MobEffectInstance(ModEffects.RADIATION_SICKNESS.get(), 40, 1 , false, false ));
        }
        if (radiacaoAtualPlayer >= 50) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 0 , false, false ));
        }
        if (radiacaoAtualPlayer >= 100) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 1 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 50, 2 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 50, 2 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 50, 2 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 50, 2 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, 2 , false, false ));
        }

        if (radiacaoAtualPlayer >= 150) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 2 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 50, 2 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 50, 2 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 50, 2 , false, false ));
        }
    }

    public static boolean canReceiveRadiation(Player player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains("CanReceiveRadiation")) return false; {
            data.putBoolean("CanReceiveRadiation", true);
        }
        return data.getBoolean("CanReceiveRadiation");
    }
}
