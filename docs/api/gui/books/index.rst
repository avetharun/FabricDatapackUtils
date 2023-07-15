Written Book API
=======
- Note: the following applies to **Written Books** and can only be done via NBT editing!
These methods of modifying the written book is per-page! If you want to use it for the entire book, you need to modify each page to include the image!

Adding a custom background to a page:
::
  "pages":[
    '{"texture":"namespace:textures/gui/book"}'
  ]

Adding a custom foreground layer to a page:
::
  "pages":[
    '{"foreground":"namespace:textures/gui/something_in_front_of_text"}'
  ]

The identifiers do not require a .png at the end! It will add automatically.
