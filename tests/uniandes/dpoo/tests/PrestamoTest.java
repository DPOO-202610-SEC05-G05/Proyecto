package uniandes.dpoo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import uniandes.dpoo.proyecto.cafe.implementación.*; 

public class PrestamoTest {
    private Prestamo prestamo;
    private Mesa mesa;
    private Juego juegoApto;
    private Juego juegoNoApto;      //para menores :P

    @BeforeEach
    public void setUp() {
        prestamo = new Prestamo(1, LocalDate.of(2025, 1, 1), "pendiente", null);

        mesa = new Mesa(4);
        mesa.setNumPersonas(4);
        mesa.setHayMenores(false);

        juegoApto = new Juego(1,"Catan",1995,"Kosmos","Estrategia",3,4,10,false,"Disponible",120000);
        juegoNoApto = new Juego(2,"Poker Extremo",2020,"Casino","Cartas",2,8,18,false,"Disponible",50000);
    }

    @Test
    public void testRegistrarPrestamo() {
        prestamo.registrarPrestamo(juegoApto);
        assertEquals("activo", prestamo.getEstado());
        assertEquals(LocalDate.now(), prestamo.getFecha());
        assertNull(prestamo.getFechaDevolucion());
    }

    @Test
    public void testFinalizarPrestamo() {
        prestamo.finalizarPrestamo();
        assertEquals("finalizado", prestamo.getEstado());
        assertEquals(LocalDate.now(), prestamo.getFechaDevolucion());
    }

    @Test
    public void testEsAptoJugadoresTrue() {
        boolean resultado = prestamo.esAptoJugadores(mesa, juegoApto);
        assertTrue(resultado, "La mesa cumple el # permitido de jugadores.");
    }

    @Test
    public void testEsAptoJugadoresFalse() {
        mesa.setNumPersonas(10);
        boolean resultado = prestamo.esAptoJugadores(mesa, juegoApto);
        assertFalse(resultado, "La mesa pasa el máximo de jugadores permitido.");
    }

    @Test
    public void testEsAptoEdadTrueSinMenores() {
        boolean resultado = prestamo.esAptoEdad(mesa, juegoNoApto);
        assertTrue(resultado, "Si no hay menores, cualquier juego debería permitirse.");
    }

    @Test
    public void testEsAptoEdadFalseConMenores() {
        mesa.setHayMenores(true);
        boolean resultado = prestamo.esAptoEdad(mesa, juegoNoApto);
        assertFalse(resultado, "Un juego +18 no debería permitirse con menores.");
    }
}