package uniandes.dpoo.proyecto.cafe;

import java.util.List;
import java.util.ArrayList;

public class Pasteleria extends ProductoMenu {
    private List<String> alergenos;

    public Pasteleria(String nombre, double precio){
        super(nombre, precio);
        this.alergenos = new ArrayList<>();
    }

    public Pasteleria(String nombre, double precio, List<String> alergenos){
        super(nombre, precio);
        this.alergenos = alergenos;
    }

    public List<String> getAlergenos(){
        return alergenos;
    }

    public String getAlergenosComoString() {
        return String.join(",", alergenos);
    }
}