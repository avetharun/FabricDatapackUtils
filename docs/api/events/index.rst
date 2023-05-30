Adding an event to an item:
===============

Event types:

- on_use : On use item

- on_interact_block : On interact with block

- on_swing : On swinging hand (or item)

- on_attack_entity : On entity attacked (by player)

- on_interact_entity : On interact with entity (by player)


A few example events will be supplied, though they can be changed and modified by `Creating a custom event type <adding_custom_events>`_
The following applies to all event files:

- Sound::id is required to use a sound for the event.

- Particle::id is required to use a particle.

The other paramaters are just there for documentation, the "id" paramater is the only required one for the built-in events.


Client Side Event
=======
The following will only run on the client, which needs to be put in a Resource Pack.
assets/minecraft/events/on_use/itemname.json:
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

Server Side Event
=======
The following will only run on the server, which needs to be put in a Data Pack.
data/minecraft/events/on_use/itemname.json:
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



