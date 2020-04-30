package com.example.insta;

import com.easyfilepicker.filter.entity.VideoFile;

public class VideoModel {
    VideoFile img;
    VideoModel(VideoFile img)
    {
        this.img=img;
    }

    public VideoFile getImg() {
        return img;
    }

    public void setImg(VideoFile img) {
        this.img = img;
    }
}
