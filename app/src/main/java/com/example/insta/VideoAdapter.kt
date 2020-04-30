package com.example.insta

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.easyfilepicker.filter.entity.VideoFile
import kotlinx.android.synthetic.main.show_image.view.*
import kotlinx.android.synthetic.main.show_video.view.*

class VideoAdapter (val items: ArrayList<String>?, val context: Context) : RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoAdapter.MyViewHolder {
//        TODO("Not yet implemented")

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.show_video,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        if (items != null) {
            return items.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: VideoAdapter.MyViewHolder, position: Int) {
//        TODO("Not yet implemented")


        holder.videoView.setVideoURI(Uri.parse(items?.get(position)))
//        holder.videoView.start()
        holder.videoView.seekTo(1)


    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var videoView: VideoView

        init {
            videoView = itemView.videoView

        }


    }
}