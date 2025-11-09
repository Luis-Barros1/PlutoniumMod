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

    private static final ResourceLocation RADIO_HEARTS = ResourceLocation.fromNamespaceAndPath(PlutoniumMod.MOD_ID, "textures/gui/heart_radioactive.png");

    @SubscribeEvent
    public static void onRenderHealth(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() != VanillaGuiOverlay.PLAYER_HEALTH.type()) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        boolean radioactive = player.hasEffect(ModEffects.RADIATION_SICKNESS.get());
        if (!radioactive) return; // deixa vanilla renderizar normalmente quando não está com o efeito

        // Cancela vanilla e desenha nossos corações no lugar exato
        event.setCanceled(true);

        GuiGraphics g = event.getGuiGraphics();

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        
        // Calcula posição Y dinâmica baseada na presença de fome
        // Hotbar padrão: screenHeight - 22
        // Fome padrão: screenHeight - 32 (10 pixels acima da hotbar)
        // Corações padrão: screenHeight - 42 (10 pixels acima da fome) ou screenHeight - 32 (sem fome)
        int top = screenHeight - 39;
        
        // Calcula posição X centralizada dinamicamente (não usa X fixo)
        int totalHearts = (int) Math.ceil(player.getMaxHealth() / 2.0);
        int heartsPerRow = 10;
        int heartsInFirstRow = Math.min(totalHearts, heartsPerRow);
        
        // Centraliza horizontalmente na tela
        int left = (screenWidth / 2) - 91;

        renderRadioactiveHearts(g, player, left, top);
    }

    private static void renderRadioactiveHearts(GuiGraphics g, Player player, int x, int y) {
        // Configuração de sprite: 3 tiles 9x9 lado a lado: [empty | half | full]
        // Renderiza em tamanho reduzido (7x7 pixels na tela)
        int tileW = 9;
        int tileH = 9;
        int heartsPerRow = 10;

        float health = Math.max(0.0F, player.getHealth());
        float maxHealth = player.getMaxHealth();
        
        // Calcula quantos corações completos temos (cada coração = 2 meio-corações)
        int totalHearts = (int) Math.ceil(maxHealth / 2.0);
        int fullHearts = (int) (health / 2.0);
        boolean hasHalfHeart = (health % 2.0F) >= 1.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        for (int i = 0; i < totalHearts; i++) {
            int col = i % heartsPerRow;
            int row = i / heartsPerRow;
            int drawX = x + col * 8;
            // Para múltiplas linhas, cada linha acima subtrai altura (Y aumenta para baixo)
            int drawY = y - (row * (tileH));

            // Desenha container vazio primeiro (u=0, v=0)
            // Renderiza tileW x tileH na tela, mas pega sourceTileW x sourceTileH da textura
            g.blit(RADIO_HEARTS, drawX, drawY, 0, 0, tileW, tileH, 27, 9);

            // Desenha vida atual
            if (i < fullHearts) {
                // Coração cheio (u=18, v=0)
                g.blit(RADIO_HEARTS, drawX, drawY, 18, 0, tileW, tileH, 27, 9);
            } else if (i == fullHearts && hasHalfHeart) {
                // Meio coração (u=9, v=0)
                g.blit(RADIO_HEARTS, drawX, drawY, 9, 0, tileW, tileH, 27, 9);
            } else if (i == 0) {
                g.blit(RADIO_HEARTS, drawX, drawY, 9, 0, tileW, tileH, 27, 9);
            }
        }

        RenderSystem.disableBlend();
    }
}


