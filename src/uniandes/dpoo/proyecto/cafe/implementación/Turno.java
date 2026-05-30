package uniandes.dpoo.proyecto.cafe;
import java.util.ArrayList;
import java.util.List;

public class Turno {
    private String dia;
    private String horaInicio;
    private String horaFinal;
    private List<Empleado> empleados;

    public Turno(String dia, String horaInicio, String horaFinal){
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
        this.empleados = new ArrayList<>();
    }

    public String getDia(){
        return dia; 
    }

    public String getHoraInicio(){ 
        return horaInicio; 
    }

    public String getHoraFinal(){ 
        return horaFinal;
     }
    public List<Empleado> getEmpleados() { 
        return empleados; }

    public List<Integer> getIdsEmpleados() {
        List<Integer> ids = new ArrayList<>();
        for(Empleado e : empleados) {
            ids.add(e.getId());
        }
        return ids;
    }

    public void setDia(String dia){ 
        this.dia = dia; 
    }

    public void setHoraInicio(String horaInicio){ 
        this.horaInicio = horaInicio; 
    }

    public void setHoraFinal(String horaFinal){ 
        this.horaFinal = horaFinal; 
    }

    public void agregarEmpleado(Empleado empleado){
        this.empleados.add(empleado);
    }

    public boolean validarFuncionamientoCafe(){
        int cocinero = 0;
        int mesero = 0;

        for(Empleado e : empleados){
            if (e instanceof Cocinero){
                cocinero++;
            }
            if (e instanceof Mesero){
                mesero++;
            }
        }

        return cocinero >= 1 && mesero >= 2;
    }
}
