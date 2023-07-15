package com.feintha.dpu.mixin.Client;

import com.feintha.dpu.alib;
import com.feintha.dpu.client.ModelOverrides.BakedNBTModelOverride;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ModelOverrideList.class)
public abstract class ModelOverrideListMixin {
//    @Redirect(method="<init>(Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/List;)V",
//            at=@At(value = "NEW", target = "([Lnet/minecraft/client/render/model/json/ModelOverrideList$InlinedCondition;Lnet/minecraft/client/render/model/BakedModel;)Lnet/minecraft/client/render/model/json/ModelOverrideList$BakedOverride;"))
//    ModelOverrideList.BakedOverride redirectMixin(ModelOverrideList.InlinedCondition[] inlinedConditions, BakedModel bakedModel, @Local(ordinal = 0) ModelOverride override){
//        var bakedOverride = new ModelOverrideList.BakedOverride(inlinedConditions, bakedModel);
//        alib.setMixinField(bakedOverride, "compound", alib.getMixinField(override, "data"));
//        alib.setMixinField(bakedOverride, "isNBTOverride", alib.getMixinField(override, "isNBTOverride"));
//        return bakedOverride;
//    }

//    @Inject(
//            locals = LocalCapture.CAPTURE_FAILEXCEPTION,
//            method="apply", at=@At(shift = At.Shift.BEFORE, value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/ModelOverrideList$BakedOverride;test([F)Z"), cancellable = true)
//    private void applyMixin(BakedModel model, ItemStack stack, ClientWorld world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir, Item item, int i, float[] fs, ModelOverrideList.BakedOverride[] var9, int var10, int var11, ModelOverrideList.BakedOverride bakedOverride){
//        boolean bl1 = alib.getMixinField(bakedOverride, "isNBTOverride");
//        if (bl1) {
//            boolean bl2 = alib.runPrivateMixinMethod(bakedOverride, "testNBT", stack);
//            if (bl2) {
//                BakedModel bakedModel = bakedOverride.model;
//                cir.setReturnValue(bakedModel);
//                cir.cancel();
//            }
//        }
//
//    }
}
