package com.example.deber02

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

class TiendaList : AppCompatActivity() {
    private lateinit var dbHelper: SqliteHelper
    private lateinit var listViewTiendas: ListView
    private lateinit var tiendas: List<TiendaEntity>
    private lateinit var adapter: ArrayAdapter<String>
    private var selectedTiendaId: Int? = null
    private var selectedCelularId: Int? = null
    private var selectedItemType: String = "Tienda"  // "Tienda" or "Celular"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tienda_list)

        dbHelper = SqliteHelper(this)
        listViewTiendas = findViewById(R.id.listViewTiendas)

        loadTiendas()

        listViewTiendas.setOnItemClickListener { parent, view, position, id ->
            selectedTiendaId = tiendas[position].id
            viewCelulares(selectedTiendaId!!)
        }

        registerForContextMenu(listViewTiendas)
    }

    override fun onResume() {
        super.onResume()
        loadTiendas() // Recargar la lista cada vez que la actividad vuelve a ser visible
    }

    private fun loadTiendas() {
        tiendas = dbHelper.getAllTiendas()
        val tiendaNames = tiendas.map { it.nombre }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tiendaNames)
        listViewTiendas.adapter = adapter
    }

    private fun viewCelulares(tiendaId: Int) {
        val intent = Intent(this, CelularesList::class.java).apply {
            putExtra("TIENDA_ID", tiendaId)
        }
        startActivity(intent)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        selectedItemType = if (selectedTiendaId != null) "Tienda" else "Celular"
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        selectedTiendaId = tiendas[info.position].id
        return when (item.itemId) {
            R.id.edit -> {
                if (selectedItemType == "Tienda") {
                    selectedTiendaId?.let { editTienda(it) }
                } else {
                    selectedCelularId?.let { editCelular(it) }
                }
                true
            }
            R.id.delete -> {
                if (selectedItemType == "Tienda") {
                    selectedTiendaId?.let { deleteTienda(it) }
                } else {
                    selectedCelularId?.let { deleteCelular(it) }
                }
                true
            }
            R.id.view_location -> {
                selectedTiendaId?.let { viewLocation(it) }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun viewLocation(tiendaId: Int) {
        val tienda = dbHelper.getTiendaById(tiendaId)
        val intent = Intent(this, MapsActivity::class.java).apply {
            putExtra("LATITUD", tienda.latitud)
            putExtra("LONGITUD", tienda.longitud)
        }
        startActivity(intent)
    }

    private fun editTienda(tiendaId: Int) {
        val intent = Intent(this, CreateUpdateTienda::class.java).apply {
            putExtra("TIENDA_ID", tiendaId)
        }
        startActivity(intent)
    }

    private fun deleteTienda(tiendaId: Int) {
        val rowsDeleted = dbHelper.deleteTienda(tiendaId)
        if (rowsDeleted > 0) {
            loadTiendas()
            Toast.makeText(this, "Tienda eliminada", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al eliminar la tienda", Toast.LENGTH_SHORT).show()
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
            selectedTiendaId?.let { viewCelulares(it) }
            Toast.makeText(this, "Celular eliminado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al eliminar el celular", Toast.LENGTH_SHORT).show()
        }
    }
}