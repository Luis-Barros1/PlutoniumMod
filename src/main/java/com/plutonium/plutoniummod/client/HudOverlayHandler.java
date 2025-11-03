package com.plutonium.plutoniummod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.registry.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PlutoniumMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HudOverlayHandler {

    private static final ResourceLocation RADIO_HEARTS = new ResourceLocation(PlutoniumMod.MOD_ID, "textures/gui/heart_radioactive.png");

    @SubscribeEvent
    public static void onRenderHealth(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() != VanillaGuiOverlay.PLAYER_HEALTH.type()) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        boolean radioactive = player.hasEffect(ModEffects.RADIATION_SICKNESS.get());
        if (!radioactive) return; // deixa vanilla renderizar normalmente quando não está com o efeito

        // Cancela vanilla e desenha nossos corações
        event.setCanceled(true);

        GuiGraphics g = event.getGuiGraphics();

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int x = 10; // posição base (similar ao vanilla)
        int y = screenHeight - 39;

        renderRadioactiveHearts(g, player, x, y);
    }

    private static void renderRadioactiveHearts(GuiGraphics g, Player player, int x, int y) {
        // Configuração de sprite: 3 tiles 9x9 lado a lado: [empty | half | full]
        int tileW = 9;
        int tileH = 9;

        int healthHalfHearts = (int) Math.ceil(player.getMaxHealth() / 2.0);
        int currentHalfHearts = (int) Math.ceil(Math.max(0.0F, player.getHealth()) / 2.0F);
        boolean odd = ((int) Math.ceil(player.getHealth())) % 2 == 1; // meio coração se ímpar

        // Bind textura
        RenderSystem.enableBlend();
        g.blit(RADIO_HEARTS, 0, 0, 0, 0, 0, 0, 27, 9); // noop para garantir atlas/contexto; largura total esperada 27px

        for (int i = 0; i < healthHalfHearts; i++) {
            int drawX = x + i % 10 * (tileW + 1);
            int row = i / 10;
            int drawY = y - row * (tileH + 1);

            // Container vazio
            g.blit(RADIO_HEARTS, drawX, drawY, 0, 0, 0, tileW, tileH, 27, 9);

            // Vida
            int heartIndex = i * 2 + 2; // valor inteiro de meio-corações que este slot cobre
            if (heartIndex <= currentHalfHearts) {
                // Coração cheio
                g.blit(RADIO_HEARTS, drawX, drawY, 18, 0, tileW, tileH, 27, 9);
            } else if (heartIndex - 1 == currentHalfHearts && odd) {
                // Meio coração
                g.blit(RADIO_HEARTS, drawX, drawY, 9, 0, tileW, tileH, 27, 9);
            }
        }

        RenderSystem.disableBlend();
    }
}


