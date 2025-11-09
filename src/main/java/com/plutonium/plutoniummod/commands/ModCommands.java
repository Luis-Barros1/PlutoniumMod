package com.plutonium.plutoniummod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.plutonium.plutoniummod.PlutoniumMod;
import com.plutonium.plutoniummod.system.radiation.RadiationService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlutoniumMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ModCommands {

    private ModCommands() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("plutonium")
                        .requires(source -> source.hasPermission(2))
                        .then(
                                Commands.literal("radiation")
                                        .then(
                                                Commands.argument("player", EntityArgument.player())
                                                        .then(
                                                                Commands.literal("off")
                                                                        .executes(context -> disableRadiation(
                                                                                context.getSource(),
                                                                                EntityArgument.getPlayer(context, "player")
                                                                        ))
                                                        )
                                                        .then(
                                                                Commands.literal("on")
                                                                        .executes(context -> enableRadiation(
                                                                                context.getSource(),
                                                                                EntityArgument.getPlayer(context, "player")
                                                                        ))
                                                        )
                                        )
                        )
        );
    }

    private static int disableRadiation(CommandSourceStack source, ServerPlayer target) {
        RadiationService.disableRadiation(target);

        Component feedback = Component.translatable("commands.plutonium.radiation.off", target.getDisplayName());
        source.sendSuccess(() -> feedback, true);
        target.sendSystemMessage(Component.translatable("commands.plutonium.radiation.off.self"));

        return Command.SINGLE_SUCCESS;
    }

    private static int enableRadiation(CommandSourceStack source, ServerPlayer target) {
        if (target.gameMode.isCreative()) {
            source.sendFailure(Component.translatable("commands.plutonium.radiation.on.fail", target.getDisplayName()));
            return 0;
        }

        RadiationService.enableRadiation(target);

        Component feedback = Component.translatable("commands.plutonium.radiation.on", target.getDisplayName());
        source.sendSuccess(() -> feedback, true);
        target.sendSystemMessage(Component.translatable("commands.plutonium.radiation.on.self"));

        return Command.SINGLE_SUCCESS;
    }
}

