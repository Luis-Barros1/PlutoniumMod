package com.plutonium.plutoniummod.worldgen;

import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.blocks.ModBlocks;
import com.plutonium.plutoniummod.registry.ModFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_LEAD_ORE_KEY = registerKey("lead_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PANDORITH_SPIRE_NBT_KEY = registerKey("pandorith_spire_nbt");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CRATER_NBT_KEY = registerKey("crater_nbt");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        System.out.println("[PlutoniumMod] Bootstrap chamado: ModConfiguredFeatures");
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldLeadOres = List.of(OreConfiguration.target(stoneReplaceable, ModBlocks.LEAD_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, ModBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState()));

        register(context, OVERWORLD_LEAD_ORE_KEY, Feature.ORE, new OreConfiguration(overworldLeadOres, 9));

        //Espinhos de Pandorith
        context.register(PANDORITH_SPIRE_NBT_KEY, new ConfiguredFeature<>(ModFeatures.PANDORITH_SPIRE.get(), NoneFeatureConfiguration.INSTANCE));

        //Cratera
        context.register(CRATER_NBT_KEY, new ConfiguredFeature<>(ModFeatures.CRATER_NBT.get(), NoneFeatureConfiguration.INSTANCE));
    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(PlutoniumMod.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC featureConfig) {
        context.register(key, new ConfiguredFeature<>(feature, featureConfig));
    }
}
