package uniandes.dpoo.proyecto.cafe;
public class Bebida extends ProductoMenu {
    private boolean esCaliente;
    private boolean esAlcoholica;

    public Bebida(String nombre, double precio, boolean esCaliente, boolean esAlcoholica){
        super(nombre, precio);
        this.esCaliente = esCaliente;
        this.esAlcoholica = esAlcoholica;
    }

    public boolean getEsCaliente(){
        return esCaliente;
    }

    public boolean getEsAlcoholica(){
        return esAlcoholica;
    }

    public boolean esRestringible(boolean esCaliente, boolean esAlcoholica){
        return esCaliente || esAlcoholica;
    }
}
