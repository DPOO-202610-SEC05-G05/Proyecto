package uniandes.dpoo.proyecto.cafe;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class HistorialPrestamo extends Historial<Prestamo> {
    private List<Prestamo> prestamos;
    public HistorialPrestamo(){
        super(new ArrayList<>());
        this.prestamos = registros;
    }

    public List<Prestamo> getPrestamos(){
        return prestamos;
    }

    public void registrarPrestamo(Prestamo prestamo){
        prestamos.add(prestamo);
    }

    public List<Prestamo> consultarPorCliente(Cliente cliente){
        return new ArrayList<>();
    }

    public List<Prestamo> consultarActivos(){
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo prestamo : prestamos){
            if (prestamo.getEstado().equals("activo")){
                activos.add(prestamo);
            }
        }
        return activos;
    }

    @Override
    public List<Prestamo> consultarPorFecha(LocalDate date1, LocalDate date2){
        List<Prestamo> resultado = new ArrayList<>();
        for (Prestamo prestamo : prestamos){
            if ((prestamo.getFecha().isEqual(date1) || prestamo.getFecha().isAfter(date1)) &&
                (prestamo.getFecha().isEqual(date2) || prestamo.getFecha().isBefore(date2))){
                resultado.add(prestamo);
            }
        }
        return resultado;
    }
}