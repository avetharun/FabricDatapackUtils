package com.feintha.dpu.mixin;

import com.feintha.dpu.DatapackUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.registry.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(CampfireBlock.class)
public class CampfireMixin {
    @ModifyReturnValue(method="isSignalFireBaseBlock", at=@At("RETURN"))
    private boolean isSignalFireBaseBlockOverriden(boolean original, BlockState state){
        return state.isIn(DatapackUtils.CAMPFIRE_SIGNAL_BLOCKS);
    }
}
