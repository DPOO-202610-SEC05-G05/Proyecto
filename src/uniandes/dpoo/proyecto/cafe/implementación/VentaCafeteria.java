package uniandes.dpoo.proyecto.cafe;
import java.time.LocalDate;

public class VentaCafeteria extends Venta {
    private double impuestoConsumo; 
    private double propina; 

    public VentaCafeteria(int id, LocalDate fecha, double subtotal, Usuario comprador, boolean esValida){
        super(id, fecha, subtotal, comprador, esValida);
        this.impuestoConsumo = 0.08;
        this.propina = subtotal * 0.10;
        this.total = subtotal + (subtotal * this.impuestoConsumo) + this.propina;
    }
    
    public VentaCafeteria(int id, LocalDate fecha, double total, double impuestoConsumo, double propina, boolean esValida){
        super(id, fecha, total, null, esValida);
        this.impuestoConsumo = impuestoConsumo;
        this.propina = propina;
        this.total = total;
    }
    
    public double getImpuestoConsumo(){
        return impuestoConsumo; 
    }
    
    public double getPropina(){ 
        return propina;
    }
}