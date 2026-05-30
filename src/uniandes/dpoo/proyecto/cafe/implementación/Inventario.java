package uniandes.dpoo.proyecto.cafe;

public abstract class Inventario {
    protected Juego juego;
    protected int cantidadTotal;
    protected String estado;

    public Inventario(Juego juego, int cantidadTotal, String estado){
        this.juego = juego;
        this.cantidadTotal = cantidadTotal;
        this.estado = estado;
    }
    public Juego getJuego(){
        return juego;
    }

    public int getCantidadTotal(){
        return cantidadTotal;
    }

    public String getEstado(){
        return estado;
    }

    public void setEstado(String newEstado){
        this.estado = newEstado;
    }

    public void setCantidadTotal(int newInt){
        this.cantidadTotal = newInt;
    }
}
