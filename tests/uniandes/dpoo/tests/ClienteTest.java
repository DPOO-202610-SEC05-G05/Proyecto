package uniandes.dpoo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import uniandes.dpoo.proyecto.cafe.implementación.*; 

public class ClienteTest {
    private Cliente cliente;
    private Mesa mesa;
    private Juego juego;
    private Prestamo prestamo;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta inventarioVenta;
    private Bebida cerveza;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente(1,"Juan","juan@gmail.com","123",0,true);
        mesa = new Mesa(4);
        juego = new Juego(1,"Catan",1995,"Kosmos","Estrategia",3,4,10,false,"Disponible",120000);
        prestamo = new Prestamo(1,null,"pendiente",null);
        inventarioPrestamo = new InventarioPrestamo(juego,3,3,"Disponible");
        inventarioVenta = new InventarioVenta(juego,2,"Disponible");
        cerveza = new Bebida("Poker",8000,false,true);
    }

    @Test
    public void testReservarMesaExitosa() {
        cliente.reservarMesa(mesa, 4, false);
        assertTrue(mesa.getOcupada());
        assertEquals(4, mesa.getNumPersonas());
    }

    @Test
    public void testReservarJuegosExitoso() {
        cliente.reservarMesa(mesa, 4, false);
        cliente.reservarJuegos(juego,mesa,prestamo,  inventarioPrestamo);
        assertEquals(2, inventarioPrestamo.getCantidadDisponible());
        assertTrue(mesa.getJuegosEnMesa().contains("Catan"));
        assertEquals("activo", prestamo.getEstado());
    }

    @Test
    public void testJuegosDisponibles() {
        List<InventarioPrestamo> inventarios = new ArrayList<>();
        inventarios.add(inventarioPrestamo);
        List<String> disponibles = cliente.juegoDisponibles(inventarios);
        assertEquals(1, disponibles.size());
        assertTrue(disponibles.contains("Catan"));
    }

    @Test
    public void testRegresarJuego() {
        cliente.reservarMesa(mesa, 4, false);
        cliente.reservarJuegos(juego,mesa,prestamo,inventarioPrestamo);
        cliente.regresarJuego(juego,mesa,prestamo,inventarioPrestamo);
        assertEquals(3,inventarioPrestamo.getCantidadDisponible());
        assertFalse(mesa.getJuegosEnMesa().contains("Catan"));
        assertEquals("finalizado",prestamo.getEstado());
    }

    @Test
    public void testComprarJuegoExitoso() {
        VentaJuego venta = cliente.comprarJuego(juego,1,inventarioVenta);
        assertTrue(venta.isValida());
        assertEquals(1,inventarioVenta.getCantidadTotal());
        assertEquals(1,cliente.getHistorialDeCompras().size());
    }

    @Test
    public void testComprarJuegoSinInventario() {
        inventarioVenta.setCantidadTotal(0);
        VentaJuego venta = cliente.comprarJuego(juego,1,inventarioVenta);
        assertEquals(0,venta.getSubtotal());
        assertEquals(0,venta.getTotal());
    }

    @Test
    public void testComprarProductoConAlcoholYMenores() {
        mesa.setHayMenores(true);
        VentaCafeteria venta = cliente.comprarProducto(cerveza,mesa,1);
        assertEquals(0,venta.getSubtotal());
        assertEquals(0,venta.getTotal());
    }

    @Test
    public void testComprarProductoExitoso() {
        mesa.setHayMenores(false);
        VentaCafeteria venta = cliente.comprarProducto(cerveza,mesa,1);
        assertTrue(venta.isValida());
        assertEquals(1, cliente.getHistorialDeCompras().size());
    }

}