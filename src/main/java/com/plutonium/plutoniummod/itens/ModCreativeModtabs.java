package com.plutonium.plutoniummod.itens;


import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.blocks.LeadOreBlock;
import com.plutonium.plutoniummod.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModtabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PlutoniumMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("plutoniun_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItens.RAW_PANDORITH.get()))
                    .title(Component.translatable("creativetab.plutoniun_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItens.RAW_PANDORITH.get());
                        pOutput.accept(ModItens.LEAD_INGOT.get());
                        pOutput.accept(ModBlocks.LEAD_BLOCK_ITEM.get());
                        pOutput.accept(ModItens.LEAD_ORE.get());
                        pOutput.accept(ModBlocks.STEEL_BLOCK_ITEM.get());
                        pOutput.accept(ModBlocks.GRANDMA_FLOOR_ITEM.get());
                        pOutput.accept(ModBlocks.RAW_PANDORITH_BLOCK_ITEM.get());
                        pOutput.accept(ModBlocks.CHECKERED_TILE_ITEM.get());
                        pOutput.accept(ModBlocks.CERAMIC_PANEL_ITEM.get());


                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
