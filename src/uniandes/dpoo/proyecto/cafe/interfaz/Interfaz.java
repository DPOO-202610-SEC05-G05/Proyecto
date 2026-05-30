package uniandes.dpoo.proyecto.cafe.interfaz;

import javax.swing.*;
import java.awt.*;

public class Interfaz extends JFrame {
    
    private JPanel panelPrincipal;
    private CardLayout cardLayout;

    public Interfaz() {
        setTitle("Dulces & Dados Café - Mockup");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelMenu = new JPanel();
        panelMenu.setBackground(new Color(74, 46, 27)); 
        
        JButton btnLogin = new JButton("Login");
        JButton btnCliente = new JButton("Vista Cliente");
        JButton btnAdmin = new JButton("Vista Admin");
        JButton btnEmpleado = new JButton("Vista Empleado");

        panelMenu.add(btnLogin);
        panelMenu.add(btnCliente);
        panelMenu.add(btnAdmin);
        panelMenu.add(btnEmpleado);
        add(panelMenu, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        panelPrincipal.add(crearPanelLogin(), "Login");
        panelPrincipal.add(crearPanelCliente(), "Cliente");
        panelPrincipal.add(crearPanelAdmin(), "Admin");
        panelPrincipal.add(crearPanelEmpleado(), "Empleado");

        add(panelPrincipal, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> cardLayout.show(panelPrincipal, "Login"));
        btnCliente.addActionListener(e -> cardLayout.show(panelPrincipal, "Cliente"));
        btnAdmin.addActionListener(e -> cardLayout.show(panelPrincipal, "Admin"));
        btnEmpleado.addActionListener(e -> cardLayout.show(panelPrincipal, "Empleado"));
    }

    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        
        form.add(new JLabel("Usuario:"));
        form.add(new JTextField(15));
        form.add(new JLabel("Contraseña:"));
        form.add(new JPasswordField(15));
        form.add(new JLabel("")); 
        form.add(new JButton("Entrar"));
        
        panel.add(form);
        return panel;
    }

    private JPanel crearPanelCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("<html><h2>Bienvenido, Cliente (Puntos: 150)</h2></html>", SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel centro = new JPanel(new GridLayout(1, 2, 20, 20));
        
        JPanel cafeteria = new JPanel();
        cafeteria.setLayout(new BoxLayout(cafeteria, BoxLayout.Y_AXIS));
        cafeteria.setBorder(BorderFactory.createTitledBorder("Menú de Cafetería"));
        cafeteria.add(new JButton("Pedir Café Moca - $4.50"));
        cafeteria.add(new JButton("Pedir Galleta - $2.00"));

        JPanel juegos = new JPanel();
        juegos.setLayout(new BoxLayout(juegos, BoxLayout.Y_AXIS));
        juegos.setBorder(BorderFactory.createTitledBorder("Juegos Disponibles"));
        juegos.add(new JButton("Reservar Ticket To Ride"));
        juegos.add(new JButton("Reservar Catan"));

        centro.add(cafeteria);
        centro.add(juegos);
        panel.add(centro, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel crearPanelAdmin() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("<html><h2>Panel de Control - Administrador</h2></html>", SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel graficas = new JPanel(new GridLayout(2, 2, 10, 10));
        

        graficas.add(crearPlaceholderGrafica("Gráfico Pastel: Copias (Venta vs Préstamo)"));
        graficas.add(crearPlaceholderGrafica("Gráfico Barras: Ventas Categoría (5 días)"));
        

        JPanel panelLinea = crearPlaceholderGrafica("Gráfico Líneas: Reservas en la semana");
        
        JPanel centro = new JPanel(new BorderLayout());
        centro.add(graficas, BorderLayout.CENTER);
        centro.add(panelLinea, BorderLayout.SOUTH);
        
        panel.add(centro, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelEmpleado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("<html><h2>Panel de Empleado - Turno Mañana</h2></html>", SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel ordenes = new JPanel();
        ordenes.setLayout(new BoxLayout(ordenes, BoxLayout.Y_AXIS));
        ordenes.setBorder(BorderFactory.createTitledBorder("Órdenes Pendientes"));
        
        ordenes.add(new JCheckBox("Mesa 4: 2x Café Moca, 1x Ticket to Ride"));
        ordenes.add(new JCheckBox("Mesa 2: 1x Catan (Devolución)"));
        
        panel.add(ordenes, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPlaceholderGrafica(String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 248, 220)); 
        panel.setBorder(BorderFactory.createLineBorder(new Color(139, 90, 43), 2, true));
        panel.setPreferredSize(new Dimension(300, 150));
        
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + texto + "</div></html>", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Interfaz().setVisible(true);
        });
    }
}