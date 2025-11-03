package com.plutonium.plutoniummod.registry;

import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.effects.RadiationSicknessEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, PlutoniumMod.MOD_ID);
    public static final RegistryObject<MobEffect> RADIATION_SICKNESS = EFFECTS.register("radiation_sickness", RadiationSicknessEffect::new);
}
