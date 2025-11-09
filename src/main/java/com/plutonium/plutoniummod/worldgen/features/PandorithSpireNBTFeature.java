package com.plutonium.plutoniummod.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.core.Vec3i;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;

import java.util.List;
import java.util.Optional;

public class PandorithSpireNBTFeature extends Feature<NoneFeatureConfiguration> {

    // ✅ CORREÇÃO: Remover "structures/" - o Minecraft adiciona automaticamente
    private static final List<ResourceLocation> STRUCTURES = List.of(
            new ResourceLocation("plutoniummod", "pandorith_spike_1"),
            new ResourceLocation("plutoniummod", "pandorith_spike_2"),
            new ResourceLocation("plutoniummod", "pandorith_spike_3")
    );

    public PandorithSpireNBTFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        System.out.println("[PlutoniumMod] Tentando gerar espinho em " + origin);

        // ✅ SOLUÇÃO: Obter ServerLevel através do getLevel()
        ServerLevel serverLevel = level.getLevel();

        if (serverLevel == null) {
            System.err.println("[PlutoniumMod] ServerLevel é null");
            return false;
        }

        StructureTemplateManager manager = serverLevel.getStructureManager();
        ResourceLocation structureRL = STRUCTURES.get(random.nextInt(STRUCTURES.size()));

        // Carregar template
        Optional<StructureTemplate> templateOpt = manager.get(structureRL);

        if (templateOpt.isEmpty()) {
            System.err.println("[PlutoniumMod] ❌ Estrutura não encontrada: " + structureRL);
            System.err.println("[PlutoniumMod] Verifique se o arquivo existe em: data/plutoniummod/structures/pandorith_spike_1.nbt");
            return false;
        }

        StructureTemplate template = templateOpt.get();
        System.out.println("[PlutoniumMod] ✅ Template carregado: " + structureRL);

        // Usar WORLD_SURFACE_WG durante worldgen
        BlockPos surface = level.getHeightmapPos(
                net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG,
                origin
        );

        // ✅ VALIDAÇÃO 1: Verificar se tem chão sólido abaixo
        BlockPos groundCheck = surface.below();
        BlockState groundState = level.getBlockState(groundCheck);

        // Rejeitar se for água, lava, ar ou blocos não sólidos
        if (groundState.isAir() ||
                groundState.getFluidState().is(FluidTags.WATER) ||
                groundState.getFluidState().is(FluidTags.LAVA) ||
                !groundState.isSolidRender(level, groundCheck)) {
            System.out.println("[PlutoniumMod] ❌ Terreno inválido em " + surface + " - bloco: " + groundState.getBlock());
            return false;
        }

        // ✅ VALIDAÇÃO 2: Verificar área 3x3 ao redor (mais rigoroso)
        int solidBlocks = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos checkPos = surface.offset(x, -1, z);
                BlockState checkState = level.getBlockState(checkPos);

                if (!checkState.isAir() &&
                        checkState.isSolidRender(level, checkPos) &&
                        !checkState.getFluidState().is(FluidTags.WATER) &&
                        !checkState.getFluidState().is(FluidTags.LAVA)) {
                    solidBlocks++;
                }
            }
        }

        // Exigir pelo menos 7 dos 9 blocos sólidos (evita bordas de ilhas/lagos)
        if (solidBlocks < 7) {
            System.out.println("[PlutoniumMod] ❌ Área instável em " + surface + " - apenas " + solidBlocks + "/9 blocos sólidos");
            return false;
        }

        System.out.println("[PlutoniumMod] ✅ Terreno validado: " + solidBlocks + "/9 blocos sólidos");

        // Configurações de rotação
        Rotation rotation = Rotation.getRandom(random);
        Mirror mirror = Mirror.NONE;

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(rotation)
                .setMirror(mirror)
                .setIgnoreEntities(false)
                .setKeepLiquids(false)  // Evita problemas com água/lava
                .setRandom(random);

        // Centralizar estrutura
        Vec3i size = template.getSize(rotation);
        BlockPos adjustedPos = surface.offset(-size.getX() / 2, 0, -size.getZ() / 2);

        System.out.println("[PlutoniumMod] Posição ajustada: " + adjustedPos + " | Tamanho: " + size);

        // Verificar chunks disponíveis
        int chunkX = adjustedPos.getX() >> 4;
        int chunkZ = adjustedPos.getZ() >> 4;
        int endChunkX = (adjustedPos.getX() + size.getX()) >> 4;
        int endChunkZ = (adjustedPos.getZ() + size.getZ()) >> 4;

        for (int cx = chunkX; cx <= endChunkX; cx++) {
            for (int cz = chunkZ; cz <= endChunkZ; cz++) {
                if (!level.hasChunk(cx, cz)) {
                    System.err.println("[PlutoniumMod] ⚠️ Chunk não disponível: " + cx + ", " + cz);
                    return false;
                }
            }
        }

        System.out.println("[PlutoniumMod] Todos os chunks estão disponíveis, colocando estrutura...");

        try {
            // ✅ Usar WorldGenLevel e flag 3 (mais seguro)
            boolean success = template.placeInWorld(
                    level,              // WorldGenLevel
                    adjustedPos,
                    BlockPos.ZERO,
                    settings,
                    random,
                    3                   // Flag 3 = UPDATE_NEIGHBORS | UPDATE_CLIENTS
            );

            if (success) {
                System.out.println("[PlutoniumMod] ✅ Estrutura gerada com SUCESSO em " + adjustedPos);
            } else {
                System.err.println("[PlutoniumMod] ❌ FALHA ao gerar estrutura em " + adjustedPos);
            }

            return success;
        } catch (Exception e) {
            System.err.println("[PlutoniumMod] ❌ EXCEÇÃO ao gerar estrutura: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}