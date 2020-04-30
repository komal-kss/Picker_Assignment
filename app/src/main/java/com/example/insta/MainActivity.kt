package com.example.insta

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyfilepicker.activity.ImagePickActivity
import com.easyfilepicker.activity.VideoPickActivity
import com.easyfilepicker.filter.entity.ImageFile
import com.easyfilepicker.filter.entity.VideoFile
import com.picker.gallery.model.GalleryData
import com.picker.gallery.model.interactor.GalleryPicker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_alert.view.*
import java.io.ByteArrayOutputStream


open class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_READ_WRITE = 123
    val fileListName: ArrayList<String> = ArrayList()
    val videoList:ArrayList<String> = ArrayList()
    val REQUEST_RESULT_CODE = 101
    var REQUEST_CODE_PICK_IMAGE = 0x100
    var RESULT_PICK_IMAGE = "ResultPickImage"
    var REQUEST_CODE_TAKE_IMAGE = 0x101
    var REQUEST_CODE_PICK_VIDEO = 0x200
    var RESULT_PICK_VIDEO = "ResultPickVideo"
    var REQUEST_CODE_TAKE_VIDEO = 0x201
    lateinit var dialog :AlertDialog


    var REQUEST_MULTIPLE_IMAGES = 0x600
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter=MyPickerAdapter(fileListName, this@MainActivity)
        recyclerView2.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView2.adapter=VideoAdapter(videoList, this@MainActivity)

        button.setOnClickListener {

            button.setOnClickListener(View.OnClickListener {

                val customDialog= LayoutInflater.from(this).inflate(R.layout.custom_alert,null);

                val dialogBuilder=AlertDialog.Builder(this).setView(customDialog).setTitle("Picker")
                dialog=dialogBuilder.show();

                customDialog.gallery_pickerId.setOnClickListener {

                    Toast.makeText(applicationContext,"Gallery is opening...", Toast.LENGTH_SHORT).show()
                Log.e("gallery","gallllllllllllllllllllll")
                gallery()

                }
                customDialog.camera_pickerId.setOnClickListener {
                    Toast.makeText(applicationContext,"camera is opening...", Toast.LENGTH_SHORT).show()
                Log.e("cam","camfmmmmmmmmm")
                openCamera()

                }
                customDialog.video_pickerId.setOnClickListener {
                    Toast.makeText(applicationContext,"gallery videos is opening...", Toast.LENGTH_SHORT).show()
                    Log.e("cam","camfmmmmmmmmm")
                    video()
                }
                //to dismiss the dialog
                customDialog.cancel_Id.setOnClickListener {
                    dialog.dismiss();
                }
            })
        }


    }
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent,7)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun gallery(){
        if (isReadWritePermitted()) getGalleryResults() else checkReadWritePermission()
        val intent1 = Intent(this, ImagePickActivity::class.java)
        startActivityForResult(intent1, REQUEST_CODE_PICK_IMAGE)
//        val i = Intent(this@MainActivity, PickerActivity::class.java)
//        i.putExtra("IMAGES_LIMIT", 4)
//        i.putExtra("VIDEOS_LIMIT", 4)
//        i.putExtra("REQUEST_RESULT_CODE", REQUEST_RESULT_CODE)
//        startActivityForResult(i, 101)//comment krdo

    }
    fun video()
    {
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
                        var totalItem= list.size
                        for(i in 0..(totalItem?.minus(1))!!){
                            Log.d("totalItem",totalItem.toString())
                   Log.d("image1", data?.clipData?.getItemAt(i).toString())
                            val uri = list.get(i).path
                            Log.d("uri",uri)


                            fileListName?.add(uri)


                        }
//                        Log.d("list", list.toString())
                        recyclerView.adapter?.notifyDataSetChanged()
                        dialog.dismiss()
//                        val intent=Intent(this,Show_data::class.java)
//
//                        startActivity(intent)
                    } catch (e: Exception) {
                    }
                }
                REQUEST_CODE_PICK_VIDEO -> {
                    val list: ArrayList<VideoFile> =
                        data!!.getParcelableArrayListExtra(RESULT_PICK_VIDEO)
                    var totalItem= list.size
                    Log.e("video1",list.get(0).path.toString())
                    for(i in 0..(totalItem?.minus(1))!!){
                        Log.d("totalItemv",totalItem.toString())

                        val path = list.get(i).path
                        Log.d("video",path)


                        videoList?.add(path)


                    }
                    recyclerView2.adapter?.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
        }



        if (resultCode === Activity.RESULT_OK && requestCode===REQUEST_RESULT_CODE ) {
            val mediaList = data?.getParcelableArrayListExtra<GalleryData>("MEDIA")
//            Log.e("SELECTED MEDIA", mediaList.size.toString())

            var totalItem= mediaList?.size
             Log.d("totalitem", totalItem.toString())

            for(i in 0..totalItem!!) {
                Log.d("image11", mediaList?.get(i).toString())


            }




//            val i=Intent(this@MainActivity,Show_data::class.java)
//            startActivity(i);
        }

        if (requestCode === 7 && resultCode === Activity.RESULT_OK) {

            val bit = data?.extras!!["data"] as Bitmap?
            Log.d("cam",bit.toString())
            var img:String?=null
            if (bit != null) {
                img=getImageUri(this,bit)
                Log.d("String",img)
            }
            if (img != null) {
                fileListName.add(img)
            }

            recyclerView.adapter?.notifyDataSetChanged()
            dialog.dismiss()
//            imageView2.setImageBitmap(bitmap)
        }


    }
    public fun getImageUri(inContext:Context,inImage:Bitmap) :String{

  inImage.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream());
  var path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//  return Uri.parse(path);
//        MediaStore.Images.
        Log.d("path cam",path)
        return path;
}

//    fun BitMapToString(bitmap: Bitmap): String {
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
//        val b = baos.toByteArray()
//        return Base64.encodeToString(b, Base64.DEFAULT)
//    }

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


