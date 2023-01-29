package com.example.turtine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class Listadapter (val context: Context, val routinelist: ArrayList<Routine>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(context).inflate(R.layout.lv_finish, null)

        val Title_tv = view.findViewById<TextView>(R.id.Title_tv)
        val content_tv = view.findViewById<TextView>(R.id.content_tv)
        val rlist = routinelist[position]
        Title_tv.text = rlist.title
        content_tv.text = rlist.rot


        return view
    }

    override fun getItem(position: Int): Any {
        return routinelist[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return routinelist.size
    }
}
