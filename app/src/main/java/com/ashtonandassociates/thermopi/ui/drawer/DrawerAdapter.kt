package com.ashtonandassociates.thermopi.ui.drawer

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ashtonandassociates.thermopi.R

/**
 * Created by theKernel on 18.02.2018.
 */

class DrawerAdapter(context: Context, layoutResourceID: Int, listItems: List<DrawerItem>) : ArrayAdapter<DrawerItem>(context, layoutResourceID, listItems) {
//    var myContext = context
    var layoutResourceId = layoutResourceID
    var listItems = listItems

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view: View
        var drawerItem = DrawerItem()

        if (convertView == null) {
            val act: Activity = context as Activity
            val inflater: LayoutInflater = act.layoutInflater
            view = inflater.inflate(layoutResourceId, parent, false)
        } else {
            view = convertView
        }

        view.findViewById<ImageView>(R.id.drawer_item_icon).setImageResource(listItems.get(position).iconId)
        view.findViewById<TextView>(R.id.drawer_item_text).text = listItems.get(position).itemName
//        drawerItem.iconId = listItems.get(position).iconId
//        drawerItem.itemName = listItems.get(position).itemName

        return view
    }
}
