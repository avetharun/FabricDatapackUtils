package com.feintha.dpu.mixin;

import com.feintha.dpu.alib;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Inject(at=@At("HEAD"), method="renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V")
    public void renderItemWithSeed(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo ci) {
        alib.setMixinField(stack, "transformationMode", renderMode);
    }
    @Inject(at=@At("HEAD"), method="renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V")
    public void renderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        alib.setMixinField(stack, "transformationMode", renderMode);
    }
    @Mixin(InGameHud.class)
    public static class InGameHudMixin{
        @Inject(method="renderHotbarItem", at=@At("HEAD"))
        void injectRenderHotbarItem(MatrixStack matrixStack, int i, int j, float f, PlayerEntity playerEntity, ItemStack itemStack, int k, CallbackInfo ci){
            alib.setMixinField(itemStack, "isBeingRenderedInHotbar", true);
        }
        @Inject(method="renderHotbarItem", at=@At("RETURN"))
        void injectRenderHotbarItemEnd(MatrixStack matrixStack, int i, int j, float f, PlayerEntity playerEntity, ItemStack itemStack, int k, CallbackInfo ci){
            alib.setMixinField(itemStack, "isBeingRenderedInHotbar", false);
        }
    }

}
