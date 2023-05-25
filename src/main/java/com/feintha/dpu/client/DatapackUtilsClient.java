package com.feintha.dpu.client;

import com.feintha.dpu.alib;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.util.Identifier;

public class DatapackUtilsClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {


        ModelPredicateProviderRegistry.register(new Identifier("is_hand_first"), (stack, world, entity, seed) -> isModelTransformationInHandFirst(alib.getMixinField(stack, "transformationMode")) && !(boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar")? 0.07991f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_hand_third"), (stack, world, entity, seed) -> isModelTransformationInHandThird(alib.getMixinField(stack, "transformationMode")) && !(boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar")? 0.07991f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_hand_any"), (stack, world, entity, seed) -> isModelTransformationInHand(alib.getMixinField(stack, "transformationMode")) && !(boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar") ? .1f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_gui"), (stack, world, entity, seed) -> alib.getMixinField(stack, "transformationMode") == ModelTransformationMode.GUI || (boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar")? 0.134192f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_inventory"), (stack, world, entity, seed) -> alib.getMixinField(stack, "transformationMode") == ModelTransformationMode.GUI || (boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar") ? 0.134192f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_fixed"), (stack, world, entity, seed) -> alib.getMixinField(stack, "transformationMode") == ModelTransformationMode.FIXED && !(boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar")? 0.07991f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_dropped"), (stack, world, entity, seed) -> alib.getMixinField(stack, "transformationMode") == ModelTransformationMode.GROUND && !(boolean)alib.getMixinField(stack, "isBeingRenderedInHotbar")? 0.07991f : 0f);
        ModelPredicateProviderRegistry.register(new Identifier("is_hotbar"), (stack, world, entity, seed) -> alib.getMixinField(stack, "isBeingRenderedInHotbar"));
        ModelPredicateProviderRegistry.register(new Identifier("bow_pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
            }
        });

        ModelPredicateProviderRegistry.register(new Identifier("bow_pulling"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
        });
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
