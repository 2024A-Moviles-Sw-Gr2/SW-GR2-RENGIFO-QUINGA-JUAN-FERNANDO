import java.io.File
import java.time.LocalDate

data class Tienda(
    var id: Int,
    var nombre: String,
    var direccion: String,
    var telefono: String,
    var fechaApertura: LocalDate,
    val inventario: MutableList<Celular> = mutableListOf()
)

data class Celular(
    var id: Int,
    var tiendaId: Int,
    var marca: String,
    var modelo: String,
    var almacenamiento: Int,
    var precio: Double,
    var disponible: Boolean
)

class AplicacionCRUD(private val tiendasArchivo: String, private val celularesArchivo: String) {

    private val tiendas: MutableList<Tienda> = mutableListOf()
    private val celulares: MutableList<Celular> = mutableListOf()

    init {
        crearArchivosSiNoExisten()
        cargarDatos()
    }

    private fun crearArchivosSiNoExisten() {
        listOf(tiendasArchivo, celularesArchivo).forEach {
            val file = File(it)
            if (!file.exists()) file.createNewFile()
        }
    }

    private fun cargarDatos() {
        cargarTiendas()
        cargarCelulares()
    }

    private fun cargarTiendas() {
        File(tiendasArchivo).useLines { lines ->
            lines.map { it.split(",") }
                .filter { it.size == 5 }
                .forEach { campos ->
                    tiendas.add(Tienda(
                        id = campos[0].toInt(),
                        nombre = campos[1],
                        direccion = campos[2],
                        telefono = campos[3],
                        fechaApertura = LocalDate.parse(campos[4])
                    ))
                }
        }
    }

    private fun cargarCelulares() {
        File(celularesArchivo).useLines { lines ->
            lines.map { it.split(",") }
                .filter { it.size == 7 }
                .forEach { campos ->
                    val celular = Celular(
                        id = campos[0].toInt(),
                        tiendaId = campos[1].toInt(),
                        marca = campos[2],
                        modelo = campos[3],
                        almacenamiento = campos[4].toInt(),
                        precio = campos[5].toDouble(),
                        disponible = campos[6].toBoolean()
                    )
                    celulares.add(celular)
                    tiendas.find { it.id == celular.tiendaId }?.inventario?.add(celular)
                }
        }
    }

    private fun guardarTiendasEnArchivo() {
        File(tiendasArchivo).printWriter().use { out ->
            tiendas.forEach { tienda ->
                out.println("${tienda.id},${tienda.nombre},${tienda.direccion},${tienda.telefono},${tienda.fechaApertura}")
            }
        }
    }

    private fun guardarCelularesEnArchivo() {
        File(celularesArchivo).printWriter().use { out ->
            celulares.forEach { celular ->
                out.println("${celular.id},${celular.tiendaId},${celular.marca},${celular.modelo},${celular.almacenamiento},${celular.precio},${celular.disponible}")
            }
        }
    }

    fun crearTienda(nombre: String, direccion: String, telefono: String, fechaApertura: LocalDate) {
        val id = (tiendas.maxOfOrNull { it.id } ?: 0) + 1
        tiendas.add(Tienda(id, nombre, direccion, telefono, fechaApertura))
        guardarTiendasEnArchivo()
    }

    fun crearCelular(tiendaId: Int, marca: String, modelo: String, almacenamiento: Int, precio: Double, disponible: Boolean) {
        val tienda = tiendas.find { it.id == tiendaId }
        if (tienda != null) {
            val id = (celulares.maxOfOrNull { it.id } ?: 0) + 1
            val celular = Celular(id, tiendaId, marca, modelo, almacenamiento, precio, disponible)
            celulares.add(celular)
            tienda.inventario.add(celular)
            guardarCelularesEnArchivo()
        } else {
            println("Tienda no encontrada.")
        }
    }

    fun quitarCelularDeTienda(celularId: Int) {
        val celular = celulares.find { it.id == celularId }
        if (celular != null) {
            val tienda = tiendas.find { it.id == celular.tiendaId }
            tienda?.inventario?.remove(celular)
            celulares.remove(celular)
            guardarCelularesEnArchivo()
        } else {
            println("Celular no encontrado.")
        }
    }

    fun leerInventarioDeTienda(tiendaId: Int): List<Celular>? {
        return tiendas.find { it.id == tiendaId }?.inventario
    }

    fun listarTiendas() {
        println("Tiendas disponibles:")
        tiendas.forEach { println("ID: ${it.id}, Nombre: ${it.nombre}") }
    }

    fun listarCelulares() {
        println("Celulares disponibles:")
        celulares.forEach { println("ID: ${it.id}, Marca: ${it.marca}, Modelo: ${it.modelo}, Tienda ID: ${it.tiendaId}") }
    }
}

fun main() {
    val aplicacion = AplicacionCRUD("tiendas.txt", "celulares.txt")

    var opcion: Int
    do {
        println("Menu:")
        println("1. Crear Tienda")
        println("2. Agregar Celular")
        println("3. Quitar Celular")
        println("4. Listar Inventario de Tienda")
        println("5. Salir")
        println("Ingrese su opcion:")
        opcion = readLine()?.toIntOrNull() ?: 0

        when (opcion) {
            1 -> {
                println("Ingrese nombre de la tienda:")
                val nombre = readLine() ?: ""
                println("Ingrese direccion de la tienda:")
                val direccion = readLine() ?: ""
                println("Ingrese telefono de la tienda:")
                val telefono = readLine() ?: ""
                val fechaApertura = LocalDate.now()
                aplicacion.crearTienda(nombre, direccion, telefono, fechaApertura)
                println("Tienda creada exitosamente.")
            }
            2 -> {
                aplicacion.listarTiendas()
                println("Ingrese ID de la tienda:")
                val tiendaId = readLine()?.toIntOrNull() ?: -1
                if (tiendaId == -1) {
                    println("ID de tienda inválido.")
                    continue
                }
                println("Ingrese marca del celular:")
                val marca = readLine() ?: ""
                println("Ingrese modelo del celular:")
                val modelo = readLine() ?: ""
                println("Ingrese almacenamiento del celular:")
                val almacenamiento = readLine()?.toIntOrNull() ?: 0
                println("Ingrese precio del celular:")
                val precio = readLine()?.toDoubleOrNull() ?: 0.0
                println("¿El celular está disponible? (true/false):")
                val disponible = readLine()?.toBoolean() ?: false
                aplicacion.crearCelular(tiendaId, marca, modelo, almacenamiento, precio, disponible)
                println("Celular creado exitosamente.")
            }
            3 -> {
                aplicacion.listarCelulares()
                println("Ingrese ID del celular:")
                val celularId = readLine()?.toIntOrNull() ?: -1
                if (celularId == -1) {
                    println("ID de celular inválido.")
                    continue
                }
                aplicacion.quitarCelularDeTienda(celularId)
                println("Celular eliminado exitosamente.")
            }
            4 -> {
                aplicacion.listarTiendas()
                println("Ingrese ID de la tienda:")
                val tiendaId = readLine()?.toIntOrNull() ?: -1
                if (tiendaId == -1) {
                    println("ID de tienda inválido.")
                    continue
                }
                val inventario = aplicacion.leerInventarioDeTienda(tiendaId)
                if (inventario != null && inventario.isNotEmpty()) {
                    println("Inventario de la Tienda $tiendaId:")
                    inventario.forEach { println(it) }
                } else {
                    println("Tienda no encontrada o sin inventario.")
                }
            }
            5 -> println("Saliendo del programa...")
            else -> println("Opción inválida. Inténtelo de nuevo.")
        }
    } while (opcion != 5)
}
