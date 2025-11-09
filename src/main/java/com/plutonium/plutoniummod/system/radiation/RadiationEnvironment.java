package com.plutonium.plutoniummod.system.radiation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class RadiationEnvironment {
    public static int getExposureLevel(Player player) {
        RadiationData data = new RadiationData(player);
        int level = 0;

        if (player.isCreative() || player.isSpectator()) {
            data.setCanReceiveRadiation(false);
            return 0;
        }

        var biomeHolder = player.level().getBiome(player.blockPosition());
        var optKey = biomeHolder.unwrapKey();
        if (optKey.isPresent()) {
            ResourceLocation id = optKey.get().location();
            String path = id.getPath();
            if (path.contains("desert")) level = Math.max(level, 3);
            if (path.contains("plains")) level = Math.max(level, 2);
        }

        if (player.blockPosition().getY() < 30){
            level = Math.max(level, 1);
            data.setCanReceiveRadiation(false);
        }
        if (player.blockPosition().getY() >= 30){
            level = Math.max(level, 1);
            data.setCanReceiveRadiation(true);
        }

        if (player.isInWaterRainOrBubble()) level = Math.max(level, 1);

        return level;
    }
}
