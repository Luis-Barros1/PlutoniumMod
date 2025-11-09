package com.plutonium.plutoniummod.datagen;

import com.plutonium.plutoniummod.PlutoniumMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = PlutoniumMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        System.out.println("[PlutoniumMod] ==========================================");
        System.out.println("[PlutoniumMod] GatherDataEvent disparado!");
        System.out.println("[PlutoniumMod] Include Server: " + event.includeServer());
        System.out.println("[PlutoniumMod] Include Client: " + event.includeClient());
        System.out.println("[PlutoniumMod] Include Dev: " + event.includeDev());
        System.out.println("[PlutoniumMod] Include Reports: " + event.includeReports());
        System.out.println("[PlutoniumMod] ==========================================");

        // IMPORTANTE: Sempre adicionar com true, não com event.includeServer()
        // porque o provider precisa rodar para gerar os arquivos
        generator.addProvider(true, new ModWorldGenProvider(packOutput, lookupProvider));

        System.out.println("[PlutoniumMod] ModWorldGenProvider adicionado!");
    }
}