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

import net.minecraft.world.level.block.state.BlockState;
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

        // Configurações de rotação (precisamos do tamanho antes de validar)
        Rotation rotation = Rotation.getRandom(random);
        Mirror mirror = Mirror.NONE;

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(rotation)
                .setMirror(mirror)
                .setIgnoreEntities(false)
                .setKeepLiquids(false)  // Evita problemas com água/lava
                .setRandom(random);

        // Obter tamanho da estrutura ANTES de validar
        Vec3i size = template.getSize(rotation);
        BlockPos adjustedPos = surface.offset(-size.getX() / 2, 0, -size.getZ() / 2);

        // ✅ VALIDAÇÃO: Verificar toda a área da base da estrutura
        int totalBlocks = size.getX() * size.getZ();
        int solidBlocks = 0;
        int minRequired = (int) (totalBlocks * 0.7); // Exigir pelo menos 70% dos blocos sólidos

        for (int x = 0; x < size.getX(); x++) {
            for (int z = 0; z < size.getZ(); z++) {
                BlockPos checkPos = adjustedPos.offset(x, -1, z);
                
                // Verificar se a posição está dentro dos limites do mundo
                if (checkPos.getY() < level.getMinBuildHeight() || checkPos.getY() >= level.getMaxBuildHeight()) {
                    System.out.println("[PlutoniumMod] ❌ Posição fora dos limites do mundo: " + checkPos);
                    return false;
                }
                
                BlockState checkState = level.getBlockState(checkPos);

                if (!checkState.isAir() &&
                        checkState.isSolidRender(level, checkPos) &&
                        !checkState.getFluidState().is(FluidTags.WATER) &&
                        !checkState.getFluidState().is(FluidTags.LAVA)) {
                    solidBlocks++;
                }
            }
        }

        // Validar se há terreno sólido suficiente
        if (solidBlocks < minRequired) {
            System.out.println("[PlutoniumMod] ❌ Área instável em " + adjustedPos + 
                    " - apenas " + solidBlocks + "/" + totalBlocks + " blocos sólidos (mínimo: " + minRequired + ")");
            return false;
        }

        System.out.println("[PlutoniumMod] ✅ Terreno validado: " + solidBlocks + "/" + totalBlocks + " blocos sólidos");

        System.out.println("[PlutoniumMod] Posição ajustada: " + adjustedPos + " | Tamanho: " + size);

        // ✅ Verificação robusta de chunks: incluir área da estrutura + chunks adjacentes
        int chunkX = adjustedPos.getX() >> 4;
        int chunkZ = adjustedPos.getZ() >> 4;
        int endChunkX = (adjustedPos.getX() + size.getX() - 1) >> 4;
        int endChunkZ = (adjustedPos.getZ() + size.getZ() - 1) >> 4;
        
        // Adicionar margem de 1 chunk em todas as direções para garantir chunks adjacentes
        int margin = 1;
        int minChunkX = chunkX - margin;
        int maxChunkX = endChunkX + margin;
        int minChunkZ = chunkZ - margin;
        int maxChunkZ = endChunkZ + margin;

        System.out.println("[PlutoniumMod] Verificando chunks: [" + minChunkX + "," + minChunkZ + "] até [" + maxChunkX + "," + maxChunkZ + "]");

        // Verificar todos os chunks necessários (área + adjacentes)
        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                // Verificar se o chunk existe
                if (!level.hasChunk(cx, cz)) {
                    System.err.println("[PlutoniumMod] ❌ Chunk não existe: [" + cx + ", " + cz + "]");
                    return false;
                }
                
                // Tentar acessar o chunk para garantir que está realmente carregado e acessível
                try {
                    var chunk = level.getChunk(cx, cz);
                    if (chunk == null) {
                        System.err.println("[PlutoniumMod] ❌ Chunk é null: [" + cx + ", " + cz + "]");
                        return false;
                    }
                    // Se chegou aqui, o chunk está carregado e acessível
                } catch (Exception e) {
                    System.err.println("[PlutoniumMod] ❌ Erro ao acessar chunk [" + cx + ", " + cz + "]: " + e.getMessage());
                    return false;
                }
            }
        }

        System.out.println("[PlutoniumMod] ✅ Todos os chunks estão disponíveis e carregados, colocando estrutura...");

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