package jgappsandgames.smartreminderssave.shopping

import jgappsandgames.me.poolutilitykotlin.PoolObjectCreator
import jgappsandgames.me.poolutilitykotlin.PoolObjectInterface

class ShoppingItem: PoolObjectInterface {
    override fun deconstruct() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getID(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class ShoppingItemCreator: PoolObjectCreator<ShoppingItem> {
    override fun generatePoolObject(): ShoppingItem {
        return ShoppingItem()
    }
}