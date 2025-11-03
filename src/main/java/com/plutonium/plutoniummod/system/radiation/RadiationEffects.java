package com.plutonium.plutoniummod.system.radiation;

import com.plutonium.plutoniummod.registry.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class RadiationEffects {

    public static void applyEffects(Player player, RadiationData data) {
        double level = data.getRadiationLevel();

        if (level >= 10.0 && level < 20.0) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0 , false, false ));
            player.addEffect(new MobEffectInstance(ModEffects.RADIATION_SICKNESS.get(), 200, 0 , false, false ));
        } else if (level >= 20.0 && level < 40.0) {
            //player.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 0 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0 , false, false ));
            player.addEffect(new MobEffectInstance(ModEffects.RADIATION_SICKNESS.get(), 200, 1 , false, false ));
        } else if (level >= 40.0 && level < 60.0) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 2 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 1 , false, false ));
            player.addEffect(new MobEffectInstance(ModEffects.RADIATION_SICKNESS.get(), 200, 2 , false, false ));
        } else if (level >= 60.0) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 2 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 0 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 3 , false, false ));
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 2 , false, false ));
            player.addEffect(new MobEffectInstance(ModEffects.RADIATION_SICKNESS.get(), 200, 2 , false, false ));
        }
    }
}
