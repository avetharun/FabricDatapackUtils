Additional Item Model Overrides
===============

.. code-block::
    is_hand_first | is_hand_third | is_hand_any : boolean
    - Shows override only if specified model is in the user's hand
    Example:
	"overrides": [
		{
			"predicate": {
				"is_hand_any": true
			},
			"model": "namespace:item/item_in_hand"
		}
    ]
