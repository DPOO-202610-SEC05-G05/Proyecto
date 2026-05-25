package uniandes.dpoo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import uniandes.dpoo.proyecto.cafe.*; 

public class TurnoTest {
    private Turno turno;
    private Mesero mesero1;
    private Mesero mesero2;
    private Cocinero cocinero;

    @BeforeEach
    public void setUp() {
        turno = new Turno("Lunes","08:00","16:00");
        mesero1 = new Mesero(1,"Carlos","carlos@gmail.com","123",turno,new ArrayList<>(),new ArrayList<>(),true);
        mesero2 = new Mesero(2,"Pedro","pedro@gmail.com","123",turno,new ArrayList<>(),new ArrayList<>(),true);
        cocinero = new Cocinero(3,"Mario","Mario@gmail.com","123",turno,true);
    }

    @Test
    public void testAgregarEmpleado() {
        turno.agregarEmpleado(mesero1);
        assertEquals(1, turno.getEmpleados().size());
        assertTrue(turno.getEmpleados().contains(mesero1));
    }

    @Test
    public void testGetIdsEmpleados() {
        turno.agregarEmpleado(mesero1);
        turno.agregarEmpleado(cocinero);
        assertTrue(turno.getIdsEmpleados().contains(1));
        assertTrue(turno.getIdsEmpleados().contains(3));
    }

    @Test
    public void testValidarFuncionamientoCafeTrue() {
        turno.agregarEmpleado(mesero1);
        turno.agregarEmpleado(mesero2);
        turno.agregarEmpleado(cocinero);
        assertTrue(turno.validarFuncionamientoCafe(),"Debe haber al menos 1 cocinero y 2 meseros.");
    }

    @Test
    public void testValidarFuncionamientoCafeFalseSinCocinero() {
        turno.agregarEmpleado(mesero1);
        turno.agregarEmpleado(mesero2);
        assertFalse(turno.validarFuncionamientoCafe(), "No debería funcionar sin cocinero.");
    }

    @Test
    public void testValidarFuncionamientoCafeFalseSinMeseros() {
        turno.agregarEmpleado(cocinero);
        assertFalse(turno.validarFuncionamientoCafe(), "No debería funcionar sin suficientes meseros.");
    }
}