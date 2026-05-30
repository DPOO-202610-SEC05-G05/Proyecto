package uniandes.dpoo.proyecto.cafe.implementación;
import java.time.LocalDate;
import java.util.List;

public abstract class Venta {
    protected int id;
    protected LocalDate fecha;
    protected double subtotal; 
    protected Usuario comprador; 
    protected double total;
    protected boolean esValida;

    public Venta(int id, LocalDate fecha, double subtotal, Usuario comprador, boolean esValida){
        this.id = id;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.comprador = comprador;
        this.esValida = esValida;
    }

    public int getId(){
         return id; 
    }

    public LocalDate getFecha(){ 
        return fecha; 
    }

    public double getSubtotal(){ 
        return subtotal;
    }

    public double getTotal(){ 
        return total; 
    }

    public Usuario getComprador(){
        return comprador; 
    }
    public boolean isValida(){
        return esValida; 
    }

    public void registrarVenta(List<Venta> historialCompras){ 
        if (this.esValida){
            historialCompras.add(this); 
        }
    }
}