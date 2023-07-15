package com.feintha.dpu.mixin.Client;


import com.feintha.dpu.alib;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Inject(method="drawSlot", at=@At("HEAD"))
    void drawSlotBegin(DrawContext context, Slot slot, CallbackInfo ci){
        ItemStack itemStack = slot.getStack();
        alib.setMixinField(itemStack, "isBeingRenderedInGUICompat", true);
    }
}
