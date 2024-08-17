package com.example.examen2

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CreateUpdateTienda : AppCompatActivity() {
    private lateinit var dbHelper: SqliteHelper
    private var tiendaId: Int? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_tienda)

        dbHelper = SqliteHelper(this)

        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val editTextDireccion = findViewById<EditText>(R.id.editTextDireccion)
        val editTextTelefono = findViewById<EditText>(R.id.editTextTelefono)
        val editTextPais = findViewById<EditText>(R.id.editTextPais)
        val editTextLatitud = findViewById<EditText>(R.id.editTextLatitud)
        val editTextLongitud = findViewById<EditText>(R.id.editTextLongitud)

        // Check if we are updating an existing store
        tiendaId = intent.getIntExtra("TIENDA_ID", -1)
        if (tiendaId != -1) {
            val tienda = dbHelper.getTiendaById(tiendaId!!)
            editTextNombre.setText(tienda.nombre)
            editTextDireccion.setText(tienda.direccion)
            editTextTelefono.setText(tienda.telefono)
            editTextPais.setText(tienda.pais)
            editTextLatitud.setText(tienda.latitud.toString())
            editTextLongitud.setText(tienda.longitud.toString())
        }

        findViewById<Button>(R.id.buttonSaveTienda).setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val direccion = editTextDireccion.text.toString()
            val telefono = editTextTelefono.text.toString()
            val pais = editTextPais.text.toString()
            val latitud = editTextLatitud.text.toString().toDoubleOrNull()
            val longitud = editTextLongitud.text.toString().toDoubleOrNull()

            if (nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || pais.isEmpty() || latitud == null || longitud == null) {
                Toast.makeText(this, "Por favor, rellene todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val fechaApertura = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

            val values = ContentValues().apply {
                put("nombre", nombre)
                put("direccion", direccion)
                put("telefono", telefono)
                put("fechaApertura", fechaApertura)
                put("pais", pais)
                put("latitud", latitud)
                put("longitud", longitud)
            }

            val db = dbHelper.writableDatabase
            if (tiendaId == -1) {
                val newRowId = db.insert("tienda", null, values)
                if (newRowId != -1L) {
                    Toast.makeText(this, "Tienda guardada con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar la tienda", Toast.LENGTH_SHORT).show()
                }
            } else {
                val rowsAffected = db.update("tienda", values, "id=?", arrayOf(tiendaId.toString()))
                if (rowsAffected > 0) {
                    Toast.makeText(this, "Tienda actualizada con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al actualizar la tienda", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.buttonDeleteTienda).setOnClickListener {
            tiendaId?.let {
                val rowsDeleted = dbHelper.deleteTienda(it)
                if (rowsDeleted > 0) {
                    Toast.makeText(this, "Tienda eliminada con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al eliminar la tienda", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.buttonViewMap).setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("LATITUD", editTextLatitud.text.toString().toDouble())
            intent.putExtra("LONGITUD", editTextLongitud.text.toString().toDouble())
            startActivity(intent)
        }
    }
}