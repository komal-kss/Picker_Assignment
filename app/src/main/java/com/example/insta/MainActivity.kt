package com.example.insta

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.picker.gallery.model.GalleryData
import com.picker.gallery.model.interactor.GalleryPicker
import com.picker.gallery.utils.MLog
import com.picker.gallery.view.PickerActivity
import kotlinx.android.synthetic.main.activity_main.*


open class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_READ_WRITE = 123

    val REQUEST_RESULT_CODE = 101
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
           val dialog=AlertDialog.Builder(this@MainActivity)
            dialog.setMessage("Choose option")

            dialog.setNeutralButton("Gallery"){dialog, which->
                Toast.makeText(applicationContext,"Gallery is opening...", Toast.LENGTH_SHORT).show()
                Log.e("gallery","gallllllllllllllllllllll")
                gallery()
            }
            dialog.setPositiveButton("Camera"){dialog, which ->
                Toast.makeText(applicationContext,"camera is opening...", Toast.LENGTH_SHORT).show()
                Log.e("cam","camfmmmmmmmmm")
                openCamera()
            }
            dialog.show()
        }

    }
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent,7)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun gallery(){
        if (isReadWritePermitted()) getGalleryResults() else checkReadWritePermission()
        val i = Intent(this@MainActivity, PickerActivity::class.java)
        i.putExtra("IMAGES_LIMIT", 4)
        i.putExtra("VIDEOS_LIMIT", 4)
        i.putExtra("REQUEST_RESULT_CODE", REQUEST_RESULT_CODE)
        startActivityForResult(i, 101)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == REQUEST_RESULT_CODE && data != null) {
            val mediaList = data.getParcelableArrayListExtra<GalleryData>("MEDIA")
            MLog.e("SELECTED MEDIA", mediaList.size.toString())

            for(image in mediaList){
                Log.e("komu",image.toString())
            }


            val i=Intent(this@MainActivity,Show_data::class.java)


            startActivity(i);
        }

        if (requestCode === 7 && resultCode === Activity.RESULT_OK) {
            val bitmap = data!!.extras!!["data"] as Bitmap?

            imageView2.setImageBitmap(bitmap)
        }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkReadWritePermission(): Boolean {
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_READ_WRITE)
        return true
    }

    private fun getGalleryResults() {
        val images = GalleryPicker(this).getImages()
        val videos = GalleryPicker(this).getVideos()
       Log.e("no","${images.size}")

    }
    private fun isReadWritePermitted(): Boolean = (checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
}

