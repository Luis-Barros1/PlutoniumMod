package com.plutonium.plutoniummod.itens;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;

public class HazmatHelmet extends ArmorItem {
    public HazmatHelmet(Properties properties) {
        super(ArmorMaterials.CHAIN,,properties);
        properties.durability(500);

    }
}
