package com.feintha.dpu.mixin.Client;

import com.feintha.dpu.client.BooleanModelOverride;
import com.feintha.dpu.client.DPUClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.feintha.dpu.client.DPUClient.BOOLEAN_MODEL_OVERRIDE;

@Mixin(ModelPredicateProviderRegistry.class)
public class ModelPredicateProviderRegistryMixin {

}
