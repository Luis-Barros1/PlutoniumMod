package com.plutonium.plutoniummod.worldgen.features;

import com.mojang.serialization.Codec;
import com.plutonium.plutoniummod.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.List;

public class CraterFeature extends Feature<NoneFeatureConfiguration> {

    private static final int[] RADIUS_OPTIONS = new int[]{5, 10, 15};
    private static final double WALL_THICKNESS = 1.6D;
    private static final List<BlockState> WALL_PALETTE = List.of(
            Blocks.STONE.defaultBlockState(),
            Blocks.COBBLESTONE.defaultBlockState(),
            Blocks.BLACKSTONE.defaultBlockState(),
            Blocks.BASALT.defaultBlockState()
    );
    
    private static final List<BlockState> DAMAGE_PALETTE = List.of(
            Blocks.GRAVEL.defaultBlockState(),
            Blocks.COBBLESTONE.defaultBlockState(),
            Blocks.COARSE_DIRT.defaultBlockState()
    );

    public CraterFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        System.out.println("tentando construir cratera");
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        BlockPos surface = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin);
        int surfaceY = surface.getY();

        if (surfaceY <= level.getMinBuildHeight() + 10) {
            System.out.println("altura minima de construção");
            return false;
        }

        BlockPos groundBelow = surface.below();
        BlockState groundState = level.getBlockState(groundBelow);
        if (groundState.isAir() || groundState.getFluidState().is(FluidTags.WATER) || groundState.getFluidState().is(FluidTags.LAVA)) {
            System.out.println("terreno incompleto abaixo do local");
            return false;
        }

        // Determinar se esta cratera terá lava (1 em 30)
        boolean hasLava = random.nextInt(30) == 0;
        
        int radius = RADIUS_OPTIONS[random.nextInt(RADIUS_OPTIONS.length)];
        int depth = Mth.clamp((int) (radius * 0.6F) + random.nextInt(4) - 1, radius / 2, radius);
        int centerY = surfaceY - depth;

        if (centerY <= level.getMinBuildHeight() + 5) {
            System.out.println("Muito baixo");
            return false;
        }

        int damageRadius = (int) (radius * 1.5);
        BlockPos minPos = new BlockPos(surface.getX() - damageRadius, centerY - 2, surface.getZ() - damageRadius);
        BlockPos maxPos = new BlockPos(surface.getX() + damageRadius, surfaceY, surface.getZ() + damageRadius);
        if (!chunksLoaded(level, minPos, maxPos)) {
            System.out.println("Chunck não carregado");
            return false;
        }

        if (!hasSolidCap(level, surface, radius, depth)) {
            System.out.println("Não tem cap solido");
            return false;
        }

        clearAboveSurface(level, surface, radius, surfaceY);

        MutableBlockPos mutablePos = new MutableBlockPos();
        BlockState rimSoil = Blocks.COARSE_DIRT.defaultBlockState();
        BlockState rimAsh = Blocks.GRAVEL.defaultBlockState();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                double horizontalDist = Math.sqrt(dx * dx + dz * dz);
                if (horizontalDist > radius + 1) continue;

                // rim dressing (slight scorch on surface edge)
                if (horizontalDist >= radius - 1.0 && horizontalDist <= radius + 0.5) {
                    mutablePos.set(surface.getX() + dx, surfaceY - 1, surface.getZ() + dz);
                    if (!level.getBlockState(mutablePos).isAir()) {
                        BlockState rimState = random.nextFloat() < 0.45F ? rimAsh : rimSoil;
                        level.setBlock(mutablePos, rimState, Block.UPDATE_CLIENTS);
                    }
                }

                for (int dy = 0; dy <= depth; dy++) {
                    int worldY = surfaceY - dy;
                    if (worldY < level.getMinBuildHeight() + 1) break;

                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (dist > radius + 0.5) {
                        continue;
                    }

                    mutablePos.set(surface.getX() + dx, worldY, surface.getZ() + dz);

                    if (dist <= radius - WALL_THICKNESS) {
                        carveInterior(level, mutablePos, worldY, surfaceY, random, centerY, depth, hasLava);
                    } else if (dist <= radius) {
                        placeWallBlock(level, mutablePos, random, dy, depth, hasLava);
                    }
                }
            }
        }

        damageSurroundingArea(level, surface, radius, surfaceY, random);

        return true;
    }

    private static boolean isCraterBottom(int currentY, int centerY, int depth) {
        // Verificar se estamos nos últimos 5% da profundidade a partir do fundo
        int bottomThreshold = (int) (depth * 0.05F);
        int distanceFromBottom = currentY - centerY; // Distância do fundo (sempre >= 0)
        return distanceFromBottom >= 0 && distanceFromBottom <= bottomThreshold;
    }

    private static void carveInterior(WorldGenLevel level, MutableBlockPos pos, int currentY, int surfaceY, RandomSource random, int centerY, int depth, boolean hasLava) {
        if (currentY > surfaceY) {
            return;
        }
        BlockState state = level.getBlockState(pos);
        
        if (isCraterBottom(currentY, centerY, depth)) {
            // Preencher fundo com blocos da paleta
            int depthFromSurface = surfaceY - currentY;
            placeWallBlock(level, pos, random, depthFromSurface, depth, hasLava);
        } else {
            // Remover blocos do resto do interior
            if (!state.isAir()) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            }
        }
    }

    private static void placeWallBlock(WorldGenLevel level, MutableBlockPos pos, RandomSource random, int depthFromSurface, int craterDepth, boolean hasLava) {
        BlockState existing = level.getBlockState(pos);
        if (existing.isAir() || existing.is(Blocks.WATER)) {
                return;
        }
        BlockState chosen = pickWallBlock(random, depthFromSurface, craterDepth, hasLava);
        level.setBlock(pos, chosen, Block.UPDATE_CLIENTS);
    }

    private static BlockState pickWallBlock(RandomSource random, int depthFromSurface, int craterDepth, boolean hasLava) {
        float depthRatio = (float) depthFromSurface / craterDepth;

        // Só gerar lava se esta cratera foi selecionada para ter lava (1 em 30)
        if (hasLava && depthRatio > 0.7F && random.nextFloat() < 0.2F) {
            return Blocks.LAVA.defaultBlockState();
        }
        if (depthRatio > 0.5F && random.nextFloat() < 0.35F) {
            return Blocks.BLACKSTONE.defaultBlockState();
        }
        if (random.nextFloat() < 0.15F) {
            return ModBlocks.PANDORITH_ORE.get().defaultBlockState();
        }
        return WALL_PALETTE.get(random.nextInt(WALL_PALETTE.size()));
    }

    private static boolean hasSolidCap(WorldGenLevel level, BlockPos surface, int radius, int depth) {
        MutableBlockPos mutablePos = new MutableBlockPos();
        int surfaceY = surface.getY();
        int capDepth = Math.min(depth, Math.max(4, depth / 3));
        int allowedGaps = 9999999;
        //int allowedGaps = Math.max(1, radius / 3);
        int gapsFound = 0;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = 0; dy <= capDepth; dy++) {
                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (dist > radius + 0.5) {
                        System.out.println("Dist > raio");
                        continue;
                    }

                    int worldY = surfaceY - dy;
                    if (worldY <= level.getMinBuildHeight() + 1) {
                        System.out.println("world y <= nivel minimo de construção do mundo");
                        return false;
                    }

                    mutablePos.set(surface.getX() + dx, worldY, surface.getZ() + dz);
                    BlockState state = level.getBlockState(mutablePos);
                    if (state.isAir() || state.getFluidState().is(FluidTags.WATER) || state.getFluidState().is(FluidTags.LAVA)) {
                        gapsFound++;
                        System.out.println("Break");
                        break;
                    }
                }
                if (gapsFound > allowedGaps) {
                    System.out.println("Gaps encontrados maior que os gaps permitidos");
                    return false;
                }
            }
        }

        return true;
    }

    private static void clearAboveSurface(WorldGenLevel level, BlockPos surface, int radius, int surfaceY) {
        System.out.println("Tentando limpar superfície acima");
        MutableBlockPos mutablePos = new MutableBlockPos();
        int maxReach = 50; // Altura máxima a limpar acima da superfície
        int reach = Math.min(level.getMaxBuildHeight() - 1, surfaceY + maxReach);
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                double horizontalDist = Math.sqrt(dx * dx + dz * dz);
                if (horizontalDist > radius + 0.5) continue;

                for (int y = surfaceY + 1; y <= reach; y++) {
                    mutablePos.set(surface.getX() + dx, y, surface.getZ() + dz);
                    BlockState state = level.getBlockState(mutablePos);
                    if (state.isAir()) {
                        break; // Parar quando encontrar ar (já está limpo)
                    }
                    // Remover blocos sólidos e fluidos
                    if (state.getFluidState().isEmpty()) {
                        level.setBlock(mutablePos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                    } else {
                        // Se for fluido, também remover mas continuar subindo
                        level.setBlock(mutablePos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
    }

    private static void damageSurroundingArea(WorldGenLevel level, BlockPos surface, int radius, int surfaceY, RandomSource random) {
        MutableBlockPos mutablePos = new MutableBlockPos();
        int damageRadius = (int) (radius * 1.5);
        
        for (int dx = -damageRadius; dx <= damageRadius; dx++) {
            for (int dz = -damageRadius; dz <= damageRadius; dz++) {
                double horizontalDist = Math.sqrt(dx * dx + dz * dz);
                
                // Apenas processar área entre radius e damageRadius
                if (horizontalDist <= radius || horizontalDist > damageRadius) {
                    continue;
                }
                
                // Calcular fator de destruição (1.0 próximo da cratera, 0.0 longe)
                double damageFactor = 1.0 - (horizontalDist - radius) / (radius * 0.5);
                damageFactor = Mth.clamp(damageFactor, 0.0, 1.0);
                
                // Aplicar destruição apenas na superfície e algumas camadas abaixo
                for (int dy = 0; dy <= 3; dy++) {
                    int worldY = surfaceY - dy;
                    if (worldY < level.getMinBuildHeight() + 1) break;
                    
                    mutablePos.set(surface.getX() + dx, worldY, surface.getZ() + dz);
                    BlockState state = level.getBlockState(mutablePos);
                    
                    // Não destruir ar, água ou lava
                    if (state.isAir() || state.getFluidState().is(FluidTags.WATER) || state.getFluidState().is(FluidTags.LAVA)) {
                        continue;
                    }
                    
                    // Probabilidade de destruição baseada no fator de dano
                    if (random.nextFloat() < damageFactor) {
                        BlockState damageBlock = DAMAGE_PALETTE.get(random.nextInt(DAMAGE_PALETTE.size()));
                        level.setBlock(mutablePos, damageBlock, Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
    }

    private static boolean chunksLoaded(WorldGenLevel level, BlockPos min, BlockPos max) {
        int minChunkX = min.getX() >> 4;
        int minChunkZ = min.getZ() >> 4;
        int maxChunkX = max.getX() >> 4;
        int maxChunkZ = max.getZ() >> 4;

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                if (!level.hasChunk(cx, cz)) {
                    return false;
                }
            }
        }
        return true;
    }
}

