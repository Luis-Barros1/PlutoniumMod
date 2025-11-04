package com.plutonium.plutoniummod.blocks;

import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.itens.ModItens;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PlutoniumMod.MOD_ID);

    // Minério normal de Pandorith - SUPER MACIO
    public static final RegistryObject<Block> PANDORITH_ORE = BLOCKS.register("pandorith_ore",
            () -> new PandorithOreBlock());

    // Minério de Deepslate - SUPER MACIO
    public static final RegistryObject<Block> DEEPSLATE_PANDORITH_ORE = BLOCKS.register("deepslate_pandorith_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.4f, 0.4f)));  // ← SUPER MACIO

    // Bloco de Raw Pandorith - SUPER MACIO
    public static final RegistryObject<Block> RAW_PANDORITH_BLOCK = BLOCKS.register("raw_pandorith_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)));  // ← SUPER MACIO

    // Bloco de Descontaminação de Radiação
    public static final RegistryObject<Block> DECONTAMINATION_BLOCK = BLOCKS.register("decontamination_block",
            () -> new DecontaminationBlock());

    // Itens dos blocos (mantém igual)
    public static final RegistryObject<Item> PANDORITH_ORE_ITEM = ModItens.ITENS.register("pandorith_ore",
            () -> new BlockItem(PANDORITH_ORE.get(), new Item.Properties()));

    public static final RegistryObject<Item> DEEPSLATE_PANDORITH_ORE_ITEM = ModItens.ITENS.register("deepslate_pandorith_ore",
            () -> new BlockItem(DEEPSLATE_PANDORITH_ORE.get(), new Item.Properties()));

    public static final RegistryObject<Item> RAW_PANDORITH_BLOCK_ITEM = ModItens.ITENS.register("raw_pandorith_block",
            () -> new BlockItem(RAW_PANDORITH_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> DECONTAMINATION_BLOCK_ITEM = ModItens.ITENS.register("decontamination_block",
            () -> new BlockItem(DECONTAMINATION_BLOCK.get(), new Item.Properties()));
}