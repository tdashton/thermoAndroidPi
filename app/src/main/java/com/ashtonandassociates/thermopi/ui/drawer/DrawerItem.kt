package com.ashtonandassociates.thermopi.ui.drawer

/**
 * Created by theKernel on 18.02.2018.
 */

class DrawerItem() {
    var itemName: String = ""
    var iconId: Int = -1

    constructor(itemName: String, iconId: Int) : this() {
        this.itemName = itemName
        this.iconId = iconId
    }
}