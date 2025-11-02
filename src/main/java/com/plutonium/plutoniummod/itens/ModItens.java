package com.plutonium.plutoniummod.itens;

import com.plutonium.plutoniummod.PlutoniumMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItens {
    public static final DeferredRegister<Item> ITENS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PlutoniumMod.MOD_ID);

    // Item Raw Pandorith (minério bruto)
    public static final RegistryObject<Item> RAW_PANDORITH = ITENS.register("raw_pandorith",
            () -> new PandorithItem());


}