package com.feintha.dpu.Events;

import com.feintha.dpu.DPUDataStorage;
import com.feintha.dpu.DPUEvent;
import com.feintha.dpu.alib;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DPUPlayerEvent extends DPUEvent {
    UUID owner;
    String owner_name;
    float owner_health;
    int owner_hunger;
    int owner_armor;

    @Override
    public void putEventData(@Nullable Entity owner, World world, MinecraftServer server, Identifier id) {
        if (owner != null) {
            DPUDataStorage.PushEvent(server, id.getPath(), PutEntityData(owner));
        }
        super.putEventData(owner, world, server, id);
    }

    @Override
    public <T> boolean preProcessEvent(T data) {
        if (this.requiredNbt != null && requiredNbt.getSize() != 0) {
            if (data instanceof PlayerEntity e) {
                NbtCompound c = e.writeNbt(new NbtCompound());
                return alib.checkNBTEquals(requiredNbt, c) && super.preProcessEvent(data);
            }
        }
        return super.preProcessEvent(data);
    }

    public NbtCompound PutEntityData(Entity owner) {
        NbtCompound res = new NbtCompound();
        this.owner = owner.getUuid();
        this.owner_name = owner.getName().getString();
        if (owner instanceof LivingEntity e) {
            this.owner_health = e.getHealth();
            res.putFloat("owner_health", this.owner_health);
            if (e instanceof PlayerEntity p) {
                this.owner_hunger = p.getHungerManager().getFoodLevel();
                res.putInt("owner_hunger", this.owner_hunger);
            }
            this.owner_armor = e.getArmor();
            res.putInt("owner_armor", this.owner_armor);
        }

        res.putString("owner_uuid", this.owner.toString());
        res.putString("owner_name", this.owner_name);
        res.putString("id", Registries.ENTITY_TYPE.getId(owner.getType()).toString());
        return res;
    }
    public DPUPlayerEvent(JsonObject o) {
        super(o);

    }
}
