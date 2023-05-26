Adding custom events
==============
The following requires the mod to be included in your mod's dependencies.

Create an event class:
..  code-block:: php
   public class CustomEvent extends DPUEvent{
      CustomEvent(JsonObject object) {super(object);}
      
      boolean someBoolean;
      
      @Override
      public CustomEvent Deserialize(JsonObject object) {
          // read from the event file here
          return super.Deserialize(object);
      }
   }

Register the class:
::
  // In mod initializer (NOT CLIENT):
  
  public static final DPUEventType MY_CUSTOM_EVENT = Registry.register(DPU.EVENT_TYPE, new Identifier("custom_event"), new DPUEventType(CustomEvent.class));

Now, you need to `create an event file  </api/events>`_

You should now have a working event. Remember to invoke the event with the following: 
::
  // Inside some function, for example, an item's use function:
  if (world.isClient) {
    DPU.InvokeClientEventFor(MY_CUSTOM_EVENT, new Identifier("namespace","path"));
  } else {
    DPU.InvokeServerEventFor(MY_CUSTOM_EVENT, new Identifier("namespace","path"), (ServerWorld) world, (PlayerEntity)user);
  }
