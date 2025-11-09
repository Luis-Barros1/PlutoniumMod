package com.plutonium.plutoniummod.registry;

import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.worldgen.features.PandorithSpireNBTFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, PlutoniumMod.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PANDORITH_SPIRE =
            FEATURES.register("pandorith_spire", () -> new PandorithSpireNBTFeature(NoneFeatureConfiguration.CODEC));
}
