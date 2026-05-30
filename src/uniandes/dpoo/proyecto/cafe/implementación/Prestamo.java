package uniandes.dpoo.proyecto.cafe;

import java.time.LocalDate;

public class Prestamo {
    private int id;
    private LocalDate fecha;
    private String estado;
    private LocalDate fechaDevolucion;

    public Prestamo(int id, LocalDate fecha, String estado, LocalDate fechaDevolucion) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.fechaDevolucion = fechaDevolucion;
    }

    public int getId(){
        return id; }

    public LocalDate getFecha(){ 
        return fecha; }

    public String getEstado(){ 
        return estado; }

    public LocalDate getFechaDevolucion(){ 
        return fechaDevolucion; }

    public void registrarPrestamo(Juego juego){
        this.fecha = LocalDate.now();
        this.estado ="activo";
        this.fechaDevolucion = null;
    }

    public void finalizarPrestamo(){
        this.estado = "finalizado";
        this.fechaDevolucion = LocalDate.now();
    }

    public boolean esAptoJugadores(Mesa mesa, Juego juego) {
        int personas = mesa.getNumPersonas();
        return personas >= juego.getMinJugadores() && personas <= juego.getMaxJugadores();
    }

    public boolean esAptoEdad(Mesa mesa, Juego juego) {
        if (mesa.getHayMenores()) {
            return juego.getEdadMinima() < 18;
        }
        return true;}
}