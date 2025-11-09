package com.plutonium.plutoniummod.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class LeadOreBlock extends Block {

    public LeadOreBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(2.0f)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
        );
    }
}
