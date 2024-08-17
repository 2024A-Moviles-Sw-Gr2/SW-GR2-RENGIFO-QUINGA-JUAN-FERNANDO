package com.example.deber03

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView

class VerEstadoActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_estado)

        val imagenEstado = intent.getIntExtra("imagenEstado", 0)
        val nombreUsuario = intent.getStringExtra("nombreUsuario")

        val imageViewEstado: ImageView = findViewById(R.id.imageViewEstado)
        val textViewNombre: TextView = findViewById(R.id.textViewNombre)

        imageViewEstado.setImageResource(imagenEstado)
        textViewNombre.text = nombreUsuario

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 5000)

        val buttonCerrar: ImageView = findViewById(R.id.buttonCerrar)
        buttonCerrar.setOnClickListener {
            finish()
        }


    }
}