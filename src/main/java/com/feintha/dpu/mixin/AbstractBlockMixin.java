package com.feintha.dpu.mixin;

import com.feintha.dpu.DPU;
import com.feintha.dpu.DPUDataStorage;
import com.feintha.dpu.DPUEventType;
import com.feintha.dpu.alib;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @Inject(method="onUse", at=@At("TAIL"))
    private void onUseMixin(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir){
        Identifier id = Registries.BLOCK.getId(state.getBlock());
        if (world.isClient) {
            DPU.InvokeClientEventForAt(DPUEventType.ON_INTERACT_BLOCK_EVENT, id, hit.getPos());
        } else {

            NbtCompound compound = new NbtCompound();
            compound.putString("type", id.toString());
            compound.putLongArray("blockpos", alib.getBlockPosAsArray(pos));
            compound.putString("side", hit.getSide().getName());
            compound.putString("item", Registries.ITEM.getId(player.getStackInHand(hand).getItem()).toString());
            assert world.getServer() != null;
            DPUDataStorage.PushEvent(world.getServer(), "block_use", compound);
            DPU.InvokeServerEventForAt(DPUEventType.ON_INTERACT_BLOCK_EVENT, id, hit.getPos(), (ServerWorld)world, player);
        }
    }
    @Inject(method="onStateReplaced", at=@At("TAIL"))
    void blockBrokenMixin(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        // Assume this means the block was broken.
        if (newState.isAir()) {
            Identifier id = Registries.BLOCK.getId(state.getBlock());
            if (world.isClient) {
                DPU.InvokeClientEventForAt(DPUEventType.ON_INTERACT_BLOCK_EVENT, id, pos.toCenterPos());
            } else {
                NbtCompound compound = new NbtCompound();
                compound.putString("type", id.toString());
                compound.putLongArray("blockpos", alib.getBlockPosAsArray(pos));
                assert world.getServer() != null;
                DPUDataStorage.PushEvent(world.getServer(), "block_broken", compound);
                DPU.InvokeServerEventForAt(DPUEventType.ON_INTERACT_BLOCK_EVENT, id, pos.toCenterPos(), (ServerWorld)world, null);
            }
        }
    }
}
