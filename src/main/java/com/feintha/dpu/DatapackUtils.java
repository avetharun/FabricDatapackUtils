package com.feintha.dpu;

import com.feintha.dpu.commands.StringCommand;
import com.feintha.dpu.commands.TestForBlockCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class DatapackUtils implements ModInitializer {
    /**
     * Runs the mod initializer.
     */
    public static boolean hasHadEvent = false;
    public static int CURRENT_TICK = 0;
    public static List<Consumer<MinecraftServer>> ScheduledActions = new ArrayList<>();
    public static void ScheduleForNextTick(Consumer<MinecraftServer> action) {
        ScheduledActions.add(action);
    }
    @Override
    public void onInitialize() {
        DPU.InitEverything();
        DPU.InitServerEvents();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TestForBlockCommand.register(dispatcher, registryAccess);
            StringCommand.register(dispatcher, registryAccess);
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
