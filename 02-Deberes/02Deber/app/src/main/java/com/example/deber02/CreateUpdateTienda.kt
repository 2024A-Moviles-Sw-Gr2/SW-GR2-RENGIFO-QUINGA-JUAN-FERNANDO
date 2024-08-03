package com.example.deber02

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

        // Check if we are updating an existing store
        tiendaId = intent.getIntExtra("TIENDA_ID", -1)
        if (tiendaId != -1) {
            val tienda = dbHelper.getTiendaById(tiendaId!!)
            editTextNombre.setText(tienda.nombre)
            editTextDireccion.setText(tienda.direccion)
            editTextTelefono.setText(tienda.telefono)
        }

        findViewById<Button>(R.id.buttonSaveTienda).setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val direccion = editTextDireccion.text.toString()
            val telefono = editTextTelefono.text.toString()

            if (nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val fechaApertura = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

                val values = ContentValues().apply {
                    put("nombre", nombre)
                    put("direccion", direccion)
                    put("telefono", telefono)
                    put("fechaApertura", fechaApertura)
                }

                val db = dbHelper.writableDatabase
                if (tiendaId == -1) {
                    val newRowId = db.insert("tienda", null, values)
                    if (newRowId != -1L) {
                        finish()
                    } else {
                        Toast.makeText(this, "Error al guardar la tienda", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val rowsAffected = db.update("tienda", values, "id=?", arrayOf(tiendaId.toString()))
                    if (rowsAffected > 0) {
                        finish()
                    } else {
                        Toast.makeText(this, "Error al actualizar la tienda", Toast.LENGTH_SHORT).show()
                    }
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