package com.feintha.dpu.client;

import com.feintha.dpu.DPU;
import com.feintha.dpu.DPUEventType;
import com.feintha.dpu.DatapackUtils;
import com.feintha.dpu.alib;
import com.google.gson.JsonParser;
import com.google.gson.JsonStreamParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.RegExUtils;

import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

public class DatapackUtilsClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    public static int worldTick, clientTick;
    @Override
    public void onInitializeClient() {
        DPU.InitEverything();


        ModelPredicateProviderRegistry.register(new Identifier("is_hand_first"), (stack, world, entity, seed) -> isModelTransformationInHandFirst(alib.getMixinField(stack, "transformationMode")) && !(boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar")? 0.07991f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_hand_third"), (stack, world, entity, seed) -> isModelTransformationInHandThird(alib.getMixinField(stack, "transformationMode")) && !(boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar")? 0.07991f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_hand_any"), (stack, world, entity, seed) -> isModelTransformationInHand(alib.getMixinField(stack, "transformationMode")) && !(boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar") ? .1f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_gui"), (stack, world, entity, seed) -> alib.getMixinField(stack, "transformationMode") == ModelTransformationMode.GUI || (boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar")? 0.134192f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_inventory"), (stack, world, entity, seed) -> alib.getMixinField(stack, "transformationMode") == ModelTransformationMode.GUI || (boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar") ? 0.134192f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_fixed"), (stack, world, entity, seed) -> alib.getMixinField(stack, "transformationMode") == ModelTransformationMode.FIXED && !(boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar")? 0.07991f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_dropped"), (stack, world, entity, seed) -> alib.getMixinField(stack, "transformationMode") == ModelTransformationMode.GROUND && !(boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar")? 0.07991f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_hotbar"), (stack, world, entity, seed) -> alib.getMixinField(stack, "isBeingRenderedInHotbar"));
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
            worldTick++;
        });
        ClientTickEvents.END_CLIENT_TICK.register(world -> {
            worldTick++;
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
