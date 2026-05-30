package uniandes.dpoo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import uniandes.dpoo.proyecto.cafe.implementación.*; 

public class GestorPersistenciaTest {
    private GestorPersistencia gestor;
    private Cliente cliente;
    private Mesero mesero;
    private Cocinero cocinero;
    private Administrador admin;
    private Juego juego;
    private InventarioVenta inventarioVenta;
    private InventarioPrestamo inventarioPrestamo;

    @BeforeEach
    public void setUp() {
        gestor = new GestorPersistencia();
        cliente = new Cliente(1,"Juan","juan@gmail.com","123",10,true);
        mesero = new Mesero(2,"Carlos","carlos@gmail.com","123",null,new ArrayList<>(),new ArrayList<>(),true);
        cocinero = new Cocinero(3,"Mario","mario@gmail.com","123",null,true);
        admin = new Administrador(4,"Admin","admin@gmail.com","123",true);
        juego = new Juego(1,"Catan",1995,"Kosmos","Estrategia",3,4,10,false,"Disponible",120000);
        inventarioVenta = new InventarioVenta(juego,3,"Disponible");
        inventarioPrestamo = new InventarioPrestamo(juego,2,3,"Disponible");
    }

    @Test
    public void testGuardarYCargarUsuarios() throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(cliente);
        usuarios.add(mesero);
        usuarios.add(cocinero);
        usuarios.add(admin);
        gestor.guardarUsuarios(usuarios);
        List<Usuario> cargados = gestor.cargarUsuarios();
        assertEquals(4, cargados.size());
    }

    @Test
    public void testGuardarYCargarInventarioVenta() throws IOException {
        List<InventarioVenta> inventarios = new ArrayList<>();
        inventarios.add(inventarioVenta);
        gestor.guardarInventarioVenta(inventarios);
        List<InventarioVenta> cargados = gestor.cargarInventarioVenta();
        assertEquals(1, cargados.size());
        assertEquals("Catan", cargados.get(0).getJuego().getNombre());
    }

    @Test
    public void testGuardarYCargarInventarioPrestamo() throws IOException {
        List<InventarioPrestamo> inventarios = new ArrayList<>();
        inventarios.add(inventarioPrestamo);
        gestor.guardarInventarioPrestamo(inventarios);
        List<InventarioPrestamo> cargados = gestor.cargarInventarioPrestamo();
        assertEquals(1, cargados.size());
        assertEquals(2,cargados.get(0).getCantidadDisponible());
    }

    @Test
    public void testGuardarYCargarVentas() throws IOException {
        List<Venta> ventas = new ArrayList<>();
        VentaJuego venta = new VentaJuego(1,LocalDate.now(),100000,10000,cliente,true);
        ventas.add(venta);
        gestor.guardarVentas(ventas);
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(cliente);
        List<Venta> cargadas = gestor.cargarVentas(usuarios);
        assertEquals(1, cargadas.size());
        assertTrue(cargadas.get(0) instanceof VentaJuego);
    }

    @Test
    public void testGuardarYCargarPrestamos() throws IOException {
        List<Prestamo> prestamos = new ArrayList<>();
        Prestamo prestamo = new Prestamo(1,LocalDate.now(),"activo",null);
        prestamos.add(prestamo);
        gestor.guardarPrestamos(prestamos);
        List<Prestamo> cargados =gestor.cargarPrestamos();
        assertEquals(1, cargados.size());
        assertEquals("activo",cargados.get(0).getEstado());
    }
}
