package com.feintha.dpu.mixin.Client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.server.command.ExecuteCommand;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.client.render.model.json.ModelOverride$Deserializer")
public class ModelOverrideDeserializerMixin {
    @Inject(method="deserializeMinPropertyValues", at=@At("HEAD"), cancellable = true)
    private void onDeserializeMinPropertyValues(JsonObject object, CallbackInfoReturnable<List<ModelOverride.Condition>> cir) {
        Map<Identifier, Float> map = Maps.newLinkedHashMap();
        JsonObject jsonObject = JsonHelper.getObject(object, "predicate");
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
            if (JsonHelper.isBoolean(stringJsonElementEntry.getValue())) {
                boolean s = JsonHelper.asBoolean(stringJsonElementEntry.getValue(), stringJsonElementEntry.getKey());
                float v = s ? 0.07991f : 0;
                map.put(new Identifier(stringJsonElementEntry.getKey()), v);
                continue;
            }
            map.put(new Identifier(stringJsonElementEntry.getKey()), JsonHelper.asFloat(stringJsonElementEntry.getValue(), stringJsonElementEntry.getKey()));
        }
        cir.setReturnValue (map.entrySet().stream().map((entry) -> new ModelOverride.Condition(entry.getKey(), entry.getValue())).collect(ImmutableList.toImmutableList()));
    }
}
