Adding custom events
==============
The following requires the mod to be included in your mod's dependencies.

Create an event class:
::
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


Create an event file:
*Supported event types are ONLY items*
The following example event file will spawn a particle and play a sound when the event is fired:
::
  {
      
  }
  
You should now have a working event. Remember to invoke the event with the following: 
::
  // Inside some function, for example, an item's use function:
  if (world.isClient) {
    DPU.InvokeClientEventFor(MY_CUSTOM_EVENT, new Identifier("namespace","path"));
  } else {
    DPU.InvokeServerEventFor(MY_CUSTOM_EVENT, new Identifier("namespace","path"), (ServerWorld) world, (PlayerEntity)user);
  }
