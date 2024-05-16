import java.util.*

fun main(){
    println("Hola Mundo")
    // Inmutables (No se puede RE ASIGNAR "=")

    val inmutable: String = "Adrian";
    //inmutable = "Vicente" //Error (No vale porque es solo de lectura)


    //MUTABLES
    var mutable: String = "Vicente"
    mutable = "Vicente" //Si vale

    //VAL > VAR


    //DUCK TYPING
    var ejemploVariable = "Juan Rengifo"
    val edadEjemplo = 12
    ejemploVariable.trim()
    //ejemploVariable = edadEjemplo //Error, tipo incorrecto


    //VARIABLES PRIMITIVAS
    val nombre = "Adrian"
    val sueldo = 1.2
    val estadoCivil = 'C'
    val mayorEdad = true


    //CLASES EN JAVA
    val fechaNacimiento: Date = Date()


    //When(switch)
    val estadoCivilWhen = "C"
    when (estadoCivilWhen){
        ("C") ->{
            println("Casado")
        }
        "S" ->{
            println("Soltero")
        }
        else ->{
            println("Ere gay")
        }
    }
    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo =  if (esSoltero) "Si" else "No" //if else tamaño estandar


    calcularSueldo(10.00)
    calcularSueldo(10.00,15.00,20.00)
    //Named Parameters
    //CalcularSueldo(sueldo,tasa,bonoEspecial)
    calcularSueldo(10.00, bonoEspecial = 20.00)
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00)

}


//void ->Unit

fun imprimirNombre(nombre:String): Unit{
    println("Nombre: ${nombre}")//Templete Strings
}

fun calcularSueldo(
    sueldo: Double, //Requerido
    tasa: Double = 12.00, ///Opcional (defecto)
    bonoEspecial: Double? = null  //Opcional (nullable)
    ):Double{
    //Int -> Int? (nullable)
    //Sting -> Sting? (nullable)
    //Date -> Date? (nullable)
    if(bonoEspecial ==null){
        return sueldo * (100/tasa)
    }else{
        return sueldo * (100/tasa) * bonoEspecial
    }

}

abstract class NumerosJava{
    protected val numeroUno:Int
    private val numeroDos:Int
    constructor(
        uno:Int,
        dos:Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializador")
    }
}


abstract class Numeros(//Constructor primario
//Caso 1 Parametro Normal
//uno:Int, (parametro(sin modificador acceso))

//Caso 2 Parametro y propiedad (Atributo) (private)
//private var uno: Int(propiedad "instancia.uno")

protected val numeroUno:Int, //instancia.numeroUno
protected val numeroDos:Int //instancia.numeroDos
//parametroInutil:String, //Parametro
 ){
    init{ //bloque constructor primario (OPCIONAL)
        this.numeroUno
        this.numeroDos
        //this.parametroInutil
        println("Inicializador")
    }
}



class Suma( //Constructor primario
    unoParametro:Int, //Parametro
    dosParametro:Int //Parametro
):Numeros(//Clase papa, Numeros(extendiendo)
    unoParametro,
    dosParametro
){
    public val soyPublicoExplicito:String = "Explicito" //Publicos
    val soyPublicoImplicito: String = "implicito" //Publicos (propiedades, metodos)
    init{
        //this.unoParametro //ERROR no existe
        this.numeroUno //this. OPCIONAL (propiedades, metodos)
        this.numeroDos //this. OPCIONAL (propiedades, metodos)
        numeroUno
        numeroDos
        this.soyPublicoExplicito
        soyPublicoImplicito //this. OPCIONAL (propiedades, metodos)

    }

    //public fun sumar()Int{Modificador "public" es OPCIONAL}
    fun sumar():Int{
        val total = numeroUno + numeroDos
        //Suma.agregarHistorial(total)("Suma." o "NombreClase." es OPCIONAL)
        agregarHistorial(total)
        return total
    }
    companion object{//Comparte entre todos las instancias, similar a static
        //funciones y variables
        val pi=3.14
        fun elevarAlCuadrado(num:Int):Int{
            return num * num
        }
        val historialSumas = arrayListOf<Int>()
        fun agregarHistorial(valorTotalSuma:Int){
            historialSumas.add(valorTotalSuma)
        }
    }
}




