package com.feintha.dpu.mixin;

import com.feintha.dpu.*;
import com.feintha.dpu.Events.DPUPlayerEvent;
import com.feintha.dpu.client.DatapackUtilsClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    int lastJumpTick = 0;
    @Inject(method="jump", at=@At("TAIL"))
    public void setJumping(CallbackInfo ci) {
        PlayerEntity _this = (PlayerEntity)(Object)this;
        if (_this.world.isClient) {
            var evs = DPU.getAllClientEventsFor(DPUEventType.ON_JUMP);
            for (DPUEvent ev : evs) {
                if (ev instanceof DPUPlayerEvent playerEvent) {
                    playerEvent.PutPlayerData(_this);
                }
                ev.doActionClientAt(_this.getPos());
            }
            lastJumpTick = DatapackUtilsClient.worldTick;
        } else {
            var evs = DPU.getAllServerEventsFor(DPUEventType.ON_JUMP);
            System.out.println(evs.size());
            for (DPUEvent ev : evs) {
                if (ev instanceof DPUPlayerEvent playerEvent) {
                    playerEvent.PutPlayerData(_this);
                }
                ev.doActionServerAt((ServerWorld) _this.world, _this, _this.getPos());
            }
            lastJumpTick = _this.world.getServer().getTicks();
        }
    }
    void exec(PlayerEntity _this, DPUEventType type) {
        if (_this.world.isClient) {
            var evs = DPU.getAllClientEventsFor(type);
            for (DPUEvent ev : evs) {
                if (ev instanceof DPUPlayerEvent playerEvent) {
                    playerEvent.PutPlayerData(_this);
                }
                ev.doActionClientAt(_this.getPos());
            }
        } else {
            var evs = DPU.getAllServerEventsFor(type);
            for (DPUEvent ev : evs) {
                if (ev instanceof DPUPlayerEvent playerEvent) {
                    playerEvent.PutPlayerData(_this);
                }
                ev.doActionServerAt((ServerWorld) _this.world, _this, _this.getPos());
            }
        }
    }

    public void emitGameEvent(GameEvent event) {
        PlayerEntity _this = (PlayerEntity) (Object) this;
        if (event == GameEvent.EAT) {
            exec(_this, DPUEventType.ON_EAT);
        }
        _this.world.emitGameEvent(_this, event, _this.getPos());
    }
    boolean sneakLast = false;
    boolean shieldLast = false;
    @Inject(method="tick",at=@At("TAIL"))
    void tickInject(CallbackInfo ci) {
        PlayerEntity _this = (PlayerEntity) (Object) this;
//        if (_this.isBlocking()) {
//            if (!shieldLast) {
//                exec(_this, DPUEventType.ON_SHIELD_RAISE);
//            } else {
//                exec(_this, DPUEventType.WHILE_SHIELD_RAISED);
//            }
//        } else if (shieldLast) {
//            exec(_this, DPUEventType.ON_SHIELD_LOWER);
//        }
        if (_this.isSneaking()) {
            if (!sneakLast) {
                exec(_this, DPUEventType.ON_SNEAK);
            } else {
                exec(_this, DPUEventType.WHILE_SNEAK);
            }
        } else if (sneakLast) {
            exec(_this, DPUEventType.ON_SNEAK_END);
        }
        if (_this.handSwinging && _this.handSwingTicks == 0) {
            Identifier id = Registries.ITEM.getId(_this.getMainHandStack().getItem());
            if (_this.world.isClient) {
                DPU.InvokeClientEventFor(DPUEventType.ON_SWING_EVENT, id);
            } else {
                DPU.InvokeServerEventFor(DPUEventType.ON_SWING_EVENT, id, (ServerWorld) _this.world, _this);
            }
        }
        sneakLast = _this.isSneaking();
    }
}
