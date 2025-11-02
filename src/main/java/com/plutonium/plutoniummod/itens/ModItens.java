package com.plutonium.plutoniummod.itens;

import com.plutonium.plutoniummod.PlutoniumMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItens {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PlutoniumMod.MOD_ID);

    public static final RegistryObject<Item> HAZMAT_HELMET = ITEMS.register("hazmat_helmet",
            () -> new ArmorItem(ArmorMaterials.GOLD, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(100)));

    public static final RegistryObject<Item> HAZMAT_CHESTPLATE = ITEMS.register("hazmat_chestplate",
            () -> new ArmorItem(ArmorMaterials.GOLD, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(130)));

    public static final RegistryObject<Item> HAZMAT_LEGGINGS = ITEMS.register("hazmat_leggings",
            () -> new ArmorItem(ArmorMaterials.GOLD, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(120)));

    public static final RegistryObject<Item> HAZMAT_BOOTS = ITEMS.register("hazmat_boots",
            () -> new ArmorItem(ArmorMaterials.GOLD, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(90)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
