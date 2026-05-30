package uniandes.dpoo.proyecto.cafe;

public abstract class ProductoMenu {
    protected String nombre;
    protected double precio;

    public ProductoMenu(String nombre, double precio){
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre(){
        return nombre;
    }

    public double getPrecio(){
        return precio;
    }

    public void setPrecio(double newDouble){
        this.precio = newDouble;
    }
}
