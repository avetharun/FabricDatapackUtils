package com.feintha.dpu.Events;

import com.feintha.dpu.DPUDataStorage;
import com.feintha.dpu.DPUEvent;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class DPUPlayerEvent extends DPUEvent {
    UUID owner;
    String owner_name;
    float owner_health;
    int owner_hunger;
    int owner_armor;
    public NbtCompound PutPlayerData(PlayerEntity owner) {
        this.owner = owner.getUuid();
        this.owner_name = owner.getName().getString();
        this.owner_health = owner.getHealth();
        this.owner_hunger = owner.getHungerManager().getFoodLevel();
        this.owner_armor = owner.getArmor();

        NbtCompound res = new NbtCompound();
        res.putString("owner_uuid", this.owner.toString());
        res.putString("owner_name", this.owner_name);
        res.putFloat("owner_health", this.owner_health);
        res.putInt("owner_hunger", this.owner_hunger);
        res.putInt("owner_armor", this.owner_armor);
        return res;
    }
    public DPUPlayerEvent(JsonObject o) {
        super(o);

    }
}
