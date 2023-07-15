package com.feintha.dpu.client.ModelOverrides;

import com.feintha.dpu.alib;
import com.mojang.datafixers.util.Function4;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class BakedNBTModelOverride extends BooleanModelOverride{
    public BakedNBTModelOverride(){
        // Handled via mixin!!!
        super((itemStack, clientWorld, livingEntity, integer) -> true);
    }
    public float call(ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity livingEntity, int i, NbtCompound requestedCompound) {
        return alib.checkNBTEquals(itemStack.getNbt(), requestedCompound) ? 0x7991a2 : 0;
    }
}
