package com.feintha.dpu.mixin;
import com.feintha.dpu.*;
import com.feintha.dpu.client.DatapackUtilsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    public ModelTransformationMode transformationMode;
    public boolean isBeingRenderedInHotbar = false;
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onInit(CallbackInfo info) {
        transformationMode = ModelTransformationMode.NONE;
    }
    @Inject(method="use", at=@At("TAIL"))
    private void onUseFunction(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        Item _this = ((ItemStack)(Object)this).getItem();
        Identifier id = Registries.ITEM.getId(_this);
        if (world.isClient) {
            DPU.InvokeClientEventFor(DPUEventType.ON_USE_EVENT, id);
        } else {
            NbtCompound compound = new NbtCompound();
            compound.putString("item", id.toString());
            assert world.getServer() != null;
            DPUDataStorage.PushEvent(world.getServer(), "item_use", compound);
            DPU.InvokeServerEventFor(DPUEventType.ON_USE_EVENT, id, (ServerWorld) world, (PlayerEntity)user);
        }
    }
    @Mixin(Item.class)
    public static class ItemMixin{
    }
}
