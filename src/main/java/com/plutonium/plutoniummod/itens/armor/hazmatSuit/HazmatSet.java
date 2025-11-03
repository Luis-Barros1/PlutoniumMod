package com.plutonium.plutoniummod.itens.armor.hazmatSuit;

import com.plutonium.plutoniummod.itens.ModItens;
import com.plutonium.plutoniummod.itens.armor.interfaces.IArmorSet;
import com.plutonium.plutoniummod.registry.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HazmatSet implements IArmorSet {

    public static final HazmatSet INSTANCE = new HazmatSet();
    private static final MobEffect SET_BONUS_EFFECT = ModEffects.SHELTER_EFFECT.get();

    @Override
    public boolean isFullSetEquipped(Player player) {
        ItemStack helmet = player.getInventory().getArmor(3);
        ItemStack chestplate = player.getInventory().getArmor(2);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack boots = player.getInventory().getArmor(0);

        return !helmet.isEmpty() && helmet.getItem() == ModItens.HAZMAT_HELMET.get() &&
                !chestplate.isEmpty() && chestplate.getItem() == ModItens.HAZMAT_CHESTPLATE.get() &&
                !leggings.isEmpty() && leggings.getItem() == ModItens.HAZMAT_LEGGINGS.get() &&
                !boots.isEmpty() && boots.getItem() == ModItens.HAZMAT_BOOTS.get();
    }

    @Override
    public void applySetBonus(Player player) {
        player.addEffect(new MobEffectInstance(SET_BONUS_EFFECT, 40, 0, false, false, true));
    }

    @Override
    public void removeSetBonus(Player player) {
        player.removeEffect(SET_BONUS_EFFECT);
    }
}
