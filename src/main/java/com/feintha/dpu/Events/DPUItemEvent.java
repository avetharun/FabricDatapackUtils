package com.feintha.dpu.Events;

import com.feintha.dpu.DPUEvent;
import com.google.gson.JsonObject;

public class DPUItemEvent extends DPUEvent {
    public DPUItemEvent(JsonObject o) {
        super(o);
    }
}
