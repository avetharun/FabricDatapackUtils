package com.feintha.dpu.Events;

import com.feintha.dpu.DPUDataStorage;
import com.feintha.dpu.DPUEvent;
import com.feintha.dpu.alib;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DPUPlayerEvent extends DPUEvent {

    public static class DPUPlayerRandomEvent extends DPUWorldEvent{
        public DPUPlayerRandomEvent(JsonElement o) {
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
            if (data instanceof PlayerEntity p) {
                World w = p.getWorld();
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
    public DPUPlayerEvent(JsonElement o) {
        super(o, DPUPlayerEvent::new);

    }
}
