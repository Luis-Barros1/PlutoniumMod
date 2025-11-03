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
        // Verde "radioativo" mais intenso para o ícone/partículas
        super(MobEffectCategory.HARMFUL, 0x5CFF3B);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return;
        // Intervalo baseado no tempo da entidade para não depender da duração do efeito
        int interval = amplifier <= 0 ? 80 : (amplifier == 1 ? 60 : 40);
        if ((entity.tickCount % interval) != 0) return;

        // Dano escala por nível; níveis baixos não devem matar rapidamente
        float baseDamage = 0.5F + (amplifier * 0.5F); // 0:0.5, 1:1.0, 2:1.5, 3:2.0

        // Evita matar direto em níveis baixos: preserva pelo menos 1-2 de vida
        float minHealthToDamage = amplifier >= 2 ? 2.0F : 1.0F;
        if (entity.getHealth() > minHealthToDamage) {
            entity.hurt(entity.damageSources().magic(), baseDamage);
        }

        // Em nível 3 (amplifier>=2 considerando tiers 0,1,2), aplica fogo curto
        if (amplifier >= 2) {
            entity.setSecondsOnFire(2 + amplifier); // 2-4s de fogo
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // Chamado todo tick; o throttling é feito em applyEffectTick via tickCount
        return true;
    }
}
