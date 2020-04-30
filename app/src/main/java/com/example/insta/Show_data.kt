package com.example.insta

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.show_data.*

class Show_data : MainActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        val fileListName: MutableList<String>? = null
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_data)
        goBackButton.setOnClickListener {
            val i=Intent(this,MainActivity::class.java)
            startActivity(i)

        }
//        val adapter=MyPickerAdapter(fileListName,this)
//        recyclerView.adapter =adapter
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(LinearLayoutManager(this));










    }
}