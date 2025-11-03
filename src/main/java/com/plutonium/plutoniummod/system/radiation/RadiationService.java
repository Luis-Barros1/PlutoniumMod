package com.plutonium.plutoniummod.system.radiation;
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

    private static void tick(Player player) {
        RadiationData data = new RadiationData(player);
        data.getRadiationLevel();
    }
}
