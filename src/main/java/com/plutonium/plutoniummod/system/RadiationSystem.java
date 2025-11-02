package com.plutonium.plutoniummod.system;

import com.plutonium.plutoniummod.PlutoniumMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = PlutoniumMod.MOD_ID)
public class RadiationSystem {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        String biome = player.level().getBiome(player.blockPosition()).unwrapKey().get().location().toString();

        if (player.level().isClientSide) return;
        if (event.phase == TickEvent.Phase.END) return;

        double radiation = player.getPersistentData().getDouble("Radiation_level");
        double change = 0.0;

        if (radiation >= 0.0) {
            if (player.blockPosition().getY() < 40 && player.blockPosition().getY() > 20) {
                change -= 0.2;
            }

            if (player.blockPosition().getY() < 20) {
                change -= 0.4;
            }
        }

        if (radiation <= 1000) {
            //Condionais e configs de radiação por nível
            if (player.blockPosition().getY() > 40) {
                change += 0.2;
            }
            if (biome.contains("desert")) {
                change += 0.1;
            }
        }

        radiation += change;
        player.getPersistentData().putDouble("Radiation_level", radiation);
        LOGGER.info("Evento Tick. Radiação: {}", radiation);


        //Efeitos do Jogador
        if (player.getPersistentData().getDouble("Radiation_level") > 100) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 2, 0, true, false));
        }
    }
}
