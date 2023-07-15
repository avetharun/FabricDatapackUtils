package com.feintha.dpu;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.NbtCompound;

public class DPUPredicate {
    DPUPredicate(JsonObject e){
        root = alib.json2NBT(e);
    }
    public final NbtCompound root;
}
