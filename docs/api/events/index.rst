Event Handling
=========

Event files should (normally) match the criteria of the event's associated ID (ie, on_use would use the item name) though some events (global events) run anything under the event_name folder.

If you want to forcibly run something every time an event is fired, regardless of the filename, use a wildcard (-\-.json)



Client Side Events
------
The following will only run on the client, which needs to be put in a Resource Pack (assets/ folder).
::
  {
    "sound": {
      "id": "minecraft:block.amethyst_cluster.break",
      "volume": 0.8,
      "pitch": 1.0
    },
    "particle": {
      "id": "minecraft:bubble_pop",
      "offset": [
        1,1,1
      ]
    }
  }

Server Side Events
------
The following will only run on the server, which needs to be put in a Data Pack (data/ folder).
::
  {
    "function":"some:function",
    "sound": {
      "id": "minecraft:block.amethyst_cluster.step",
      "volume": 0.8,
      "pitch": 1.0
    },
    "particle": {
      "id": "minecraft:poof",
      "offset": [
        1,1,1
      ]
    }
  }
If your event includes the function part, you can use the @s selector to do something with the player that ran the event.

Event predicates
-------
Event predicates will force the event to only apply if a given NBT object is present AND equal. (For example, item contains tag "coin")

Note: the NBT starts at the object's ROOT, so if this event is for an item, it will supply the Item ID and count. BlockStates should also work, though untested at the moment.
::
  
  {
    "function":"something:to_run",
    "predicate": {"nbt": {
      "tag": {"type": "coin"}
    }}
  }




Adding a global event:
==============

minecraft/events/on_sneak/anynamehere.json

- `All global events  </api/events/global>`_


Adding an entity interaction event:
==============
minecraft/events/on_attack_entity/entity_name.json

- `All entity events  </api/events/entity>`_





Adding an event to an item:
===============

minecraft/events/on_use/itemname.json

- `All item events  </api/events/item>`_


