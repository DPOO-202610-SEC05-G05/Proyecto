package uniandes.dpoo.proyecto.cafe;

import java.util.List;
import java.util.ArrayList;

public class Mesero extends Empleado {
    private List<String> juegosConocidos;
    private List<String> juegosFavoritos;

    public Mesero(int id, String username, String email, String password, Turno turno, List<String> juegosFavoritos, List<String> juegosConocidos, boolean esValido){
        super(id, username, email, password, turno, esValido);
        this.juegosFavoritos = juegosFavoritos != null ? juegosFavoritos : new ArrayList<>();
        this.juegosConocidos = juegosConocidos != null ? juegosConocidos : new ArrayList<>();
    }

    public List<String> getJuegosConocidos(){ return juegosConocidos; }
    public List<String> getJuegosFavoritos(){ return juegosFavoritos; }

    public void setJuegosConocidos(String juego){ this.juegosConocidos.add(juego); }
    public void setJuegosFavoritos(String juego){ this.juegosFavoritos.add(juego); }

    public void reservarJuegos(Juego juego, Prestamo prestamo, Turno turnoActual, int clientesPorAtender, InventarioPrestamo inventarioPrestamo){
        boolean trabajando = enTurno(turnoActual);
        
        if (trabajando && clientesPorAtender > 0) {
            System.out.println("Préstamo denegado: Estás en turno y hay clientes por atender.");
        }
        
        if (inventarioPrestamo.getCantidadDisponible() <= 0) {
            System.out.println("El juego " + juego.getNombre() + " no tiene copias disponibles.");
        }
        
        inventarioPrestamo.setCantidadDisponible(inventarioPrestamo.getCantidadDisponible() - 1);
        prestamo.registrarPrestamo(juego);
        System.out.println("Juego '" + juego.getNombre() + "' prestado exitosamente al mesero.");
    }

    public void regresarJuego(Juego juego, Prestamo prestamo, InventarioPrestamo inventarioPrestamo){
        inventarioPrestamo.setCantidadDisponible(inventarioPrestamo.getCantidadDisponible() + 1);
        prestamo.finalizarPrestamo();
        System.out.println("El mesero ha devuelto el juego correctamente.");
    }

    public boolean puedeEnsenarJuego(Juego juego) {
        return juegosConocidos.contains(juego.getNombre());
    }
}