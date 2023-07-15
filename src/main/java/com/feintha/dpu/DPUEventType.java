package com.feintha.dpu;

import com.feintha.dpu.Events.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class DPUEventType {
    public static final DPUEventType ON_WORLD_BEGIN_RAINING = Registry.register(DPU.EVENT_TYPE, new Identifier("on_rain_begin"), new DPUEventType(DPUWorldEvent::new));
    public static final DPUEventType ON_WORLD_END_RAINING = Registry.register(DPU.EVENT_TYPE, new Identifier("on_rain_end"), new DPUEventType(DPUWorldEvent::new));
    public static final DPUEventType ON_WORLD_BEGIN_THUNDER = Registry.register(DPU.EVENT_TYPE, new Identifier("on_thunder_begin"), new DPUEventType(DPUWorldEvent::new));
    public static final DPUEventType ON_WORLD_END_THUNDER = Registry.register(DPU.EVENT_TYPE, new Identifier("on_thunder_end"), new DPUEventType(DPUWorldEvent::new));
    public static final DPUEventType RANDOM_WORLD_TICK = Registry.register(DPU.EVENT_TYPE, new Identifier("random_world_tick"), new DPUEventType(DPUWorldEvent.DPUWorldRandomEvent::new));
    public static final DPUEventType RANDOM_ENTITY_TICK = Registry.register(DPU.EVENT_TYPE, new Identifier("random_entity_tick"), new DPUEventType(DPUEntityEvent.DPUEntityRandomEvent::new));
    public static final DPUEventType ON_USE_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_use"), new DPUEventType(DPUItemEvent::new));
    public static final DPUEventType ON_ITEM_PICKUP_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_item_pickup"), new DPUEventType(DPUItemEvent::new));
    public static final DPUEventType ON_INTERACT_BLOCK_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_interact_block"), new DPUEventType(DPUBlockEvent::new));
    public static final DPUEventType ON_SWING_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_swing"), new DPUEventType(DPUItemEvent::new));
    public static final DPUEventType ON_ATTACK_ENTITY_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_attack_entity"), new DPUEventType(DPUEntityEvent::new));
    public static final DPUEventType ON_INTERACT_ENTITY_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_interact_entity"), new DPUEventType(DPUEntityEvent::new));
    public static final DPUEventType WHILE_SNEAK = Registry.register(DPU.EVENT_TYPE, new Identifier("while_sneak"), new DPUEventType(DPUPlayerEvent::new));
    public static final DPUEventType ON_SNEAK = Registry.register(DPU.EVENT_TYPE, new Identifier("on_sneak"), new DPUEventType(DPUPlayerEvent::new));
    public static final DPUEventType ON_SNEAK_END = Registry.register(DPU.EVENT_TYPE, new Identifier("on_sneak_end"), new DPUEventType(DPUPlayerEvent::new));
    public static final DPUEventType ON_JUMP = Registry.register(DPU.EVENT_TYPE, new Identifier("on_jump"), new DPUEventType(DPUPlayerEvent::new));
    public static final DPUEventType ON_EAT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_eat"), new DPUEventType(DPUPlayerEvent::new));
    public static final DPUEventType ON_ENTITY_COLLIDE_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_collide_entity"), new DPUEventType(DPUEntityEvent::new));



//    public static final DPUEventType ON_SHIELD_RAISE = Registry.register(DPU.EVENT_TYPE, new Identifier("on_shield_raise"), new DPUEventType(DPUPlayerEvent::new));
//    public static final DPUEventType WHILE_SHIELD_RAISED = Registry.register(DPU.EVENT_TYPE, new Identifier("while_shield_raised"), new DPUEventType(DPUPlayerEvent::new));
//    public static final DPUEventType ON_SHIELD_LOWER = Registry.register(DPU.EVENT_TYPE, new Identifier("on_shield_lower"), new DPUEventType(DPUPlayerEvent::new));
    // Code to force Minecraft to load the registries here. Jank, I know, but it works.
    public static void _MINIT() {
        assert ON_USE_EVENT != null;
    }
    Function<JsonElement, ? extends DPUEvent> elementConsumer;
    DPUEventType(Function<JsonElement, ? extends DPUEvent> eventCtor) {
        elementConsumer = eventCtor;
    }
    public HashMap<Identifier, DPUEvent> Events = new HashMap<>();
    public DPUEvent getSubEvent(Identifier eventName) {
        return Events.getOrDefault(eventName, null);
    }
    public <T extends DPUEvent> T createEventType(JsonElement arg){
        // noinspection unchecked
        return (T) elementConsumer.apply(arg);
    }
    public void addSubEvent (Identifier eventName, DPUEvent event){
        Events.put(eventName, event);
    }
}
