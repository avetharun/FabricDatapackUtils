TestForBlock command
=========
The /testforblock command adds a check to look for a specific block in a set box. As of the 1.0.2 update, this is NOT async. Large checks can eat up resources!


Command syntax: ::

- /testforblock [start : pos] [end : pos] block

The block argument is able to take in a specific blockstate, so for example, if you wanted to check for only a lit campfire, you can use :code:`minecraft:campfire[lit=true]`
