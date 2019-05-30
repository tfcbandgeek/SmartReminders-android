package jgappsandgames.smartreminderssave.shopping

import jgappsandgames.me.poolutilitykotlin.Pool

val shoppingPool = Pool(minSize = 5, increment = 5, decrement = 3, maxSize = 50, generator = ShoppingItemCreator())

