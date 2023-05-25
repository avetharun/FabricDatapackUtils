Additional Item Model Overrides
===============
- is_hand_first | is_hand_third | is_hand_any : 
	*boolean* : Shows override only if specified model is in the user's hand.
- is_fixed :
	*boolean* : Shows override only if specified model is in an Item Frame
- is_gui | is_inventory : 
	*boolean* : Shows override only if specified model is in the GUI. This includes the hotbar.
- is_hotbar :
	*boolean* - Shows override only in the hotbar. Does not include inventory.
- is_dropped : 
	*boolean* - Shows override only if the item is dropped (On the ground)
Example:
::
	"overrides": [
		{
			"predicate": {
				"is_hand_any": true
			},
			"model": "namespace:item/item_in_hand"
		}
	]
