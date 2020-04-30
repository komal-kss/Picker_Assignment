package com.example.insta

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyfilepicker.Constant.*
import com.easyfilepicker.activity.ImagePickActivity
import com.easyfilepicker.activity.VideoPickActivity
import com.easyfilepicker.filter.entity.ImageFile
import com.easyfilepicker.filter.entity.VideoFile
import com.example.insta.AppConstants.PERMISSIONS_READ_WRITE
import com.picker.gallery.model.interactor.GalleryPicker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_alert.view.*
import java.io.ByteArrayOutputStream


open class MainActivity : AppCompatActivity() {

    lateinit var dialog: AlertDialog
    private val fileListName: ArrayList<String> = ArrayList()
    private val videoList: ArrayList<String> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvImagesList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvImagesList.adapter = MyPickerAdapter(fileListName, this@MainActivity)
        rvVideosList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvVideosList.adapter = VideoAdapter(videoList, this@MainActivity)
        btnAttachment.setOnClickListener(View.OnClickListener {
            val customDialog = LayoutInflater.from(this).inflate(R.layout.custom_alert, null);
            val dialogBuilder =
                AlertDialog.Builder(this).setView(customDialog).setTitle("Choose Attachment")
            dialog = dialogBuilder.show();
            customDialog.gallery_pickerId.setOnClickListener {
                Toast.makeText(applicationContext, "Gallery is opening...", Toast.LENGTH_SHORT)
                    .show()
                gallery()

            }
            customDialog.camera_pickerId.setOnClickListener {
                Toast.makeText(applicationContext, "camera is opening...", Toast.LENGTH_SHORT)
                    .show()
                openCamera()
            }
            customDialog.video_pickerId.setOnClickListener {
                Toast.makeText(
                    applicationContext,
                    "gallery videos is opening...",
                    Toast.LENGTH_SHORT
                ).show()
                video()
            }
            customDialog.cancel_Id.setOnClickListener {
                dialog.dismiss();
            }
        })
    }


    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 7)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun gallery() {
        if (isReadWritePermitted()) getGalleryResults() else checkReadWritePermission()
        val intent1 = Intent(this, ImagePickActivity::class.java)
        startActivityForResult(intent1, REQUEST_CODE_PICK_IMAGE)
    }

    fun video() {
        val intent = Intent(this, VideoPickActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    try {
                        val list: ArrayList<ImageFile> =
                            data!!.getParcelableArrayListExtra(RESULT_PICK_IMAGE)
                        val totalItem = list.size
                        for (i in 0..(totalItem.minus(1))) {
                            val uri = list.get(i).path
                            fileListName.add(uri)
                        }
                        rvImagesList.adapter?.notifyDataSetChanged()
                        dialog.dismiss()
                    } catch (e: Exception) {
                    }
                }
                REQUEST_CODE_PICK_VIDEO -> {
                    val list: ArrayList<VideoFile> =
                        data!!.getParcelableArrayListExtra(RESULT_PICK_VIDEO)
                    val totalItem = list.size
                    for (i in 0..(totalItem.minus(1))) {
                        val path = list.get(i).path
                        videoList.add(path)
                    }
                    rvVideosList.adapter?.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
        }

        if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
            val bit = data?.extras!!["data"] as Bitmap?
            var img: String? = null
            if (bit != null) {
                img = getImageUri(this, bit)
            }
            if (img != null) {
                fileListName.add(img)
            }
            rvImagesList.adapter?.notifyDataSetChanged()
            dialog.dismiss()
        }


    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): String {

        inImage.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream());
        return MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkReadWritePermission(): Boolean {
        requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PERMISSIONS_READ_WRITE
        )
        return true
    }

    private fun getGalleryResults() {
        val images = GalleryPicker(this).getImages()
        val videos = GalleryPicker(this).getVideos()
    }

    private fun isReadWritePermitted(): Boolean =
        (checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkCallingOrSelfPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
}


