package com.plutonium.plutoniummod.blocks;

import com.plutonium.plutoniummod.system.radiation.RadiationData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class DecontaminationBlock extends Block {

    public static final double DECONTAMINATION_RATE = 10.0; // Taxa de descontaminação muito mais rápida
    public static final int AREA_SIZE = 1; // 1 bloco para cada lado = área 3x3x3

    public DecontaminationBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(3.5f, 3.5f)
                .requiresCorrectToolForDrops());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            // Verifica se o bloco está recebendo sinal de redstone
            boolean hasRedstoneSignal = level.hasNeighborSignal(pos);
            
            if (hasRedstoneSignal) {
                // Processa descontaminação quando ativado
                processDecontamination(level, pos);
            }
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    public static void processDecontamination(Level level, BlockPos pos) {
        // Define a área de 3x3x3 ao redor do bloco
        BlockPos minPos = pos.offset(-AREA_SIZE, -AREA_SIZE, -AREA_SIZE);
        BlockPos maxPos = pos.offset(AREA_SIZE, AREA_SIZE, AREA_SIZE);
        AABB area = new AABB(minPos, maxPos.offset(1, 1, 1));

        // Busca todos os players na área
        List<Player> players = level.getEntitiesOfClass(Player.class, area);

        // Processa descontaminação para cada player encontrado
        for (Player player : players) {
            RadiationData data = new RadiationData(player);
            double currentLevel = data.getRadiationLevel();

            if (currentLevel > 0) {
                // Aplica descontaminação acelerada
                double newLevel = Math.max(0.0, currentLevel - DECONTAMINATION_RATE);
                data.setRadiationLevel(newLevel);

                // Se chegou a zero, garante que não há mais radiação
                if (newLevel <= 0.0) {
                    data.setRadiationLevel(0.0);
                }
            }
        }
    }
}

