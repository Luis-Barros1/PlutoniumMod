package com.plutonium.plutoniummod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.plutonium.plutoniummod.blocks.ModBlocks;
import com.plutonium.plutoniummod.itens.ModItens;


@Mod(PlutoniumMod.MOD_ID)
public class PlutoniumMod {
    public static final String MOD_ID = "plutoniummod";
    public static final Logger LOGGER = LogManager.getLogger();

    public PlutoniumMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Setup - PlutoniumMod concluido com sucesso!");
    }
}
