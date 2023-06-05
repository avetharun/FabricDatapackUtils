Event Handling
=========


Client Side Events
------
The following will only run on the client, which needs to be put in a Resource Pack.
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
The following will only run on the server, which needs to be put in a Data Pack.
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





Adding a global event:
==============

assets/minecraft/events/on_sneak/anynamehere.json:
::
   {
    "sound": {
      "id": "minecraft:block.amethyst_cluster.break",
      "volume": 0.8,
      "pitch": 1.0
    }
   }





Adding an event to an item:
===============

assets/minecraft/events/on_use/itemname.json:
::
   {
    "sound": {
      "id": "minecraft:block.amethyst_cluster.break",
      "volume": 0.8,
      "pitch": 1.0
    }
   }


