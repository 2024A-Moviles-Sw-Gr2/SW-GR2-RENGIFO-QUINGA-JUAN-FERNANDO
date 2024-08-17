package com.example.examen2

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CelularesList : AppCompatActivity() {
    private lateinit var dbHelper: SqliteHelper
    private lateinit var listViewCelulares: ListView
    private lateinit var celulares: List<CelularEntity>
    private lateinit var adapter: ArrayAdapter<String>
    private var selectedCelularId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_celulares_list)

        dbHelper = SqliteHelper(this)
        listViewCelulares = findViewById(R.id.listViewCelulares)

        val tiendaId = intent.getIntExtra("TIENDA_ID", -1)
        if (tiendaId != -1) {
            loadCelulares(tiendaId)
        }

        registerForContextMenu(listViewCelulares)
    }

    override fun onResume() {
        super.onResume()
        val tiendaId = intent.getIntExtra("TIENDA_ID", -1)
        if (tiendaId != -1) {
            loadCelulares(tiendaId) // Recargar la lista cada vez que la actividad vuelve a ser visible
        }
    }

    private fun loadCelulares(tiendaId: Int) {
        celulares = dbHelper.getCelularesByTiendaId(tiendaId)
        val celularDetails = celulares.map { "${it.marca} ${it.modelo} - ${it.precio} - ${if (it.disponible) "Disponible" else "No Disponible"}" }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, celularDetails)
        listViewCelulares.adapter = adapter
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        selectedCelularId = celulares[info.position].id
        return when (item.itemId) {
            R.id.edit -> {
                selectedCelularId?.let { editCelular(it) }
                true
            }
            R.id.delete -> {
                selectedCelularId?.let { deleteCelular(it) }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun editCelular(celularId: Int) {
        val intent = Intent(this, CreateUpdateCelular::class.java).apply {
            putExtra("CELULAR_ID", celularId)
        }
        startActivity(intent)
    }

    private fun deleteCelular(celularId: Int) {
        val rowsDeleted = dbHelper.deleteCelular(celularId)
        if (rowsDeleted > 0) {
            val tiendaId = intent.getIntExtra("TIENDA_ID", -1)
            if (tiendaId != -1) {
                loadCelulares(tiendaId)
            }
            Toast.makeText(this, "Celular eliminado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al eliminar el celular", Toast.LENGTH_SHORT).show()
        }
    }
}