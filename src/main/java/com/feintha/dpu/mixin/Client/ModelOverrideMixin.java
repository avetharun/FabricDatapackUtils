package com.feintha.dpu.mixin.Client;

import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.render.model.json.ModelOverrideList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ModelOverrideList.class)
public class ModelOverrideMixin {
    @Inject(method="<init>(Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/List;)V", at=@At("TAIL"))
    void init(Baker baker, JsonUnbakedModel parent, List<ModelOverride> overrides, CallbackInfo ci) {
//        if (overrides.size() > 0) {
//            var conds = overrides.get(0).streamConditions();
//            if (conds.findFirst().isEmpty()) {return;}
//            System.out.println(conds.findFirst().get().getType());
//        }

    }
}
