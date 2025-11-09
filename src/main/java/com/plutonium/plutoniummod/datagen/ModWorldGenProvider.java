package com.plutonium.plutoniummod.datagen;

import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.worldgen.ModBiomeModifiers;
import com.plutonium.plutoniummod.worldgen.ModConfiguredFeatures;
import com.plutonium.plutoniummod.worldgen.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(PlutoniumMod.MOD_ID));
        System.out.println("[PlutoniumMod] ==========================================");
        System.out.println("[PlutoniumMod] ModWorldGenProvider CONSTRUTOR chamado!");
        System.out.println("[PlutoniumMod] BUILDER criado com " + BUILDER + " registries");
        System.out.println("[PlutoniumMod] ==========================================");
    }

    @Override
    public String getName() {
        return "World Gen: " + PlutoniumMod.MOD_ID;
    }
}