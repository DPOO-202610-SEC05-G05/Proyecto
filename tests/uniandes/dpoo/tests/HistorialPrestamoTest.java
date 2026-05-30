package uniandes.dpoo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import uniandes.dpoo.proyecto.cafe.implementación.*; 

public class HistorialPrestamoTest {
    private HistorialPrestamo historial;
    private Prestamo prestamo1;
    private Prestamo prestamo2;
    private Prestamo prestamo3;

    @BeforeEach
    public void setUp() {
        historial = new HistorialPrestamo();
        prestamo1 = new Prestamo(1,LocalDate.of(2025, 1, 10),"activo",null);
        prestamo2 = new Prestamo(2,LocalDate.of(2025, 2, 15),"finalizado",LocalDate.of(2025, 2, 20));
        prestamo3 = new Prestamo(3,LocalDate.of(2025, 3, 1),"activo",null);
    }

    @Test
    public void testRegistrarPrestamo() {
        historial.registrarPrestamo(prestamo1);
        assertEquals(1, historial.getPrestamos().size());
        assertTrue( historial.getPrestamos().contains(prestamo1));
    }

    @Test
    public void testConsultarActivos() {
        historial.registrarPrestamo(prestamo1);
        historial.registrarPrestamo(prestamo2);
        historial.registrarPrestamo(prestamo3);
        List<Prestamo> activos = historial.consultarActivos();
        assertEquals(2, activos.size());
        assertTrue(activos.contains(prestamo1));
        assertTrue(activos.contains(prestamo3));
    }

    @Test
    public void testConsultarPorFecha() {
        historial.registrarPrestamo(prestamo1);
        historial.registrarPrestamo(prestamo2);
        historial.registrarPrestamo(prestamo3);
        List<Prestamo> resultado = historial.consultarPorFecha(LocalDate.of(2025, 1, 1),LocalDate.of(2025, 2, 28));
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(prestamo1));
        assertTrue(resultado.contains(prestamo2));
    }

    @Test
    public void testAgregarRegistroDesdeHistorialAbstracto() {
        historial.agregarRegistro(prestamo1);
        assertEquals(1, historial.getRegistros().size());
    }
}