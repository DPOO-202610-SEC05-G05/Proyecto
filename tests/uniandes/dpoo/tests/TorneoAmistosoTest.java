package uniandes.dpoo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import uniandes.dpoo.proyecto.cafe.implementación.*; 

public class TorneoAmistosoTest {
    private Juego juego;
    private Turno turno;
    private TorneoAmistoso torneoAmistoso;
    private Cliente clienteFan;
    private Cliente clienteRegular;

    @BeforeEach
    public void setUp() {
        juego = new Juego(1, "Catan", 1995, "Kosmos", "Estrategia", 3, 4, 10, false, "Disponible", 120000);
        turno = new Turno("Lunes", "08:00", "12:00");
        torneoAmistoso = new TorneoAmistoso(juego, 5, turno, true); 

        clienteFan = new Cliente(1, "JuanFan", "juan@gmail.com", "123", 0, true);
        clienteFan.setJuegosFavoritos("Catan"); 

        clienteRegular = new Cliente(2, "PedroReg", "pedro@gmail.com", "123", 0, true);
    }

    @Test
    public void testReservaCuposFanaticos() {
        Cliente c1 = new Cliente(11, "Cliente1", "c1@gmail.com", "123", 0, true);
        Cliente c2 = new Cliente(12, "Cliente2", "c2@gmail.com", "123", 0, true);
        
        torneoAmistoso.inscribir(c1, 3);
        torneoAmistoso.inscribir(c2, 1);
        
        boolean exitoRegular = torneoAmistoso.inscribir(clienteRegular, 1);
        assertFalse(exitoRegular, "Debería rechazar al cliente regular porque el último cupo es solo para fanáticos.");
        
        boolean exitoFan = torneoAmistoso.inscribir(clienteFan, 1);
        assertTrue(exitoFan, "Debería aceptar al cliente fanático haciendo uso de su cupo reservado.");
    }

    @Test
    public void testPremioTorneoAmistoso() {
        double premio = torneoAmistoso.calcularPremio();
        assertEquals(0.10, premio, "El torneo amistoso debe devolver un bono de descuento del 10% (0.10).");
    }
}