package uniandes.dpoo.proyecto.cafe;
import java.time.LocalDate;

public class VentaJuego extends Venta {
    private double iva; 
    private double descuentoAplicado;

    public VentaJuego(int id, LocalDate fecha, double subtotal, double descuentoAplicado, Usuario comprador, boolean esValida){
        super(id, fecha, subtotal, comprador, esValida);
        this.iva = 0.19; 
        this.descuentoAplicado = descuentoAplicado;
        this.total = (subtotal - descuentoAplicado) * (1 + this.iva);

    }

    public VentaJuego(int id, LocalDate fecha, double total, double iva, double descuentoAplicado, boolean esValida){
        super(id, fecha, total, null, esValida);
        this.iva = iva;
        this.descuentoAplicado = descuentoAplicado;
        this.total = total;
    }
    
    public double getIva(){ 
        return iva; 
    }
    
    public double getDescuento(){ 
        return descuentoAplicado; 
    }
}