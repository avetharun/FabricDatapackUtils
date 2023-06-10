package com.feintha.dpu.commands;

import com.feintha.dpu.DPUFastAsyncSearch;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;

public class TestForBlockCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access){
        dispatcher.register(CommandManager.literal("testforblock")
                .requires(source -> source.hasPermissionLevel(2)).then(
                        CommandManager.argument("start", BlockPosArgumentType.blockPos())
                                .then(CommandManager.argument("end", BlockPosArgumentType.blockPos()).then(CommandManager.argument("block", BlockStateArgumentType.blockState(access))
                                        .executes(TestForBlockCommand::checkForBlock)
                                )))


        );
    }
    static int checkForBlock(CommandContext<ServerCommandSource> context){
        BlockPos start = BlockPosArgumentType.getBlockPos(context, "start");
        BlockPos end = BlockPosArgumentType.getBlockPos(context, "end");
        BlockState state = BlockStateArgumentType.getBlockState(context, "block").getBlockState();
        boolean r = DPUFastAsyncSearch.CheckBlockStateInRadius(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ(), context.getSource().getWorld(), state);
        return r ? 1 : 0;
    }
}
