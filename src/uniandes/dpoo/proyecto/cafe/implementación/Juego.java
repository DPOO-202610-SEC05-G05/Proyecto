package uniandes.dpoo.proyecto.cafe;
public class Juego {
    private int id;
    private String nombre;
    private int anioPublicacion;
    private String empresa;
    private String categoria;
    private int minJugadores;
    private int maxJugadores;
    private int edadMinima;
    private String estado;
    private boolean esDificil;
    private double precio;

    public Juego(int id, String nombre, int anioPublicacion, String empresa, String categoria, int minJugadores, int maxJugadores, int edadMinima, boolean esDificil, String estado, double precio){
        this.id = id;
        this.nombre = nombre;
        this.anioPublicacion = anioPublicacion;
        this.empresa = empresa;
        this.categoria = categoria;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.edadMinima = edadMinima;
        this.esDificil = esDificil;
        this.estado = estado;
        this.precio = precio;
    }

    public int getId() { 
        return id; 
    }

    public String getNombre(){ 
        return nombre; 
    }

    public int getAnioPublicacion(){
        return anioPublicacion; 
    }

    public String getEmpresa(){ 
        return empresa; 
    }

    public String getCategoria(){ 
        return categoria;
    }

    public int getMinJugadores(){ 
        return minJugadores; 
    }

    public int getMaxJugadores(){ 
        return maxJugadores; 
    }
    
    public int getEdadMinima(){ 
        return edadMinima; 
    }

    public String getEstado(){ 
        return estado; 
    }

    public boolean isEsDificil(){ 
        return esDificil; 
    }

    public double getPrecio() { 
        return precio; 
    }

    public void setPrecio(double precio) { 
        this.precio = precio; 
    }

}