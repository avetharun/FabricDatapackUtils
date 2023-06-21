package com.feintha.dpu.mixin;

import com.feintha.dpu.*;
import com.feintha.dpu.Events.DPUPlayerEvent;
import com.feintha.dpu.client.DatapackUtilsClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    int lastJumpTick = 0;
    @Inject(method="jump", at=@At("TAIL"))
    public void setJumping(CallbackInfo ci) {
        PlayerEntity _this = (PlayerEntity)(Object)this;
        if (_this.getWorld().isClient) {
            var evs = DPU.getAllClientEventsFor(DPUEventType.ON_JUMP);
            for (DPUEvent ev : evs) {
                if (ev instanceof DPUPlayerEvent playerEvent) {
                    playerEvent.PutEntityData(_this);
                }
                ev.doActionClientAt(_this.getPos(), _this);
            }
            lastJumpTick = DatapackUtilsClient.worldTick;
        } else {
            var evs = DPU.getAllServerEventsFor(DPUEventType.ON_JUMP);
            for (DPUEvent ev : evs) {
                if (ev instanceof DPUPlayerEvent playerEvent) {
                    playerEvent.PutEntityData(_this);
                }
                ev.doActionServerAt((ServerWorld) _this.getWorld(), _this, _this.getPos(), _this);
            }
            lastJumpTick = _this.getWorld().getServer().getTicks();
        }
    }
    void exec(PlayerEntity _this, DPUEventType type) {
        if (_this.getWorld().isClient) {
            var evs = DPU.getAllClientEventsFor(type);
            for (DPUEvent ev : evs) {
                if (ev instanceof DPUPlayerEvent playerEvent) {
                    playerEvent.PutEntityData(_this);
                }
                ev.doActionClientAt(_this.getPos(), _this);
            }
        } else {
            var evs = DPU.getAllServerEventsFor(type);
            for (DPUEvent ev : evs) {
                if (ev instanceof DPUPlayerEvent playerEvent) {
                    playerEvent.PutEntityData(_this);
                }
                ev.doActionServerAt((ServerWorld) _this.getWorld(), _this, _this.getPos(), _this);
            }
        }
    }
    void exec(PlayerEntity _this, DPUEventType type, Identifier id, boolean immediate) {
        if (_this.getWorld().isClient) {
            DPU.InvokeClientEventFor(type, id);
        } else {
            DPU.InvokeServerEventFor(type, id, (ServerWorld) _this.getWorld(), _this, immediate, _this);
        }
    }
    void exec(PlayerEntity _this, DPUEventType type, Identifier id, Vec3d pos) {
        if (_this.getWorld().isClient) {
            DPU.InvokeClientEventForAt(type, id, pos);
        } else {
            DPU.InvokeServerEventForAt(type, id, pos, (ServerWorld) _this.getWorld(), _this, _this);
        }
    }

    public void emitGameEvent(GameEvent event) {
        PlayerEntity _this = (PlayerEntity) (Object) this;
        _this.getWorld().emitGameEvent(_this, event, _this.getPos());
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
            if (_this.getWorld().isClient) {
                DPU.InvokeClientEventFor(DPUEventType.ON_SWING_EVENT, id, _this);
            } else {
                DPU.InvokeServerEventFor(DPUEventType.ON_SWING_EVENT, id, (ServerWorld) _this.getWorld(), _this, false, _this);
            }
        }
        sneakLast = _this.isSneaking();
    }
    @Inject(method="eatFood", at=@At("TAIL"))
    void eatFoodMixin(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        PlayerEntity _this = (PlayerEntity) (Object) this;
        exec(_this, DPUEventType.ON_EAT, Registries.ITEM.getId(stack.getItem()), true);

    }
}
