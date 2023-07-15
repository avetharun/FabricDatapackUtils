package com.feintha.dpu.Events;

import com.feintha.dpu.DPUEvent;
import com.feintha.dpu.alib;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class DPUWorldEvent extends DPUEvent {
    public static class DPUWorldRandomEvent extends DPUWorldEvent{
        public DPUWorldRandomEvent(JsonElement o) {
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
            if (data instanceof World w) {
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
    public DPUWorldEvent(JsonElement o) {
        super(o, DPUWorldEvent::new);
    }

    @Override
    public <T> boolean preProcessEvent(T data) {
        if (this.requiredNbt != null && requiredNbt.getSize() != 0) {
            if (data instanceof World w) {
                NbtCompound c = new NbtCompound();
                c.putLong("time", w.getTime());
                c.putBoolean("is_day", w.isDay());
                c.putBoolean("is_night", w.isNight());
                c.putBoolean("has_rain", w.isRaining());
                c.putBoolean("hasThunder", w.isThundering());
                c.putInt("players_in_world", w.getPlayers().size());
                c.putBoolean("client", w.isClient());
                c.putString("difficulty", w.getLevelProperties().getDifficulty().asString());
                c.putBoolean("hardcore", w.getLevelProperties().isHardcore());
                c.putBoolean("can_sleep", w.getDimension().bedWorks());
                c.putBoolean("can_spawn_raids", w.getDimension().hasRaids());
                c.putInt("moon_phase", w.getDimension().getMoonPhase(w.getTime()));
                return alib.checkNBTEquals(requiredNbt, c) && super.preProcessEvent(data);
            }
        }
        return super.preProcessEvent(data);
    }

    @Override
    public DPUEvent Deserialize(JsonObject object) {
        return super.Deserialize(object);
    }
}
