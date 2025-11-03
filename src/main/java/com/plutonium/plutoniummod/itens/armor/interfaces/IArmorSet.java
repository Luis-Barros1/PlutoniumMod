package com.plutonium.plutoniummod.itens.armor.interfaces;

import net.minecraft.world.entity.player.Player;

public interface IArmorSet {
    /**
     * Verifica se o jogador está usando o set completo.
     * @param player O jogador a ser verificado.
     * @return true se o set estiver completo, false caso contrário.
     */
    boolean isFullSetEquipped(Player player);

    /**
     * Aplica o bônus do set. Chamado a cada tick que o set está completo.
     * @param player O jogador que recebe o bônus.
     */
    void applySetBonus(Player player);

    /**
     * Remove o bônus do set. Chamado quando o set é quebrado.
     * @param player O jogador que perde o bônus.
     */
    void removeSetBonus(Player player);
}
