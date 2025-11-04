package com.plutonium.plutoniummod.events;

import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.blocks.DecontaminationBlock;
import com.plutonium.plutoniummod.blocks.ModBlocks;
import com.plutonium.plutoniummod.system.radiation.RadiationData;
import com.plutonium.plutoniummod.system.radiation.RadiationDecay;
import com.plutonium.plutoniummod.system.radiation.RadiationEffects;
import com.plutonium.plutoniummod.system.radiation.RadiationEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = PlutoniumMod.MOD_ID)
public class TickEventHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.level().isClientSide) return;
        if (event.phase == TickEvent.Phase.END) return;

        //Executa a cada 1 segundo por jogador
        if (player.tickCount % 20 == 0) {
            RadiationData data = new RadiationData(player);
            int exposureLevel = RadiationEnvironment.getExposureLevel(player);
            RadiationDecay.updateRadiationLevel(player, data, exposureLevel);
            RadiationEffects.applyEffects(player, data);
            LOGGER.debug("Radiação ({}): {}", player.getScoreboardName(), data.getRadiationLevel());
        }

        //Executa a cada 2 segundos
        if (player.tickCount % 40 == 0) {

        }

        //Executa a cada 5 segundos
        if (player.tickCount % 100 == 0) {

        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;

        // Processa blocos de descontaminação a cada segundo (20 ticks)
        if (event.getServer().getTickCount() % 20 == 0) {
            for (Level level : event.getServer().getAllLevels()) {
                if (level.isClientSide) continue;

                // Busca todos os blocos de descontaminação no nível
                findAndProcessDecontaminationBlocks(level);
            }
        }
    }

    private static void findAndProcessDecontaminationBlocks(Level level) {
        // Busca blocos de descontaminação em uma área ao redor dos players
        // Verifica área de 5x5x5 ao redor do player para encontrar blocos de descontaminação
        // Isso cobre a área de efeito de 3x3x3 do bloco mais uma margem de segurança
        for (Player player : level.players()) {
            BlockPos playerPos = player.blockPosition();
            
            // Verifica área expandida ao redor do player para encontrar blocos de descontaminação
            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -2; z <= 2; z++) {
                        BlockPos checkPos = playerPos.offset(x, y, z);
                        Block block = level.getBlockState(checkPos).getBlock();
                        
                        if (block == ModBlocks.DECONTAMINATION_BLOCK.get()) {
                            // Verifica se o bloco está recebendo sinal de redstone
                            if (level.hasNeighborSignal(checkPos)) {
                                DecontaminationBlock.processDecontamination(level, checkPos);
                            }
                        }
                    }
                }
            }
        }
    }
}
