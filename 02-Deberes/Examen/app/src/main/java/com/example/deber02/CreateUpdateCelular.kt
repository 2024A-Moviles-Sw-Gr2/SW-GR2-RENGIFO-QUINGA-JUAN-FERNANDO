package com.example.deber02

import android.content.ContentValues
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateUpdateCelular : AppCompatActivity() {
    private lateinit var dbHelper: SqliteHelper
    private var celularId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_celular)

        dbHelper = SqliteHelper(this)

        val editTextMarca = findViewById<EditText>(R.id.editTextMarca)
        val editTextModelo = findViewById<EditText>(R.id.editTextModelo)
        val editTextAlmacenamiento = findViewById<EditText>(R.id.editTextAlmacenamiento)
        val editTextPrecio = findViewById<EditText>(R.id.editTextPrecio)
        val editTextDisponible = findViewById<EditText>(R.id.editTextDisponible)
        val spinnerTiendas = findViewById<Spinner>(R.id.spinnerTiendas)

        // Cargar las tiendas en el Spinner
        val tiendas = dbHelper.getAllTiendas()
        if (tiendas.isEmpty()) {
            // Mostrar un mensaje y finalizar la actividad si no hay tiendas
            Toast.makeText(this, "No hay tiendas disponibles. Cree una tienda primero.", Toast.LENGTH_LONG).show()
            finish() // Cierra la actividad
            return
        }

        val tiendaNames = tiendas.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiendaNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTiendas.adapter = adapter

        // Check if we are updating an existing phone
        celularId = intent.getIntExtra("CELULAR_ID", -1)
        if (celularId != -1) {
            val celular = dbHelper.getCelularById(celularId!!)
            editTextMarca.setText(celular.marca)
            editTextModelo.setText(celular.modelo)
            editTextAlmacenamiento.setText(celular.almacenamiento.toString())
            editTextPrecio.setText(celular.precio.toString())
            editTextDisponible.setText(celular.disponible.toString())
            val tienda = tiendas.find { it.id == celular.tiendaId }
            spinnerTiendas.setSelection(tiendaNames.indexOf(tienda?.nombre))
        }

        findViewById<Button>(R.id.buttonSaveCelular).setOnClickListener {
            val marca = editTextMarca.text.toString()
            val modelo = editTextModelo.text.toString()
            val almacenamientoStr = editTextAlmacenamiento.text.toString()
            val precioStr = editTextPrecio.text.toString()
            val disponibleStr = editTextDisponible.text.toString()
            val tiendaSeleccionada = spinnerTiendas.selectedItem.toString()

            if (marca.isEmpty() || modelo.isEmpty() || almacenamientoStr.isEmpty() || precioStr.isEmpty() || disponibleStr.isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val almacenamiento = almacenamientoStr.toInt()
                val precio = precioStr.toDouble()
                val disponible = disponibleStr.toBoolean()
                val tiendaId = tiendas.find { it.nombre == tiendaSeleccionada }?.id

                if (tiendaId != null) {
                    val values = ContentValues().apply {
                        put("tiendaId", tiendaId)
                        put("marca", marca)
                        put("modelo", modelo)
                        put("almacenamiento", almacenamiento)
                        put("precio", precio)
                        put("disponible", disponible)
                    }

                    val db = dbHelper.writableDatabase
                    if (celularId == -1) {
                        val newRowId = db.insert("celular", null, values)
                        if (newRowId != -1L) {
                            finish()
                        } else {
                            Toast.makeText(this, "Error al guardar el celular", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val rowsAffected = db.update("celular", values, "id=?", arrayOf(celularId.toString()))
                        if (rowsAffected > 0) {
                            finish()
                        } else {
                            Toast.makeText(this, "Error al actualizar el celular", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Error al encontrar la tienda seleccionada", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.buttonDeleteCelular).setOnClickListener {
            celularId?.let {
                val rowsDeleted = dbHelper.deleteCelular(it)
                if (rowsDeleted > 0) {
                    finish()
                } else {
                    Toast.makeText(this, "Error al eliminar el celular", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}