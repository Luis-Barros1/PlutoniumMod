package com.plutonium.plutoniummod.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RadiationSicknessEffect extends MobEffect {
    public RadiationSicknessEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public RadiationSicknessEffect() {
        super(MobEffectCategory.HARMFUL, 0x88FF00);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return;
        entity.hurt(entity.damageSources().magic(), 1.0F + amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 0; // aplica a cada 2s
    }
}
