package com.example.insta

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.show_image.view.*

class MyPickerAdapter (val items: ArrayList<String>?, val context: Context) : RecyclerView.Adapter<MyPickerAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPickerAdapter.MyViewHolder {
//        TODO("Not yet implemented")

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.show_image, parent, false))
    }

    override fun getItemCount(): Int {
        if (items != null) {
            return items.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: MyPickerAdapter.MyViewHolder, position: Int) {
//        TODO("Not yet implemented")
//        Log.d("new img","${items?.get(position)?.getImg().toString()}")

        holder.imageView.setImageURI(Uri.parse(items?.get(position)))
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView
        init {
            imageView=itemView.imageView
        }


    }
}