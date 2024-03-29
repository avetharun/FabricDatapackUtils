package com.feintha.dpu.client;

import com.feintha.dpu.DPU;
import com.feintha.dpu.DPUDataStorage;
import com.feintha.dpu.client.ModelOverrides.BakedNBTModelOverride;
import com.feintha.dpu.client.ModelOverrides.BooleanModelOverride;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DatapackUtilsClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    public static int worldTick, clientTick;
    public static List<Consumer<ClientWorld>> ScheduledActions = new ArrayList<>();
    public static void ScheduleForNextWorldTick(Consumer<ClientWorld> action) {
        ScheduledActions.add(action);
    }
    public static NbtCompound activeRenderingItemStackCompound = null;
    @Override
    public void onInitializeClient() {
        DPU.InitEverything();

        ModelPredicateProviderRegistry.register(new Identifier("nbt"), new BakedNBTModelOverride());
        ModelPredicateProviderRegistry.register(new Identifier("is_hand_first"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandFirst(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_hand_third"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandThird(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_hand_any"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandAny(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_gui"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInGUI(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_inventory"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInGUI(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_fixed"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInFixedPos(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_dropped"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingAsDropped(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_hotbar"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHotbar(itemStack)));

        ModelPredicateProviderRegistry.register(new Identifier("use_time"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
            }
        });
        ModelPredicateProviderRegistry.register(new Identifier("is_using"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 0.07991f : 0.0F;
        });
        ModelPredicateProviderRegistry.register(new Identifier("is_swinging"), (stack, world, entity, seed) -> {
            return entity != null && entity.handSwinging && entity.getActiveItem() == stack ? 0.07991f : 0.0F;
        });
        ModelPredicateProviderRegistry.register(new Identifier("is_attacking"), (stack, world, entity, seed) -> {
            return entity != null && entity.handSwinging && entity.getActiveItem() == stack ? 0.07991f : 0.0F;
        });

        ClientTickEvents.END_WORLD_TICK.register(world -> {
            for (var action : ScheduledActions) {
                action.accept(world);
            }
            ScheduledActions.clear();
            worldTick++;
        });
        ClientTickEvents.END_CLIENT_TICK.register(world -> {
            clientTick++;
        });
        DPU.InitClientEvents();

    }
    public static boolean isModelTransformationInHand(ModelTransformationMode mode) {
        return mode.isFirstPerson() || mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
    }
    public static boolean isModelTransformationFixed(ModelTransformationMode mode) {
        return mode == ModelTransformationMode.FIXED;
    }
    public static boolean isModelTransformationGui(ModelTransformationMode mode) {
        return mode == ModelTransformationMode.GUI;
    }
    public static boolean isModelTransformationInHandFirst(ModelTransformationMode mode) {
        return mode.isFirstPerson();
    }
    public static boolean isModelTransformationInHandThird(ModelTransformationMode mode) {
        return mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
    }

}
