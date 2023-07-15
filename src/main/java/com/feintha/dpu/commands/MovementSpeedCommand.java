package com.feintha.dpu.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class MovementSpeedCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access){
        dispatcher.register(CommandManager.literal("speed")
            .then(CommandManager.argument("players", EntityArgumentType.players())

                .then(CommandManager.literal("flight").then(CommandManager.argument("speed", FloatArgumentType.floatArg(0)).executes(context -> {
                    var es = EntityArgumentType.getPlayers(context, "players");
                    if (es.size() == 0) {return 0;}
                    es.forEach(entity -> {
                        setFlightSpeed(entity, FloatArgumentType.getFloat(context, "speed"));
                    });
                    return 1;
                })))
                .then(CommandManager.literal("walking").then(CommandManager.argument("speed", FloatArgumentType.floatArg(0)).executes(context -> {
                    var es = EntityArgumentType.getPlayers(context, "players");
                    if (es.size() == 0) {return 0;}
                    es.forEach(entity -> {
                        setWalkSpeed(entity, FloatArgumentType.getFloat(context, "speed"));
                    });
                    return 1;
                })))
            )
        );
    }
    static void setWalkSpeed(ServerPlayerEntity entity, float speed) {
        entity.getAbilities().setWalkSpeed(speed);
    }
    static void setFlightSpeed(ServerPlayerEntity entity, float speed) {
        entity.getAbilities().setFlySpeed(speed);
    }
}
