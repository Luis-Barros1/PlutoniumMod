package com.plutonium.plutoniummod.events;

import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.system.RadiationService;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = PlutoniumMod.MOD_ID)
public class TickEventHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.level().isClientSide) return;
        if (event.phase == TickEvent.Phase.END) return;

        tickCounter++;

        //Executa a cada 1 segundo
        if (tickCounter % 20 == 0) {
            RadiationService.calculaRadiacaoAltura(player);
            RadiationService.aplicaEfeitosRadiacao(player);
            LOGGER.info("Radiação absorvida: {}", player.getPersistentData().getDouble("Radiation_Level"));
        }

        //Executa a cada 2 segundos
        if (tickCounter % 40 == 0) {

        }

        //Executa a cada 5 segundos
        if (tickCounter % 100 == 0) {

        }
    }
}
