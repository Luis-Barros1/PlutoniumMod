package com.plutonium.plutoniummod.worldgen;

import com.plutonium.plutoniummod.PlutoniumMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> LEAD_ORE_PLACED_KEY = registerKey("lead_ore_placed");
    public static final ResourceKey<PlacedFeature> PANDORITH_SPIRE_NBT_PLACED_KEY = registerKey("pandorith_spire_nbt_placed");
    public static final ResourceKey<PlacedFeature> CRATER_NBT_PLACED_KEY = registerKey("crater_nbt_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        System.out.println("[PlutoniumMod] Bootstrap chamado: ModPlacedFeatures");
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, LEAD_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_LEAD_ORE_KEY),
                ModOrePlacement.commonOrePlacement(20,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-30), VerticalAnchor.absolute(80))));



        List<PlacementModifier> modifiersSpire = List.of(
                RarityFilter.onAverageOnceEvery(80),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                BiomeFilter.biome()
        );

        List<PlacementModifier> modifiersCrater = List.of(
                RarityFilter.onAverageOnceEvery(20),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                BiomeFilter.biome()
        );

        context.register(CRATER_NBT_PLACED_KEY, new PlacedFeature(configuredFeatures.getOrThrow(ModConfiguredFeatures.CRATER_NBT_KEY), modifiersCrater));

        context.register(PANDORITH_SPIRE_NBT_PLACED_KEY, new PlacedFeature(configuredFeatures.getOrThrow(ModConfiguredFeatures.PANDORITH_SPIRE_NBT_KEY), modifiersSpire));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name){
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(PlutoniumMod.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers){
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
