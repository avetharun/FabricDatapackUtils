package com.feintha.dpu.mixin.Client;

import com.feintha.dpu.alib;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.impl.client.rendering.WorldRenderContextImpl;
import net.fabricmc.fabric.mixin.client.rendering.EntityRenderersMixin;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@Mixin(net.minecraft.client.render.model.json.JsonUnbakedModel.class)
public class JsonUnbakedModelMixin {
    public boolean showHandLeft = false, showHandRight = false;
    @Mixin(JsonUnbakedModel.Deserializer.class)
    public static class JsonUnbakedModel_DeserializerMixin {
//        @Inject(locals = LocalCapture.CAPTURE_FAILHARD, at=@At("TAIL"), method="deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;", cancellable = true)
//        void deserializeMixin(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext, CallbackInfoReturnable<JsonUnbakedModel> cir, JsonObject l_jsonObject, List<ModelElement> l_modelElementList, String l_parent_json, Map<String, Either<SpriteIdentifier, String>> l_texturesFromJsonMap, Boolean l_hasAAO, ModelTransformation l_modelTransformation, List<ModelOverride> l_modelOverrides, JsonUnbakedModel.GuiLight l_guiLight, Identifier l_identifier ) {
//            var m =  new JsonUnbakedModel(l_identifier, l_modelElementList, l_texturesFromJsonMap, l_hasAAO, l_guiLight, l_modelTransformation, l_modelOverrides);
//            boolean showHandLeft = false;
//            boolean showHandRight = false;
//            if (l_jsonObject.has("hands")) {
//                var h = l_jsonObject.getAsJsonObject("hands");
//                if (h.has("right")) {
//                    showHandRight = h.get("right").getAsBoolean();
//                }
//                if (h.has("left")) {
//                    showHandLeft = h.get("left").getAsBoolean();
//                }
//            }
//            alib.setMixinField(m, "showHandLeft", showHandLeft);
//            alib.setMixinField(m, "showHandRight", showHandRight);
//
////            System.out.println(""+showHandLeft+showHandRight + l_identifier);
//        }
    }
//    @Inject(locals = LocalCapture.CAPTURE_FAILHARD, at=@At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"), method= "bake(Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/function/Function;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/model/BakedModel;")
//    void bakeMixin(Baker baker, JsonUnbakedModel parent, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier id, boolean hasDepth, CallbackInfoReturnable<BakedModel> cir, Sprite sprite, BasicBakedModel.Builder builder) {
//        if (this.showHandLeft) {
//            var l = alib.setMixinField(builder, "showHandLeft", true);
//        }
//        if (this.showHandRight) {
//            var r = alib.setMixinField(builder, "showHandRight", true);
//        }
//    }
    @Mixin(BasicBakedModel.class)
    public static class BasicBakedModelMixin{
        public boolean showHandLeft = false, showHandRight = false;
    }
    @Mixin(BasicBakedModel.Builder.class)
    public static class BasicBakedModelDeserializerMixin{
//        @Shadow @Final private List<BakedQuad> quads;
//        @Shadow @Final private Map<Direction, List<BakedQuad>> faceQuads;
//        @Shadow @Final private boolean usesAo;
//        @Shadow @Final private boolean isSideLit;
//        @Shadow @Final private boolean hasDepth;
//        @Shadow private Sprite particleTexture;
//        @Shadow @Final private ModelTransformation transformation;
//        @Shadow @Final private ModelOverrideList itemPropertyOverrides;
//        public boolean showHandLeft = false, showHandRight = false;
//        @Inject(method="build", at=@At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
//        void buildMixin(CallbackInfoReturnable<BakedModel> cir) {
//            var bm = new BasicBakedModel(quads, faceQuads, usesAo, isSideLit, hasDepth, particleTexture, transformation, itemPropertyOverrides);
//            var l = alib.setMixinField(bm, "showHandLeft", this.showHandLeft);
//            var r = alib.setMixinField(bm, "showHandRight", this.showHandRight);
////            System.out.println(""+l+r+"i:"+showHandLeft + showHandRight);
//            cir.setReturnValue(bm);
//        }
    }
}
