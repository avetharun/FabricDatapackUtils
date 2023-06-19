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
    @Inject(method="use", at=@At("TAIL"), cancellable = true)
    private void onUseFunction(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack self_S = ((ItemStack)(Object)this);
        Item _this = self_S.getItem();
        Identifier id = Registries.ITEM.getId(_this);
        boolean cancel = false;
        if (world.isClient) {
            cancel = DPU.InvokeClientEventFor(DPUEventType.ON_USE_EVENT, id, self_S);
        } else {
            NbtCompound compound = new NbtCompound();
            compound.putString("item", id.toString());
            assert world.getServer() != null;
            DPUDataStorage.PushEvent(world.getServer(), "item_use", compound);
            cancel = DPU.InvokeServerEventFor(DPUEventType.ON_USE_EVENT, id, (ServerWorld) world, user, true, self_S);
        }
        if (cancel) {
            cir.setReturnValue(TypedActionResult.consume(self_S));
            cir.cancel();
        }
    }
    @Mixin(Item.class)
    public static class ItemMixin{
    }
}
