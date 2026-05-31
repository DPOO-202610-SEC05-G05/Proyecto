package uniandes.dpoo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

import uniandes.dpoo.proyecto.cafe.implementación.*; 

public class PedidoTest {
    private Pedido pedido;
    private Mesa mesa;
    private Bebida cerveza;
    private Bebida cafe;
    private Pasteleria torta;
    
    @BeforeEach
    public void setUp() {
        mesa = new Mesa(4);
        pedido = new Pedido(1,LocalDate.now(),mesa,4,true);
        cerveza = new Bebida("Poker",8000,false,true);
        cafe = new Bebida("Capuccino",7000,true,false);
        torta = new Pasteleria("Cheesecake",12000,new ArrayList<>());
    }

    @Test
    public void testAgregarProducto() {

        pedido.agregarProducto(cafe);

        assertEquals(1, pedido.getProductos().size());
        assertTrue(pedido.getProductos().contains(cafe));
    }

    @Test
    public void testAgregarBebidaAlcoholicaConMenores() {
        mesa.setHayMenores(true);
        pedido.agregarProducto(cerveza);
        assertEquals(0, pedido.getProductos().size(), "No debería agregarse alcohol a mesas con menores. Jugito Hit para los nenes");
    }

    @Test
    public void testEliminarProducto() {
        pedido.agregarProducto(cafe);
        pedido.eliminarProducto(cafe);
        assertEquals(0, pedido.getProductos().size());
    }

    @Test
    public void testCalcularTotal() {
        pedido.agregarProducto(cafe);
        pedido.agregarProducto(torta);
        double total = pedido.calcularTotal();
        assertEquals(19000, total);
    }

    @Test
    public void testTerminarPedido() {
        pedido.terminarPedido();
        assertTrue(pedido.estaTerminado());
    }

     @Test
    public void testNoAgregarProductoAPedidoTerminado() {
        pedido.terminarPedido();
        pedido.agregarProducto(cafe);
        assertEquals(0, pedido.getProductos().size(), "No deberían agregarse productos cuando terminó el pedido.");
    }

}