package com.plutonium.plutoniummod.itens;

import com.plutonium.plutoniummod.blocks.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class LeadOreItem extends BlockItem {
    public LeadOreItem() {
        super(ModBlocks.LEAD_ORE.get(), new Item.Properties());
    }
}