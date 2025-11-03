package com.plutonium.plutoniummod.events;

import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.system.radiation.RadiationData;
import com.plutonium.plutoniummod.system.radiation.RadiationDecay;
import com.plutonium.plutoniummod.system.radiation.RadiationEffects;
import com.plutonium.plutoniummod.system.radiation.RadiationEnvironment;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = PlutoniumMod.MOD_ID)
public class TickEventHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.level().isClientSide) return;
        if (event.phase == TickEvent.Phase.END) return;

        //Executa a cada 1 segundo por jogador
        if (player.tickCount % 20 == 0) {
            RadiationData data = new RadiationData(player);
            int exposureLevel = RadiationEnvironment.getExposureLevel(player);
            RadiationDecay.updateRadiationLevel(player, data, exposureLevel);
            RadiationEffects.applyEffects(player, data);
            LOGGER.debug("Radiação ({}): {}", player.getScoreboardName(), data.getRadiationLevel());
        }

        //Executa a cada 2 segundos
        if (player.tickCount % 40 == 0) {

        }

        //Executa a cada 5 segundos
        if (player.tickCount % 100 == 0) {

        }
    }
}
