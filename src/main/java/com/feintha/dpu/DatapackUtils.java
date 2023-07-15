package com.feintha.dpu;

import com.feintha.dpu.commands.MovementSpeedCommand;
import com.feintha.dpu.commands.StringCommand;
import com.feintha.dpu.commands.TestForBlockCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class DatapackUtils implements ModInitializer {
    public static TagKey<Block> CAMPFIRE_SIGNAL_BLOCKS = TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft","campfire_signal_blocks"));
    /**
     * Runs the mod initializer.
     */
    public static boolean hasHadEvent = false;
    public static int CURRENT_TICK = 0;
    public static List<Consumer<MinecraftServer>> ScheduledActions = new ArrayList<>();
    public static void ScheduleForNextTick(Consumer<MinecraftServer> action) {
        ScheduledActions.add(action);
    }
//    public static DPUBlockHolderClass DPUBlockHolder = Registry.register(Registries.BLOCK, new Identifier("dpu", "block_holder"), new DPUBlockHolderClass(FabricBlockSettings.create()));
//    public static BlockEntityType<DPUBlockHolderClass.DPUBlockHolderEntity> DPUBlockHolderEntity = FabricBlockEntityTypeBuilder.create(DPUBlockHolderClass.DPUBlockHolderEntity::new, DPUBlockHolder).build();

    @Override
    public void onInitialize() {
        DPU.InitEverything();
        DPU.InitServerEvents();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TestForBlockCommand.register(dispatcher, registryAccess);
            StringCommand.register(dispatcher, registryAccess);
//            MovementSpeedCommand.register(dispatcher, registryAccess);
        });
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if (hasHadEvent) {
                DPUDataStorage.PopEverything(server);
            }
            for (var action : ScheduledActions) {
                action.accept(server);
            }
            ScheduledActions.clear();
            hasHadEvent = false;
        });
        ServerTickEvents.END_SERVER_TICK.register(
                server -> {CURRENT_TICK++;}
        );
    }
}
