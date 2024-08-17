package com.example.examen2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CelularEntity(
    var id: Int,
    var tiendaId: Int,
    var marca: String,
    var modelo: String,
    var almacenamiento: Int,
    var precio: Double,
    var disponible: Boolean
) : Parcelable {
    companion object {
        fun fromCSV(csv: String): CelularEntity {
            val parts = csv.split(",")
            return CelularEntity(
                id = parts[0].toInt(),
                tiendaId = parts[1].toInt(),
                marca = parts[2],
                modelo = parts[3],
                almacenamiento = parts[4].toInt(),
                precio = parts[5].toDouble(),
                disponible = parts[6].toBoolean()
            )
        }
    }

    fun toCSV(): String {
        return "$id,$tiendaId,$marca,$modelo,$almacenamiento,$precio,$disponible"
    }
}