package uniandes.dpoo.proyecto.cafe;
import java.time.LocalDate;
import java.util.List;

public abstract class Historial<T> {
    protected List<T> registros;

    public Historial(List<T> registros){
        this.registros = registros;
    }

    public List<T> getRegistros(){
        return registros;
    }

    public void agregarRegistro(T registro){
        registros.add(registro);
    }

    public abstract List<T> consultarPorFecha(LocalDate date1, LocalDate date2);
}