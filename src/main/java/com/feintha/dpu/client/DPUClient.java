package com.feintha.dpu.client;

import com.feintha.dpu.DPUEventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DPUClient {
    public static final RegistryKey<Registry<BooleanModelOverride>> BOOLEAN_OVERRIDE_TYPE_KEYS = RegistryKey.ofRegistry(new Identifier("dpu", "c_boolean_model_overrides"));
    public static final Registry<BooleanModelOverride> BOOLEAN_MODEL_OVERRIDE = FabricRegistryBuilder.createSimple(BOOLEAN_OVERRIDE_TYPE_KEYS).buildAndRegister();
}
