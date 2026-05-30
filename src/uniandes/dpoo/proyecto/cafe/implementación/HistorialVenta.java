package uniandes.dpoo.proyecto.cafe;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class HistorialVenta extends Historial<Venta> {
    private List<Venta> ventas;

    public HistorialVenta(){
        super(new ArrayList<>());
        this.ventas = registros;
    }

    public List<Venta> getVentas(){
        return ventas;
    }

    public void registrarVenta(Venta venta){
        ventas.add(venta);
    }

    public List<Venta> consultarPorCliente(Cliente cliente){
        return new ArrayList<>();
    }

    public double calcularTotalVentas(){
        double total = 0;
        for (Venta venta : ventas){
            total += venta.getTotal();
        }
        return total;
    }

    @Override
    public List<Venta> consultarPorFecha(LocalDate date1, LocalDate date2){
        List<Venta> resultado = new ArrayList<>();
        for (Venta venta : ventas){
            if ((venta.getFecha().isEqual(date1) || venta.getFecha().isAfter(date1)) &&
                (venta.getFecha().isEqual(date2) || venta.getFecha().isBefore(date2))){
                resultado.add(venta);
            }
        }
        return resultado;
    }
}
