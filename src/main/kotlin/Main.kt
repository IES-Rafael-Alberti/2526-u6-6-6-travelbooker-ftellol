import java.util.UUID
import java.time.LocalDate

abstract class Reserva(val id: String, val fechaCreacion: String, var descripcion: String) {
    open val detalle: String
        get() = "$id - $descripcion"
}

class ReservaVuelo private constructor(id: String, fechaCreacion: String, descripcion: String, val origen: String, val destino: String, var horaVuelo: String) : Reserva(id, fechaCreacion, descripcion) {
    override val detalle: String
        get() = "$id - $descripcion - $origen -> $destino [$horaVuelo]"

    companion object {
        fun creaInstancia(descripcion: String, origen: String, destino: String, horaVuelo: String): ReservaVuelo {
            val id = UUID.randomUUID().toString().substring(0, 8)
            val fechaCreacion = LocalDate.now().toString()

            return ReservaVuelo(id, fechaCreacion, descripcion, origen, destino, horaVuelo)
        }
    }

    override fun toString(): String {
        return "La reserva con id: $id creada $fechaCreacion tiene como descripcion: $descripcion, el vuelo sale desde $origen y su llegada es en $destino. La hora del vuelo es $horaVuelo"
    }
}

class ReservaHotel private constructor(id: String, fechaCreacion: String, descripcion: String, val ubicacion: String, var numeroNoches: Int) : Reserva(id, fechaCreacion, descripcion) {
    override val detalle: String
        get() = "$id - $descripcion - $ubicacion ($numeroNoches)"

    companion object {
        fun creaInstancia(descripcion: String, ubicacion: String, numeroNoches: Int): ReservaHotel {
            val id = UUID.randomUUID().toString().substring(0, 8)
            val fechaCreacion = LocalDate.now().toString()

            return ReservaHotel(id, fechaCreacion, descripcion, ubicacion, numeroNoches)
        }
    }

    override fun toString(): String {
        return "La reserva con id: $id creada $fechaCreacion tiene como descripcion: $descripcion, su ubicacion esta en: $ubicacion y cuenta con $numeroNoches noches"
    }
}

interface IReservaRepository {
    fun agregar(reserva: Reserva)
    fun obtenerTodas(): List<Reserva>
}

class ReservaRepositoryMemoria : IReservaRepository {
    private val lista = mutableListOf<Reserva>()
    override fun agregar(reserva: Reserva) {
        lista.add(reserva)
    }
    override fun obtenerTodas(): List<Reserva> {
        return lista
    }
}

class ReservaService(private val repository: IReservaRepository) {
    fun crearReservaVuelo(descripcion: String, origen: String, destino: String, horaVuelo: String) {
        val reserva = ReservaVuelo.creaInstancia(descripcion, origen, destino, horaVuelo)
        repository.agregar(reserva)
        println ("Reserva de vuelo creada: ${reserva.detalle}")
    }

    fun crearReservaHotel(descripcion: String, ubicacion: String, numeroNoches: Int) {
        val reserva = ReservaHotel.creaInstancia(descripcion, ubicacion, numeroNoches)
        repository.agregar(reserva)
        println ("Reserva de hotel creada ${reserva.detalle}")
    }

    fun todasLasReservas() {
        val reservas = repository.obtenerTodas()
        if (reservas.isEmpty()) {
            println ("No hay reservas")
        } else {
            reservas.forEach { println(it.detalle) }
        }
    }
}

fun main() {
    val repositorio = ReservaRepositoryMemoria()
    val servicioReserva = ReservaService(repositorio)

    while (true) {
        println("=========================")
        println("MENU DE RESERVAS")
        println("=========================")
        println("1. Crear reserva de vuelo")
        println("2. Crear reserva de hotel")
        println("3. Mostrar todas las reservas")
        println("4. Salir")
        print("Elige una opción: ")

        when (readLine()?.trim()) {
            "1" -> {
                print("Descripción del vuelo: ")
                val descripcion = readLine() ?: ""
                print("Origen: ")
                val origen = readLine() ?: ""
                print("Destino: ")
                val destino = readLine() ?: ""
                print("Hora del vuelo: ")
                val horaVuelo = readLine() ?: ""
                servicioReserva.crearReservaVuelo(descripcion, origen, destino, horaVuelo)
            }

            "2" -> {
                print("Descripción del hotel: ")
                val descripcion = readLine() ?: ""
                print("Ubicación: ")
                val ubicacion = readLine() ?: ""
                print("Número de noches: ")
                val numeroNoches = readLine()?.toIntOrNull() ?: 1
                servicioReserva.crearReservaHotel(descripcion, ubicacion, numeroNoches)
            }

            "3" -> servicioReserva.todasLasReservas()

            "4" -> {
                println("Vuelva pronto a reservas Manolo")
                return
            }

            else -> println("Opción no válida, intenta de nuevo.")
        }
    }
}
