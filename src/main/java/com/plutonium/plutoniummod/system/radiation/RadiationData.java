package com.plutonium.plutoniummod.system.radiation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class RadiationData {

    private final Player player;
    private final CompoundTag data;
    public static final String KEY_LEVEL = "Radiation_Level";
    public static final String KEY_RESISTANCE = "Radiation_Resistance";
    public static final String KEY_CAN_RECEIVE = "Can_Receive_Radiation";

    public RadiationData(Player player) {
        this.player = player;
        this.data = player.getPersistentData();
    }

    public double getRadiationLevel() {
        return data.getDouble(KEY_LEVEL);
    }

    public void setRadiationLevel(double value) {
        data.putDouble(KEY_LEVEL, value);
    }

    public double getRadiationResistance() {
        return data.getDouble(KEY_RESISTANCE);
    }

    public void setRadiationResistance(double value) {
        data.putDouble(KEY_RESISTANCE, value);
    }

    public boolean canReceiveRadiation() {
        if (!data.contains(KEY_CAN_RECEIVE))
            data.putBoolean(KEY_CAN_RECEIVE, true);
        return data.getBoolean(KEY_CAN_RECEIVE);
    }

    public void setCanReceiveRadiation(boolean value) {
        data.putBoolean(KEY_CAN_RECEIVE, value);
    }
}
