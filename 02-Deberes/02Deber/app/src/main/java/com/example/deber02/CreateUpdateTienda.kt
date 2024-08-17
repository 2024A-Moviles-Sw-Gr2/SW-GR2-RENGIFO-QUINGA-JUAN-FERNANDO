package com.example.deber02

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateUpdateTienda : AppCompatActivity() {
    private lateinit var dbHelper: SqliteHelper
    private var tiendaId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_tienda)

        dbHelper = SqliteHelper(this)

        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val editTextDireccion = findViewById<EditText>(R.id.editTextDireccion)
        val editTextTelefono = findViewById<EditText>(R.id.editTextTelefono)
        val editTextLatitud = findViewById<EditText>(R.id.editTextLatitud)
        val editTextLongitud = findViewById<EditText>(R.id.editTextLongitud)

        // Check if we are updating an existing store
        tiendaId = intent.getIntExtra("TIENDA_ID", -1)
        if (tiendaId != -1) {
            val tienda = dbHelper.getTiendaById(tiendaId!!)
            editTextNombre.setText(tienda.nombre)
            editTextDireccion.setText(tienda.direccion)
            editTextTelefono.setText(tienda.telefono)
            editTextLatitud.setText(tienda.latitud.toString())  // Nuevo
            editTextLongitud.setText(tienda.longitud.toString())  // Nuevo
        }

        findViewById<Button>(R.id.buttonSaveTienda).setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val direccion = editTextDireccion.text.toString()
            val telefono = editTextTelefono.text.toString()
            val latitudStr = editTextLatitud.text.toString()
            val longitudStr = editTextLongitud.text.toString()

            if (nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || latitudStr.isEmpty() || longitudStr.isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                try {
                    val latitud = latitudStr.toDouble()
                    val longitud = longitudStr.toDouble()

                    val values = ContentValues().apply {
                        put("nombre", nombre)
                        put("direccion", direccion)
                        put("telefono", telefono)
                        put("latitud", latitud)  // Campo latitud
                        put("longitud", longitud)  // Campo longitud
                    }

                    val db = dbHelper.writableDatabase
                    if (tiendaId == -1) {
                        val newRowId = db.insert("tienda", null, values)
                        if (newRowId != -1L) {
                            Toast.makeText(this, "Tienda guardada", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Error al guardar la tienda", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        val rowsAffected =
                            db.update("tienda", values, "id=?", arrayOf(tiendaId.toString()))
                        if (rowsAffected > 0) {
                            Toast.makeText(this, "Tienda actualizada", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Error al actualizar la tienda",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(
                        this,
                        "Formato incorrecto de latitud o longitud",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        findViewById<Button>(R.id.buttonDeleteTienda).setOnClickListener {
            tiendaId?.let {
                val rowsDeleted = dbHelper.deleteTienda(it)
                if (rowsDeleted > 0) {
                    finish()
                } else {
                    Toast.makeText(this, "Error al eliminar la tienda", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}