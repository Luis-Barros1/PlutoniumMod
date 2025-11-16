package com.plutonium.plutoniummod.blocks;

import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.itens.ModItens;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
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
                    .strength(2.5f)
                    .sound(SoundType.DEEPSLATE)
                    .requiresCorrectToolForDrops()));  // ← SUPER MACIO

    // Bloco de Raw Pandorith - SUPER MACIO
    public static final RegistryObject<Block> RAW_PANDORITH_BLOCK = BLOCKS.register("raw_pandorith_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)));  // ← SUPER MACIO

    // Minério de Chumbo
    public static final RegistryObject<Block> LEAD_ORE = BLOCKS.register("lead_ore", LeadOreBlock::new);

    public static final RegistryObject<Block> DEEPSLATE_LEAD_ORE = BLOCKS.register("deepslate_lead_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2.5f)
                    .sound(SoundType.DEEPSLATE)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> CAUTION_BLOCK = BLOCKS.register("caution_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> STEEL_BLOCK = BLOCKS.register("steel_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> CHECKERED_TILE = BLOCKS.register("checkered_tile",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4.0f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> LEAD_BLOCK = BLOCKS.register("lead_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> GRANDMA_FLOOR = BLOCKS.register("grandma_floor",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4.0f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> CERAMIC_PANEL = BLOCKS.register("ceramic_panel",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4.0f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));



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

    public static final RegistryObject<Item> DEEPSLATE_LEAD_ORE_ITEM = ModItens.ITENS.register("deepslate_lead_ore",
            () -> new BlockItem(DEEPSLATE_LEAD_ORE.get(), new Item.Properties()));

    public static final RegistryObject<Item> CAUTIO_BLOCK = ModItens.ITENS.register("caution_block",
            () -> new BlockItem(CAUTION_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> STEEL_BLOCK_ITEM = ModItens.ITENS.register("steel_block",
            () -> new BlockItem(STEEL_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> CHECKERED_TILE_ITEM = ModItens.ITENS.register("checkered_tile",
            () -> new BlockItem(CHECKERED_TILE.get(), new Item.Properties()));

    public static final RegistryObject<Item> LEAD_BLOCK_ITEM = ModItens.ITENS.register("lead_block",
            () -> new BlockItem(LEAD_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> GRANDMA_FLOOR_ITEM = ModItens.ITENS.register("grandma_floor",
            () -> new BlockItem(GRANDMA_FLOOR.get(), new Item.Properties()));

    public static final RegistryObject<Item> CERAMIC_PANEL_ITEM = ModItens.ITENS.register("ceramic_panel",
            () -> new BlockItem(CERAMIC_PANEL.get(), new Item.Properties()));

}