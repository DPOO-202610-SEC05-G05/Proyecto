package uniandes.dpoo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import uniandes.dpoo.proyecto.cafe.*; 

public class TorneoCompetitivoTest {
    private Juego juego;
    private Turno turno;
    private Turno turnoDiferente;
    private TorneoCompetitivo torneoCompetitivo;
    private Cliente clienteRegular;
    private Empleado empleado;

    @BeforeEach
    public void setUp() {
        juego = new Juego(1, "Catan", 1995, "Kosmos", "Estrategia", 3, 4, 10, false, "Disponible", 120000);
        turno = new Turno("Lunes", "08:00", "12:00");
        turnoDiferente = new Turno("Martes", "14:00", "18:00");
        
        torneoCompetitivo = new TorneoCompetitivo(juego, 10, turno, true);
        
        clienteRegular = new Cliente(2, "PedroReg", "pedro@gmail.com", "123", 0, true);
        empleado = new Empleado(3, "AnaEmp", "ana@gmail.com", "123", turno, true) {};
    }

    @Test
    public void testInscripcionExitosa() {
        boolean exito = torneoCompetitivo.inscribir(clienteRegular, 2);
        assertTrue(exito, "El cliente debería poder inscribirse tomando 2 cupos.");
        assertEquals(2, torneoCompetitivo.totalCuposTomados());
    }

    @Test
    public void testLimiteMaximoTresCupos() {
        boolean exito1 = torneoCompetitivo.inscribir(clienteRegular, 2);
        assertTrue(exito1);
        boolean exito2 = torneoCompetitivo.inscribir(clienteRegular, 2); 
        
        assertFalse(exito2, "El sistema no debería permitir que un usuario reserve más de 3 cupos.");
        assertEquals(2, torneoCompetitivo.totalCuposTomados(), "Los cupos totales no deberían haber aumentado tras el fallo.");
    }

    @Test
    public void testEmpleadoEnTurnoNoPuedeInscribirse() {
        boolean exito = torneoCompetitivo.inscribir(empleado, 1);
        assertFalse(exito, "Un empleado no puede inscribirse si su turno coincide con el del torneo.");
    }

    @Test
    public void testEmpleadoLibrePuedeInscribirse() {
        empleado.setTurno(turnoDiferente);
        boolean exito = torneoCompetitivo.inscribir(empleado, 1);
        assertTrue(exito, "El empleado debería poder inscribirse si NO está en turno.");
    }

    @Test
    public void testRecaudoYPremioTorneoCompetitivo() {
        torneoCompetitivo.inscribir(clienteRegular, 2);
        
        empleado.setTurno(turnoDiferente);
        torneoCompetitivo.inscribir(empleado, 1);

        double recaudoEsperado = 20000 * 2; 
        double premioEsperado = recaudoEsperado * 0.70; 

        assertEquals(premioEsperado, torneoCompetitivo.calcularPremio(), "El premio debe ser el 70% de las entradas pagadas (excluyendo empleados).");
    }

    @Test
    public void testDesinscripcionEliminaTodosLosCupos() {
        torneoCompetitivo.inscribir(clienteRegular, 2);
        assertEquals(2, torneoCompetitivo.totalCuposTomados());
        
        boolean exito = torneoCompetitivo.desinscribir(clienteRegular);
        
        assertTrue(exito, "La desinscripción debería ser exitosa.");
        assertEquals(0, torneoCompetitivo.totalCuposTomados(), "Al desinscribirse, se deben liberar TODOS los cupos tomados por el usuario.");
    }

    @Test
    public void testDesinscripcionUsuarioNoInscrito() {
        boolean exito = torneoCompetitivo.desinscribir(clienteRegular);
        assertFalse(exito, "Debería fallar al intentar desinscribir un usuario que no tiene cupos en el torneo.");
    }
}