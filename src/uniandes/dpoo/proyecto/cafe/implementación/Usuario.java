package uniandes.dpoo.proyecto.cafe;
public abstract class Usuario {
    private int id;
    private String username;
    private String email;
    private String password;
    protected boolean esValido;

    public Usuario (int id, String username, String email, String password, boolean esValido){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.esValido = esValido;
    }

    public int getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public boolean esValido() {
        return esValido;
    }

    public boolean login(String username, String email, String password) {
        return this.username.equals(username) && this.email.equals(email) && this.password.equals(password);
    }

    public void setUsername(String username){
        this.username = username;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
}