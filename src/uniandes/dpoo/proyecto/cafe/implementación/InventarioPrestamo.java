package uniandes.dpoo.proyecto.cafe.implementación;
<<<<<<< HEAD
=======

>>>>>>> b91631047a9b8999e30f38d6077c05492653beb9
public class InventarioPrestamo extends Inventario{
    private int cantidadDisponible;

    public InventarioPrestamo(Juego juego, int cantidadDisponible, int cantidadTotal, String estado){
        super(juego, cantidadTotal, estado);
        this.cantidadDisponible = cantidadDisponible;
    }

    public int getCantidadDisponible(){
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int newInt){
        this.cantidadDisponible = newInt;
    }
}
