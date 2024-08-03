package com.example.deber02

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Parcelize
data class TiendaEntity(
    var id: Int,
    var nombre: String,
    var direccion: String,
    var telefono: String,
    var fechaApertura: String
) : Parcelable {
    companion object {
        fun fromCSV(csv: String): TiendaEntity {
            val parts = csv.split(",")
            return TiendaEntity(
                id = parts[0].toInt(),
                nombre = parts[1],
                direccion = parts[2],
                telefono = parts[3],
                fechaApertura = parts[4]
            )
        }
    }

    fun toCSV(): String {
        return "$id,$nombre,$direccion,$telefono,$fechaApertura"
    }

    fun getFechaAperturaAsDate(): LocalDate {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        return LocalDate.parse(fechaApertura, formatter)
    }
}