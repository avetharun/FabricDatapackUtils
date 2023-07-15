package com.feintha.dpu.Events;

import com.feintha.dpu.DPUEvent;
import com.feintha.dpu.alib;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;

public class DPUItemEvent extends DPUEvent {
    public DPUItemEvent(JsonElement o) {
        super(o, DPUItemEvent::new);
    }
    @Override
    public <T> boolean preProcessEvent(T data) {
        if (this.requiredNbt != null && requiredNbt.getSize() != 0) {
            if (data instanceof ItemStack s) {
                NbtCompound c = s.writeNbt(new NbtCompound());
                return alib.checkNBTEquals(requiredNbt, c);
            } else if (data instanceof ItemUsageContext c) {
                NbtCompound cmp = c.getStack().writeNbt(new NbtCompound());
                cmp.putString("hand", c.getHand().name().toLowerCase());
                return alib.checkNBTEquals(requiredNbt, cmp);
            }
        }
        return false;
    }
}
