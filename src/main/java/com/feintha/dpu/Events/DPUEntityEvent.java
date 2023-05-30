package com.feintha.dpu.Events;

import com.feintha.dpu.DPUEvent;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

public class DPUEntityEvent extends DPUEvent {
    Identifier requiredItemID = new Identifier("minecraft", "air");
    public DPUEntityEvent(JsonObject o) {
        super(o);
    }

    @Override
    public DPUEvent Deserialize(JsonObject object) {
        if (object.has("requiredItem")) {
            this.requiredItemID = new Identifier(object.get("requiredItem").getAsString());
        }
        return super.Deserialize(object);
    }
}
