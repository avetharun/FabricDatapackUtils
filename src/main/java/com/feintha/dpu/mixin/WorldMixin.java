package com.feintha.dpu.mixin;


import com.feintha.dpu.DPU;
import com.feintha.dpu.DPUEventType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.command.DataCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(World.class)
public class WorldMixin {
    @Mixin(ServerWorld.class)
    public static abstract class ServerWorldMixin {
        @Shadow @Final private ServerWorldProperties worldProperties;

        boolean rainingLast = false;
        boolean thunderingLast = false;
        @Inject(method="tick", at=@At("TAIL"))
        void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
            ServerWorld w = (ServerWorld) (Object)this;
            DPU.InvokeServerEventFor(DPUEventType.RANDOM_WORLD_TICK, w.getDimensionKey().getValue(), w, null, true, w);
        }
        @Inject(method="tickWeather", at=@At("TAIL"))
        void onTickWeather(CallbackInfo ci){
            boolean rC = worldProperties.isRaining();
            boolean tC = worldProperties.isThundering();
            ServerWorld w = (ServerWorld) (Object)this;
            if (rC && !rainingLast) {
                DPU.InvokeServerEventFor(DPUEventType.ON_WORLD_BEGIN_RAINING,  w.getDimensionKey().getValue(), w, null, true, w);
                rainingLast = rC;
                thunderingLast = tC;
                return;
            } else if (!rC && rainingLast) {
                DPU.InvokeServerEventFor(DPUEventType.ON_WORLD_END_RAINING,  w.getDimensionKey().getValue(), w, null, true, w);
                rainingLast = rC;
                thunderingLast = tC;
                return;
            }
            if (tC && !thunderingLast) {
                DPU.InvokeServerEventFor(DPUEventType.ON_WORLD_BEGIN_THUNDER,  w.getDimensionKey().getValue(), w, null, true, w);

                rainingLast = rC;
                thunderingLast = tC;
                return;
            } else if (!tC && thunderingLast) {
                DPU.InvokeServerEventFor(DPUEventType.ON_WORLD_END_THUNDER,  w.getDimensionKey().getValue(), w, null, true, w);

                rainingLast = rC;
                thunderingLast = tC;
                return;
            }
        }
    }
    @Mixin(ClientWorld.class)
    public static class ClientWorldMixin {
        @Shadow @Final private ClientWorld.Properties clientWorldProperties;
        boolean rainingLast = false;
        boolean thunderingLast = false;
        @Inject(method="tickTime", at=@At("TAIL"))
        void tickTimeMixin(CallbackInfo ci){
            boolean rC = clientWorldProperties.isRaining();
            boolean tC = clientWorldProperties.isThundering();
            World w = (World)(Object)this;
            if (rC && !rainingLast) {
                DPU.InvokeClientEventFor(DPUEventType.ON_WORLD_BEGIN_RAINING, w.getDimensionKey().getValue(), w);
            } else if (!rC && rainingLast) {
                DPU.InvokeClientEventFor(DPUEventType.ON_WORLD_END_RAINING, w.getDimensionKey().getValue(), w);
            }
            if (tC && !thunderingLast) {
                DPU.InvokeClientEventFor(DPUEventType.ON_WORLD_BEGIN_THUNDER,  w.getDimensionKey().getValue(),w);
            } else if (!tC && thunderingLast) {
                DPU.InvokeClientEventFor(DPUEventType.ON_WORLD_END_THUNDER,  w.getDimensionKey().getValue(),w);
            }
            rainingLast = rC;
            thunderingLast = tC;

        }
        @Inject(method="tick", at=@At("TAIL"))
        void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
            World w = (World)(Object)this;
            DPU.InvokeClientEventFor(DPUEventType.RANDOM_WORLD_TICK, w.getDimensionKey().getValue(), w);
        }
    }
}
