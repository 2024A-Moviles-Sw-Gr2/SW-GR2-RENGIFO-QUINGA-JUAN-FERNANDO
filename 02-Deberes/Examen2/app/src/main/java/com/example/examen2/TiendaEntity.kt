package com.example.examen2

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Parcelize
data class TiendaEntity(
    var id: Int,
    var nombre: String,
    var direccion: String,
    var telefono: String,
    var pais: String,
    var latitud: Double,
    var longitud: Double,
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
                pais = parts[4],
                latitud = parts[5].toDouble(),
                longitud = parts[6].toDouble(),
                fechaApertura = parts[7]

            )
        }
    }

    fun toCSV(): String {
        return "$id,$nombre,$direccion,$telefono,$pais, $latitud, $longitud,$fechaApertura"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFechaAperturaAsDate(): LocalDate {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        return LocalDate.parse(fechaApertura, formatter)
    }
}