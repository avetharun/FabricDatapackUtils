package com.feintha.dpu.Events;

import com.feintha.dpu.DPUEvent;
import com.feintha.dpu.alib;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class DPUEntityEvent extends DPUEvent {

    public static class DPUEntityRandomEvent extends DPUEntityEvent{
        public DPUEntityRandomEvent(JsonElement o) {
            super(o);
        }
        float chance = 0;
        int min_ticks;
        int ticks_since_last = 0;
        @Override
        public DPUEvent Deserialize(JsonObject object) {
            min_ticks = JsonHelper.getInt(object, "minimum_ticks", 10);
            if (object.has("chance")) {
                float c =object.get("chance").getAsFloat();
                if (c > 1) {
                    chance = c * 0.01f;
                } else if (c > 0){
                    chance = c;
                }
            }
            return super.Deserialize(object);
        }

        @Override
        public <T> boolean preProcessEvent(T data) {
            if (data instanceof Entity e) {
                World w = e.getWorld();
                if (min_ticks == 0) {
                    min_ticks = w.random.nextInt(40);
                }
                if (ticks_since_last - 1 > min_ticks + w.random.nextInt(20)) {
                    float f = w.random.nextFloat();
                    ticks_since_last = 0;
                    return f > chance && super.preProcessEvent(data);
                }
                ticks_since_last++;
                return false;
            }
            return false;
        }
    }
    Identifier requiredItemID = new Identifier("minecraft", "air");
    public DPUEntityEvent(JsonElement o) {
        super(o, DPUEntityEvent::new);
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
