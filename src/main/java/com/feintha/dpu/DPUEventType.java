package com.feintha.dpu;

import com.feintha.dpu.Events.DPUBlockEvent;
import com.feintha.dpu.Events.DPUEntityEvent;
import com.feintha.dpu.Events.DPUItemEvent;
import com.feintha.dpu.Events.DPUPlayerEvent;
import com.google.gson.JsonObject;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class DPUEventType {
    public static final DPUEventType ON_USE_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_use"), new DPUEventType(DPUItemEvent.class));
    public static final DPUEventType ON_INTERACT_BLOCK_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_interact_block"), new DPUEventType(DPUBlockEvent.class));
    public static final DPUEventType ON_SWING_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_swing"), new DPUEventType(DPUItemEvent.class));
    public static final DPUEventType ON_ATTACK_ENTITY_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_attack_entity"), new DPUEventType(DPUEntityEvent.class));
    public static final DPUEventType ON_INTERACT_ENTITY_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_interact_entity"), new DPUEventType(DPUEntityEvent.class));
    public static final DPUEventType WHILE_SNEAK = Registry.register(DPU.EVENT_TYPE, new Identifier("while_sneak"), new DPUEventType(DPUPlayerEvent.class));
    public static final DPUEventType ON_SNEAK = Registry.register(DPU.EVENT_TYPE, new Identifier("on_sneak"), new DPUEventType(DPUPlayerEvent.class));
    public static final DPUEventType ON_SNEAK_END = Registry.register(DPU.EVENT_TYPE, new Identifier("on_sneak_end"), new DPUEventType(DPUPlayerEvent.class));
    public static final DPUEventType ON_JUMP = Registry.register(DPU.EVENT_TYPE, new Identifier("on_jump"), new DPUEventType(DPUPlayerEvent.class));
    public static final DPUEventType ON_EAT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_eat"), new DPUEventType(DPUPlayerEvent.class));
    public static final DPUEventType ON_ENTITY_COLLIDE_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("on_collide_entity"), new DPUEventType(DPUEntityEvent.class));



//    public static final DPUEventType ON_SHIELD_RAISE = Registry.register(DPU.EVENT_TYPE, new Identifier("on_shield_raise"), new DPUEventType(DPUPlayerEvent.class));
//    public static final DPUEventType WHILE_SHIELD_RAISED = Registry.register(DPU.EVENT_TYPE, new Identifier("while_shield_raised"), new DPUEventType(DPUPlayerEvent.class));
//    public static final DPUEventType ON_SHIELD_LOWER = Registry.register(DPU.EVENT_TYPE, new Identifier("on_shield_lower"), new DPUEventType(DPUPlayerEvent.class));
    // Code to force Minecraft to load the registries here. Jank, I know, but it works.
    public static void _MINIT() {
        assert ON_USE_EVENT != null;
    }




    Class<? extends DPUEvent> eventClass;
    DPUEventType(Class<? extends DPUEvent> eventClass) {
        this.eventClass = eventClass;
    }
    public HashMap<Identifier, DPUEvent> Events = new HashMap<>();
    public DPUEvent getSubEvent(Identifier eventName) {
        return Events.getOrDefault(eventName, null);
    }
    public <T extends DPUEvent> T createEventType(Object... args){
        try {
            if (args.length == 0) {
                throw new RuntimeException("Event type created with no input.");
            }
            Class<?>[] argTypes = new Class[args.length];
            // First argument should ALWAYS be a string.
            argTypes[0] = JsonObject.class;
            for (int i = 1; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            //noinspection unchecked
            Constructor<T> constructor = (Constructor<T>) eventClass.getConstructor(argTypes);
            System.out.println("Constructed");
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public void addSubEvent (Identifier eventName, DPUEvent event){
        Events.put(eventName, event);
        System.out.println("addedSubEvent");
    }
}
