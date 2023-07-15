package com.feintha.dpu.mixin.Client;

import com.feintha.dpu.alib;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ModelOverride.class)
public class ModelOverrideMixin {
//    @Unique
//    public boolean isNBTOverride = false;
//    @Unique
//    public NbtCompound data = new NbtCompound();
//    @Mixin(ModelOverride.Condition.class)
//    public static class ModelOverrideConditionMixin{
//        @Unique
//        public boolean isNBTOverride = false;
//        @Unique
//        public NbtCompound data = new NbtCompound();
//
//
//    }
//    @Mixin(ModelOverrideList.BakedOverride.class)
//    public static class BakedOverrideMixin{
//        @Unique
//        public boolean isNBTOverride = false;
//        @Unique
//        public NbtCompound compound = new NbtCompound();
//        @Unique
//        private boolean testNBT(ItemStack s) {
//            return alib.checkNBTEquals(s.getOrCreateNbt(), compound);
//        }
//
//    }
}
