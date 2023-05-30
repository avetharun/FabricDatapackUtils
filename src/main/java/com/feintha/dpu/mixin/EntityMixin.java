package com.feintha.dpu.mixin;

import com.feintha.dpu.DPU;
import com.feintha.dpu.DPUDataStorage;
import com.feintha.dpu.DPUEventType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method="interactAt", at=@At("TAIL"))
    void useAtInject(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        Entity e = (Entity) (Object)this;
        Identifier id = Registries.ENTITY_TYPE.getId(e.getType());
        System.out.println(id);
        if (player.world.isClient){
            DPU.InvokeClientEventForAt(DPUEventType.ON_INTERACT_ENTITY_EVENT, id, hitPos);
        } else {
            assert player.world.getServer() != null;
            NbtCompound compound = new NbtCompound();
            compound.putString("type", id.toString());
            compound.putString("name", e.getName().getString());
            compound.putUuid("uuid", e.getUuid());
            DPUDataStorage.PushEvent(player.world.getServer(), "entity_interact", compound);
            DPU.InvokeServerEventForAt(DPUEventType.ON_INTERACT_ENTITY_EVENT, id, hitPos, (ServerWorld)player.world, player);
        }
    }
    @Inject(method="handleAttack", at=@At("TAIL"))
    void attackInject(Entity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (!attacker.isPlayer()){return;}
        Entity e = (Entity) (Object)this;
        Identifier id = Registries.ENTITY_TYPE.getId(e.getType());
        System.out.println(id);
        if (attacker.world.isClient){
            DPU.InvokeClientEventFor(DPUEventType.ON_INTERACT_ENTITY_EVENT, id);
        } else {
            assert attacker.world.getServer() != null;
            NbtCompound compound = new NbtCompound();
            compound.putString("type", id.toString());
            compound.putString("name", e.getName().getString());
            compound.putUuid("uuid", e.getUuid());
            DPUDataStorage.PushEvent(attacker.world.getServer(), "entity_attack", compound);
            DPU.InvokeServerEventForAt(DPUEventType.ON_INTERACT_ENTITY_EVENT, id, e.getEyePos(), (ServerWorld)attacker.world, null);
        }
    }

}
