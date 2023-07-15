package com.feintha.dpu.Events;

import com.feintha.dpu.DPUEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class DPUInventoryEvent extends DPUEvent {
    public DPUInventoryEvent(JsonElement o) {
        super(o, DPUInventoryEvent::new);
    }
    int slot;
    List<String> screenRequirement = new ArrayList<>();
    @Override
    public DPUEvent Deserialize(JsonObject object) {
        slot = JsonHelper.getInt(object, "slot");
        boolean bl1, bl2 = false;
        if (JsonHelper.hasArray(object, "screens")) {
            var v = JsonHelper.getArray(object, "screens").asList();
            for(JsonElement e : v) {
                screenRequirement.add(e.getAsString());
            }
        } else if (JsonHelper.hasString(object, "screen")) {
            screenRequirement.add(JsonHelper.getString(object, "screen"));
        }

        return super.Deserialize(object);
    }
}
