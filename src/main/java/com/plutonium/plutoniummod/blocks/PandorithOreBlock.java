package com.plutonium.plutoniummod.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class PandorithOreBlock extends Block {

    public PandorithOreBlock() {
        super(Properties.of()
                .mapColor(MapColor.STONE)
                .strength(0.3f, 0.3f));  // ← SUPER MACIO para teste
    }
}