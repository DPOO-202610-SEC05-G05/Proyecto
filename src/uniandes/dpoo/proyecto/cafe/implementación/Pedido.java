package uniandes.dpoo.proyecto.cafe;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Pedido {
    private int id;
    private LocalDate fecha;
    private Mesa mesa;
    private List<ProductoMenu> productos;
    private boolean terminado;
    private int cantidadPersonas;
    private boolean esValido;

    public Pedido(int id, LocalDate fecha, Mesa mesa, int cantidadPersonas, boolean esValido){
        this.id = id;
        this.fecha = fecha;
        this.mesa = mesa;
        this.productos = new ArrayList<>();
        this.terminado = false;
        this.cantidadPersonas = cantidadPersonas;
        this.esValido = esValido;
    }

    public int getId(){
        return id;
    }

    public LocalDate getFecha(){
        return fecha;
    }

    public Mesa getMesa(){
        return mesa;
    }

    public List<ProductoMenu> getProductos(){
        return productos;
    }

    public int getCantidadPersonas(){
        return cantidadPersonas;
    }

    public void setCantidadPersonas(int personas){
        this.cantidadPersonas = personas;
    }

    public boolean estaTerminado(){
        return terminado;
    }
    
    public boolean esValido() {
        return esValido;
    }

    public void agregarProducto(ProductoMenu producto){
        if (terminado){
            System.out.println("No se pueden agregar productos a un pedido terminado.");
            return;
        }

        if (producto == null){
            System.out.println("No se recibió ningún producto.");
            return;
        }

        if (producto instanceof Bebida){
            Bebida bebida = (Bebida) producto;

            if (mesa != null && mesa.getHayMenores() && bebida.getEsAlcoholica()){
                System.out.println("No se puede agregar una bebida alcohólica a una mesa con menores.");
                return;
            }
        }

        if (producto instanceof Pasteleria){
            Pasteleria p = (Pasteleria) producto;

            if (p.getAlergenos() != null && !p.getAlergenos().isEmpty()){
                System.out.println("Advertencia: contiene alérgenos: " + p.getAlergenos());
            }
        }

        productos.add(producto);
        System.out.println("Producto agregado correctamente.");
    }

    public void eliminarProducto(ProductoMenu producto){
        if (terminado){
            System.out.println("No se pueden eliminar productos de un pedido terminado.");
        }

        if (producto == null){
            System.out.println("No se recibió ningún producto.");
        }

        if (productos.remove(producto)){
            System.out.println("Producto eliminado correctamente.");
        } else{
            System.out.println("El producto no estaba en el pedido.");
        }
    }

    public double calcularTotal() {
        double total = 0;

        for (ProductoMenu p : productos) {
            total += p.getPrecio();
        }

        return total;
    }

    public void terminarPedido(){
        terminado = true;
        System.out.println("Pedido terminado.");
    }
}