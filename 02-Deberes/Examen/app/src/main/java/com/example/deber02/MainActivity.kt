package com.example.deber02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the database
        DataBase.initialize(this)

        findViewById<Button>(R.id.btn_view_tiendas).setOnClickListener {
            startActivity(Intent(this, TiendaList::class.java))
        }

        findViewById<Button>(R.id.btn_add_tienda).setOnClickListener {
            startActivity(Intent(this, CreateUpdateTienda::class.java))
        }

        findViewById<Button>(R.id.btn_add_celular).setOnClickListener {
            startActivity(Intent(this, CreateUpdateCelular::class.java))
        }
    }
}