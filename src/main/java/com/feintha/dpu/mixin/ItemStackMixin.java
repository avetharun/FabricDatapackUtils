package com.feintha.dpu.mixin;
import com.feintha.dpu.*;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    public ModelTransformationMode transformationMode;
    public boolean isBeingRenderedInHotbar = false, isBeingRenderedInGUICompat = false;
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onInit(CallbackInfo info) {
        transformationMode = ModelTransformationMode.NONE;
    }
    @Inject(method="useOnBlock", at=@At("TAIL"))
    void onUseOnBlockFunction(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
        ItemStack self_S = ((ItemStack)(Object)this);
        Item _this = self_S.getItem();
        Identifier id = Registries.ITEM.getId(_this);
        World world = context.getWorld();
        PlayerEntity user = context.getPlayer();
        boolean cancel = false;
        if (world.isClient) {
            cancel = DPU.InvokeClientEventFor(DPUEventType.ON_USE_EVENT, id, context);
        } else {
            NbtCompound compound = new NbtCompound();
            compound.putString("item", id.toString());
            assert world.getServer() != null;
            DPUDataStorage.PushEvent(world.getServer(), "item_use", compound);
            assert user != null;
            cancel = DPU.InvokeServerEventFor(DPUEventType.ON_USE_EVENT, id, (ServerWorld) world, user, true, context);
        }
    }
    @Inject(method="useOnEntity", at=@At("TAIL"))
    void onUseOnEntityFunction(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

        ItemStack self_S = ((ItemStack)(Object)this);
        Item _this = self_S.getItem();
        Identifier id = Registries.ITEM.getId(_this);
        World world = user.getWorld();
        ItemUsageContext context = new ItemUsageContext(user, user.getActiveHand(), null);
        boolean cancel = false;
        if (world.isClient) {
            cancel = DPU.InvokeClientEventFor(DPUEventType.ON_USE_EVENT, id, context);
        } else {
            NbtCompound compound = new NbtCompound();
            compound.putString("item", id.toString());
            assert world.getServer() != null;
            DPUDataStorage.PushEvent(world.getServer(), "item_use", compound);
            cancel = DPU.InvokeServerEventFor(DPUEventType.ON_USE_EVENT, id, (ServerWorld) world, user, true, context);
        }
    }
    @Inject(method="use", at=@At("TAIL"))
    private void onUseFunction(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack self_S = ((ItemStack)(Object)this);
        Item _this = self_S.getItem();
        Identifier id = Registries.ITEM.getId(_this);
        ItemUsageContext context = new ItemUsageContext(user, user.getActiveHand(), null);
        boolean cancel = false;
        if (world.isClient) {
            cancel = DPU.InvokeClientEventFor(DPUEventType.ON_USE_EVENT, id, context);
        } else {
            NbtCompound compound = new NbtCompound();
            compound.putString("item", id.toString());
            assert world.getServer() != null;
            DPUDataStorage.PushEvent(world.getServer(), "item_use", compound);
            cancel = DPU.InvokeServerEventFor(DPUEventType.ON_USE_EVENT, id, (ServerWorld) world, user, true, context);
        }
    }

}
