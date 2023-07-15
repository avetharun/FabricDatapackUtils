package com.feintha.dpu.mixin;

import com.feintha.dpu.DPU;
import com.feintha.dpu.DPUDataStorage;
import com.feintha.dpu.DPUEventType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method="onPlayerCollision", at=@At(value = "TAIL"))
    void onPickupItemFunction(PlayerEntity player, CallbackInfo ci){
        ItemEntity _this = (ItemEntity)(Object)this;
        Identifier id = Registries.ITEM.getId(_this.getStack().getItem());
        NbtCompound compound = new NbtCompound();
        World world = _this.getWorld();
        ItemUsageContext context = new ItemUsageContext(world, player, player.getActiveHand(), _this.getStack(), null);
        compound.putString("item", id.toString());
        if (world.isClient) {
            DPU.InvokeClientEventFor(DPUEventType.ON_ITEM_PICKUP_EVENT, id, context);
        } else {
            assert world.getServer() != null;
            DPUDataStorage.PushEvent(world.getServer(), "item_pickup", compound);
            DPU.InvokeServerEventFor(DPUEventType.ON_ITEM_PICKUP_EVENT, id, (ServerWorld) world, player, true, context);
        }
    }
}
