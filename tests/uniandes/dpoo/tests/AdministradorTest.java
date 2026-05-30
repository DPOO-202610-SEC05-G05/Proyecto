package uniandes.dpoo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import uniandes.dpoo.proyecto.cafe.implementación.*; 

public class AdministradorTest {

    private Administrador administrador;

    private Juego juego;

    private InventarioPrestamo inventarioPrestamo;

    private InventarioVenta inventarioVenta;

    private HistorialPrestamo historialPrestamo;

    @BeforeEach
    public void setUp() {
        administrador = new Administrador(1,"admin","admin@gmail.com","123",true);
        juego = new Juego(1,"Risk",1959,"Hasbro","Estrategia",2,6,10,false,"Disponible",90000);
        inventarioPrestamo = new InventarioPrestamo(juego,2,2,"Disponible");
        inventarioVenta = new InventarioVenta(juego,3,"Disponible");
        historialPrestamo = new HistorialPrestamo();
    }

    @Test
    public void testGetEstadoJuego() {
        String estado = administrador.getEstadoJuego(juego);
        assertEquals("Disponible", estado);
    }

    @Test
    public void testSetEstadoJuego() {
        administrador.setEstadoJuego(inventarioPrestamo,"Dañado");
        assertEquals("Dañado",inventarioPrestamo.getEstado());
    }

    @Test
    public void testAgregarJuegoPrestamo() {
        administrador.agregarJuegoPrestamo(inventarioPrestamo,3);
        assertEquals(5,inventarioPrestamo.getCantidadTotal());
        assertEquals(5,inventarioPrestamo.getCantidadDisponible());
    }

    @Test
    public void testAgregarJuegoVenta() {
        administrador.agregarJuegoVenta(inventarioVenta,2);
        assertEquals(5,inventarioVenta.getCantidadTotal());
    }

    @Test
    public void testVerHistorialPrestamo() {
        Prestamo prestamo = new Prestamo(1,null,"activo",null);
        historialPrestamo.registrarPrestamo(prestamo);
        List<Prestamo> prestamos =administrador.verHistorialPrestamo(historialPrestamo);
        assertEquals(1, prestamos.size());
    }

    @Test
    public void testRepararJuego() {
        administrador.repararJuego(inventarioVenta,inventarioPrestamo);
        assertEquals(2,inventarioVenta.getCantidadTotal());
        assertEquals(3,inventarioPrestamo.getCantidadTotal());
        assertEquals(3,inventarioPrestamo.getCantidadDisponible());
        assertEquals("Nuevo",inventarioPrestamo.getEstado());
    }

    @Test
    public void testAprobarSugerenciaPlatillo() {
        boolean resultado = administrador.aprobarSugerenciaPlatillo("Hamburguesa");
        assertTrue(resultado);
    }
}