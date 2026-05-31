package uniandes.dpoo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import uniandes.dpoo.proyecto.cafe.implementación.*; 

public class HistorialVentaTest {
    private HistorialVenta historial;
    private Cliente cliente;
    private VentaJuego ventaJuego;
    private VentaCafeteria ventaCafe;

    @BeforeEach
    public void setUp() {
        historial = new HistorialVenta();
        cliente = new Cliente(1,"Juan","juan@gmail.com","123",0,true);
        ventaJuego = new VentaJuego(1,LocalDate.of(2025, 1, 10),100000,10000,cliente,true);
        ventaCafe = new VentaCafeteria(2,LocalDate.of(2025, 2, 15),50000,cliente,true);
    }

    @Test
    public void testRegistrarVenta() {
        historial.registrarVenta(ventaJuego);
        assertEquals(1, historial.getVentas().size());
        assertTrue(historial.getVentas().contains(ventaJuego));
    }

    @Test
    public void testCalcularTotalVentas() {
        historial.registrarVenta(ventaJuego);
        historial.registrarVenta(ventaCafe);
        double total = historial.calcularTotalVentas();
        double esperado = ventaJuego.getTotal() + ventaCafe.getTotal();
        assertEquals(esperado, total);
    }

    @Test
    public void testConsultarPorFecha() {
        historial.registrarVenta(ventaJuego);
        historial.registrarVenta(ventaCafe);
        List<Venta> resultado = historial.consultarPorFecha(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(ventaJuego));
    }

    @Test
    public void testAgregarRegistroDesdeHistorialAbstracto() {
        historial.agregarRegistro(ventaJuego);
        assertEquals(1, historial.getRegistros().size());
    }
}