package com.feintha.dpu.client.ModelOverrides;

import com.feintha.dpu.alib;
import com.mojang.datafixers.util.Function4;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BooleanModelOverride implements ClampedModelPredicateProvider {
    public BooleanModelOverride(Function4<ItemStack, ClientWorld, LivingEntity, Integer, Boolean> p) {
        this.predicate = p;
    }
    public float execute(ItemStack s, ClientWorld w, LivingEntity e, int seed) {
        if (predicate == null) {return 0;}
        return predicate.apply(s, w, e, seed) ? 0.07991f : 0f;
    }
    public static ModelTransformationMode getTransformationModeFor(ItemStack stack) {
        return alib.getMixinField(stack, "transformationMode");
    }
    public static boolean isRenderingInGUI(ItemStack stack) {
        ModelTransformationMode m = alib.getMixinField(stack, "transformationMode");
        if (m == null) {return false;}
        boolean bl1 = alib.getMixinField(stack, "isBeingRenderedInHotbar");
        return bl1 || m == ModelTransformationMode.GUI;
    }
    public static boolean isRenderingInHotbar(ItemStack stack) {
        return alib.getMixinField(stack, "isBeingRenderedInHotbar");
    }
    public static boolean isRenderingInHandFirst(ItemStack stack) {
        ModelTransformationMode m = alib.getMixinField(stack, "transformationMode");
        if (m == null) {return false;}
        boolean bl1 = alib.getMixinField(stack, "isBeingRenderedInHotbar");
        return !bl1 && m.isFirstPerson();
    }
    public static boolean isRenderingInHandThird(ItemStack stack) {
        ModelTransformationMode m = alib.getMixinField(stack, "transformationMode");
        if (m == null) {return false;}
        boolean bl1 = alib.getMixinField(stack, "isBeingRenderedInHotbar");
        return !bl1 && (m == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || m == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND);
    }
    public static boolean isRenderingInHandAny(ItemStack stack) {
        ModelTransformationMode m = alib.getMixinField(stack, "transformationMode");
        if (m == null) {return false;}
        boolean bl1 = alib.getMixinField(stack, "isBeingRenderedInHotbar");
        return !bl1 && (m == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || m == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND || m.isFirstPerson());
    }
    public static boolean isRenderingInFixedPos(ItemStack stack) {
        ModelTransformationMode m = alib.getMixinField(stack, "transformationMode");
        if (m == null) {return false;}
        boolean bl1 = alib.getMixinField(stack, "isBeingRenderedInHotbar");
        return !bl1 && m == ModelTransformationMode.FIXED;
    }
    public static boolean isRenderingAsDropped(ItemStack stack) {
        ModelTransformationMode m = alib.getMixinField(stack, "transformationMode");
        if (m == null) {return false;}
        boolean bl1 = alib.getMixinField(stack, "isBeingRenderedInHotbar");
        return !bl1 && m == ModelTransformationMode.GROUND;
    }
    public final Function4<ItemStack, ClientWorld, LivingEntity, Integer, Boolean> predicate;

    @Override
    public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
        return execute(stack, world, entity, seed);
    }
}
