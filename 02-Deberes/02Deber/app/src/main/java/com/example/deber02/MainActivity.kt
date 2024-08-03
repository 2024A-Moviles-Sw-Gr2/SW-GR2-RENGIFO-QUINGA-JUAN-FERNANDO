package com.example.deber02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private lateinit var tiendaAdapter: TiendaAdapter
    private lateinit var tiendas: MutableList<Tienda>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tiendas = loadStores()
        tiendaAdapter = TiendaAdapter(tiendas) { tienda -> showStoreDetails(tienda) }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tiendaAdapter

        findViewById<Button>(R.id.buttonCreateStore).setOnClickListener {
            val intent = Intent(this, CreateStoreActivity::class.java)
            startActivityForResult(intent, CREATE_STORE_REQUEST)
        }
    }

    private fun showStoreDetails(tienda: Tienda) {
        val intent = Intent(this, StoreDetailsActivity::class.java)
        intent.putExtra("STORE_ID", tienda.id)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_STORE_REQUEST && resultCode == RESULT_OK) {
            val newStore = data?.getParcelableExtra<Tienda>("NEW_STORE")
            newStore?.let {
                tiendas.add(it)
                tiendaAdapter.notifyDataSetChanged()
                saveStore(it)
            }
        }
    }

    private fun loadStores(): MutableList<Tienda> {
        val stores = mutableListOf<Tienda>()
        val lines = FileHelper.readFromFile("tiendas.txt", this)
        for (line in lines) {
            val store = Tienda.fromCSV(line)
            store.inventario.addAll(loadCelularesForStore(store.id))
            stores.add(store)
        }
        return stores
    }

    private fun loadCelularesForStore(storeId: Int): MutableList<Celular> {
        val celulares = mutableListOf<Celular>()
        val lines = FileHelper.readFromFile("celulares.txt", this)
        for (line in lines) {
            val celular = Celular.fromCSV(line)
            if (celular.tiendaId == storeId) {
                celulares.add(celular)
            }
        }
        return celulares
    }

    private fun saveStore(tienda: Tienda) {
        FileHelper.writeToFile("tiendas.txt", tienda.toCSV(), this)
    }

    companion object {
        const val CREATE_STORE_REQUEST = 1
    }
}