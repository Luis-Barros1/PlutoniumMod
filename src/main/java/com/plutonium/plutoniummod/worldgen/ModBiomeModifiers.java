package com.plutonium.plutoniummod.worldgen;

import com.plutonium.plutoniummod.PlutoniumMod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_LEAD_ORE = registerkey("add_lead_ore");
    public static final ResourceKey<BiomeModifier> ADD_PANDORITH_SPIRE_NBT = registerkey("add_pandorith_spire_nbt");
    public static final ResourceKey<BiomeModifier> ADD_CRATER_NBT = registerkey("add_crater_nbt");

    public static void bootstrap(BootstapContext<BiomeModifier> context){
        System.out.println("[PlutoniumMod] Bootstrap chamado: ModBiomeModifiers");
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_LEAD_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.LEAD_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));


        context.register(ADD_PANDORITH_SPIRE_NBT, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PANDORITH_SPIRE_NBT_PLACED_KEY)),
                GenerationStep.Decoration.SURFACE_STRUCTURES
        ));

        context.register(ADD_CRATER_NBT, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.CRATER_NBT_PLACED_KEY)),
                GenerationStep.Decoration.SURFACE_STRUCTURES
        ));
    }

    private static ResourceKey<BiomeModifier> registerkey(String name){
            return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(PlutoniumMod.MOD_ID, name));
    }
}
