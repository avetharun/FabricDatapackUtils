package com.feintha.dpu.mixin;

import com.feintha.dpu.IdentifierProperty;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Property.Value.class)
public class PropertyValueMixin <T extends Comparable<T>>{
    @Mutable
    @Shadow @Final private Property<T> property;
    @ModifyExpressionValue(method = "<init>", at=@At(value="INVOKE", target = "Ljava/util/Collection;contains(Ljava/lang/Object;)Z"))
    private boolean allowAny(boolean original, @Local T value){
        if (value instanceof IdentifierProperty) {
            return false;
        }
        return original;
    }
}
