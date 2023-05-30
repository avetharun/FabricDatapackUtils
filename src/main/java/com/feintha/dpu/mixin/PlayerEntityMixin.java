package com.feintha.dpu.mixin;

import com.feintha.dpu.DPU;
import com.feintha.dpu.DPUEventType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method="tick",at=@At("TAIL"))
    void tickInject(CallbackInfo ci){
        PlayerEntity _this = (PlayerEntity)(Object)this;
        if (_this.handSwinging && _this.handSwingTicks == 0) {
            Identifier id = Registries.ITEM.getId(_this.getMainHandStack().getItem());
            if (_this.world.isClient) {
                DPU.InvokeClientEventFor(DPUEventType.ON_SWING_EVENT, id);
            } else {
                DPU.InvokeServerEventFor(DPUEventType.ON_SWING_EVENT, id, (ServerWorld) _this.world, _this);
            }
        }
    }
}
