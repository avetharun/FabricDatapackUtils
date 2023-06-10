Entity Events
======
These kinds of events are fired when an entity does something, or when a player interacts with an entity.


Names of event files should be the same as the entity's name (for example, minecraft/events/on_attack_entity/sheep.json)


Event types as of 1.0.1
------

- on_attack_entity : When a player attacks an entity
- on_interact_entity : When a player right clicks on an entity
- on_collide_entity : When any entity collides with another entity
- - File requires the following structure: 'source_target.json'. The source being the entity colliding, which will be targettable by @s (example, player) and target being the target entity you want to fire when colliding with (example, fireball)
