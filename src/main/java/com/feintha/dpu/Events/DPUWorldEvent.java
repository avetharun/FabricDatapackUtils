package com.feintha.dpu.Events;

import com.feintha.dpu.DPUEvent;
import com.feintha.dpu.alib;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DPUWorldEvent extends DPUEvent {

    public DPUWorldEvent(JsonObject o) {
        super(o);
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
