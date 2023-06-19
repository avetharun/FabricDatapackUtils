package com.feintha.dpu.Events;

import com.feintha.dpu.DPUEvent;
import com.feintha.dpu.alib;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class DPUBlockEvent extends DPUEvent {
    Identifier requiredItemID = new Identifier("minecraft", "air");

    public DPUBlockEvent(JsonObject o) {
        super(o);
    }

    @Override
    public <T> boolean preProcessEvent(T data) {
        if (this.requiredNbt != null && requiredNbt.getSize() != 0) {
            if (data instanceof BlockState s) {
                NbtCompound c = new NbtCompound();
                alib.packBlockStateIntoCompound(s, c);
                return alib.checkNBTEquals(requiredNbt, c) && super.preProcessEvent(data);
            }
        }
        return super.preProcessEvent(data);
    }

    @Override
    public DPUEvent Deserialize(JsonObject object) {
        if (object.has("requiredItem")) {
            this.requiredItemID = new Identifier(object.get("requiredItem").getAsString());
        }
        return super.Deserialize(object);
    }
}
