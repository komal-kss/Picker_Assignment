package com.example.insta;

import com.easyfilepicker.filter.entity.ImageFile;

public class DataListModel {
    ImageFile img;
    DataListModel(ImageFile img)
    {
        this.img=img;
    }

    public ImageFile getImg() {
        return img;
    }

    public void setImg(ImageFile img) {
        this.img = img;
    }
}
