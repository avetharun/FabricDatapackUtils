package com.feintha.dpu.Events;

import com.feintha.dpu.DPUEvent;
import com.feintha.dpu.alib;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class DPUItemEvent extends DPUEvent {
    public DPUItemEvent(JsonObject o) {
        super(o);
    }
    @Override
    public <T> boolean preProcessEvent(T data) {
        if (this.requiredNbt != null && requiredNbt.getSize() != 0) {
            if (data instanceof ItemStack s) {
                NbtCompound c = s.writeNbt(new NbtCompound());
                return alib.checkNBTEquals(requiredNbt, c) && super.preProcessEvent(data);
            }
        }
        return super.preProcessEvent(data);
    }
}
