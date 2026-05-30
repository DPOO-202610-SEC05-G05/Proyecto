package uniandes.dpoo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import uniandes.dpoo.proyecto.cafe.implementación.*; 

public class MeseroTest {

    private Mesero mesero;
    private Turno turno;
    private Juego juego;
    private Prestamo prestamo;
    private InventarioPrestamo inventarioPrestamo;

    @BeforeEach
    public void setUp() {
        turno = new Turno("Lunes","08:00","16:00");
        mesero = new Mesero(1,"Carlos","carlos@gmail.com","123",turno,new ArrayList<>(),new ArrayList<>(),true);
        juego = new Juego(1,"Uno",1990,"Mattel","Cartas",2,10,7,false,"Disponible",40000);
        prestamo = new Prestamo(1,null,"pendiente",null);
        inventarioPrestamo = new InventarioPrestamo(juego,3,3,"Disponible");
    }

    @Test
    public void testReservarJuego() {
        Turno turnoDiferente = new Turno("Martes","10:00","18:00");
        mesero.reservarJuegos(juego,prestamo,turnoDiferente,0,inventarioPrestamo);
        assertEquals(2,inventarioPrestamo.getCantidadDisponible());
        assertEquals("activo",prestamo.getEstado());
    }

    @Test
    public void testRegresarJuego() {
        mesero.regresarJuego(juego,prestamo,inventarioPrestamo);
        assertEquals(4,inventarioPrestamo.getCantidadDisponible());
        assertEquals("finalizado",prestamo.getEstado());
    }

    @Test
    public void testPuedeEnsenarJuegoTrue() {
        mesero.setJuegosConocidos("Uno");
        assertTrue(mesero.puedeEnsenarJuego(juego));
    }

    @Test
    public void testPuedeEnsenarJuegoFalse() {
        assertFalse(mesero.puedeEnsenarJuego(juego));
    }

    @Test
    public void testAgregarJuegoFavorito() {
        mesero.setJuegosFavoritos("Uno");
        assertTrue(mesero.getJuegosFavoritos().contains("Uno"));
    }

    @Test
    public void testEnTurnoTrue() {
        assertTrue(mesero.enTurno(turno));
    }

    @Test
    public void testEnTurnoFalse() {
        Turno diferente = new Turno("Viernes","12:00","20:00");
        assertFalse(mesero.enTurno(diferente));
    }  
}