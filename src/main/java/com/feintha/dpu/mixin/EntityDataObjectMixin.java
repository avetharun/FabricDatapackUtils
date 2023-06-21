package com.feintha.dpu.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.EntityDataObject;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.transformer.meta.MixinInner;

import java.util.UUID;

@Mixin(EntityDataObject.class)
public class EntityDataObjectMixin {
    @Shadow @Final private Entity entity;

    @Inject(method="setNbt", at=@At("HEAD"), cancellable = true)
    void injectSetNBT(NbtCompound nbt, CallbackInfo ci){

        UUID uUID = this.entity.getUuid();
        this.entity.readNbt(nbt);
        this.entity.setUuid(uUID);
        ci.cancel();
    }
}
