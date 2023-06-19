package com.feintha.dpu.Events;

import com.feintha.dpu.DPUEvent;
import com.feintha.dpu.alib;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class DPUEntityEvent extends DPUEvent {
    Identifier requiredItemID = new Identifier("minecraft", "air");
    public DPUEntityEvent(JsonObject o) {
        super(o);
    }

    @Override
    public <T> boolean preProcessEvent(T data) {
        if (this.requiredNbt != null && requiredNbt.getSize() != 0) {
            if (data instanceof Entity s) {
                NbtCompound c = s.writeNbt(new NbtCompound());
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
