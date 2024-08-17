package com.example.examen2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log



class SqliteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TIENDA_TABLE)
        db?.execSQL(CREATE_CELULAR_TABLE)
    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TIENDA_TABLE)
        db?.execSQL(DROP_CELULAR_TABLE)
        onCreate(db)
    }

    fun getAllTiendas(): List<TiendaEntity> {
        val tiendas = mutableListOf<TiendaEntity>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tienda", null)

        if (cursor.moveToFirst()) {
            do {
                val tienda = TiendaEntity(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                    fechaApertura = cursor.getString(cursor.getColumnIndexOrThrow("fechaApertura")),
                    pais = cursor.getString(cursor.getColumnIndexOrThrow("pais")),
                    latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud")),
                    longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"))
                )
                tiendas.add(tienda)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tiendas
    }

    fun getTiendaById(id: Int): TiendaEntity {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM tienda WHERE id = ?", arrayOf(id.toString()))
        cursor.moveToFirst()
        val tienda = TiendaEntity(
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
            direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
            telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
            fechaApertura = cursor.getString(cursor.getColumnIndexOrThrow("fechaApertura")),
            pais = cursor.getString(cursor.getColumnIndexOrThrow("pais")),
            latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud")),
            longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"))
        )
        cursor.close()
        return tienda
    }

    fun getCelularById(id: Int): CelularEntity {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM celular WHERE id = ?", arrayOf(id.toString()))
        cursor.moveToFirst()
        val celular = CelularEntity(
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
            tiendaId = cursor.getInt(cursor.getColumnIndexOrThrow("tiendaId")),
            marca = cursor.getString(cursor.getColumnIndexOrThrow("marca")),
            modelo = cursor.getString(cursor.getColumnIndexOrThrow("modelo")),
            almacenamiento = cursor.getInt(cursor.getColumnIndexOrThrow("almacenamiento")),
            precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio")),
            disponible = cursor.getInt(cursor.getColumnIndexOrThrow("disponible")) > 0
        )
        cursor.close()
        return celular
    }

    fun deleteTienda(id: Int): Int {
        val db = writableDatabase
        return db.delete("tienda", "id=?", arrayOf(id.toString()))
    }

    fun deleteCelular(id: Int): Int {
        val db = writableDatabase
        return db.delete("celular", "id=?", arrayOf(id.toString()))
    }

    fun getCelularesByTiendaId(tiendaId: Int): List<CelularEntity> {
        val celulares = mutableListOf<CelularEntity>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM celular WHERE tiendaId = ?", arrayOf(tiendaId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val celular = CelularEntity(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    tiendaId = cursor.getInt(cursor.getColumnIndexOrThrow("tiendaId")),
                    marca = cursor.getString(cursor.getColumnIndexOrThrow("marca")),
                    modelo = cursor.getString(cursor.getColumnIndexOrThrow("modelo")),
                    almacenamiento = cursor.getInt(cursor.getColumnIndexOrThrow("almacenamiento")),
                    precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio")),
                    disponible = cursor.getInt(cursor.getColumnIndexOrThrow("disponible")) > 0
                )
                celulares.add(celular)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return celulares
    }
    fun insertTienda(nombre: String, direccion: String, telefono: String, pais: String, latitud: Double, longitud: Double): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("nombre", nombre)
            put("direccion", direccion)
            put("telefono", telefono)
            put("pais", pais)
            put("latitud", latitud)
            put("longitud", longitud)
        }
        Log.d("SQLITE", "Insertando tienda: $nombre, $direccion, $telefono, $pais, $latitud, $longitud")
        return db.insert("tienda", null, contentValues)
    }

    fun insertCelular(celular: CelularEntity): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", celular.id)
            put("tiendaId", celular.tiendaId)
            put("marca", celular.marca)
            put("modelo", celular.modelo)
            put("almacenamiento", celular.almacenamiento)
            put("precio", celular.precio)
            put("disponible", celular.disponible)
        }
        return db.insert("celular", null, values)
    }

    companion object {
        private const val DATABASE_NAME = "tienda.db"
        private const val DATABASE_VERSION = 1

        private const val CREATE_TIENDA_TABLE = """
            CREATE TABLE tienda (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                direccion TEXT NOT NULL,
                telefono TEXT NOT NULL,
                fechaApertura TEXT NOT NULL,
                pais TEXT NOT NULL,
                latitud REAL NOT NULL,
                longitud REAL NOT NULL
            )
        """

        private const val CREATE_CELULAR_TABLE = """
            CREATE TABLE celular (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tiendaId INTEGER NOT NULL,
                marca TEXT NOT NULL,
                modelo TEXT NOT NULL,
                almacenamiento INTEGER NOT NULL,
                precio DOUBLE NOT NULL,
                disponible BOOLEAN NOT NULL,
                FOREIGN KEY(tiendaId) REFERENCES tienda(id)
            )
        """

        private const val DROP_TIENDA_TABLE = "DROP TABLE IF EXISTS tienda"
        private const val DROP_CELULAR_TABLE = "DROP TABLE IF EXISTS celular"
    }
}