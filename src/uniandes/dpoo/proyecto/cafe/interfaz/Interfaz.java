package uniandes.dpoo.proyecto.cafe.interfaz;

import javax.swing.*;
import java.awt.*;

public class interfaz extends JFrame {
    
    private JPanel panelPrincipal;
    private CardLayout cardLayout;

    public interfaz() {
        setTitle("Dulces & Dados - Café");
        setSize(900, 650);
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
        panel.add(new JLabel("<html><h2 style='text-align:center;'>Bienvenido, Cliente (Puntos: 150)</h2></html>", SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel centro = new JPanel(new GridLayout(1, 2, 20, 20));
        
        JPanel cafeteria = new JPanel();
        cafeteria.setLayout(new BoxLayout(cafeteria, BoxLayout.Y_AXIS));
        cafeteria.setBorder(BorderFactory.createTitledBorder("Menú de Cafetería"));
        cafeteria.add(new JButton("Pedir Café Moca - $4.50"));
        cafeteria.add(Box.createVerticalStrut(10));
        cafeteria.add(new JButton("Pedir Galleta - $2.00"));
        
        JPanel juegos = new JPanel();
        juegos.setLayout(new BoxLayout(juegos, BoxLayout.Y_AXIS));
        juegos.setBorder(BorderFactory.createTitledBorder("Juegos Disponibles"));
        juegos.add(new JButton("Reservar Ticket To Ride"));
        juegos.add(Box.createVerticalStrut(10));
        juegos.add(new JButton("Reservar Catan"));

        centro.add(cafeteria);
        centro.add(juegos);
        panel.add(centro, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel crearPanelEmpleado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("<html><h2 style='text-align:center;'>Panel de Empleado - Turno Mañana</h2></html>", SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel ordenes = new JPanel();
        ordenes.setLayout(new BoxLayout(ordenes, BoxLayout.Y_AXIS));
        ordenes.setBorder(BorderFactory.createTitledBorder("Órdenes Pendientes"));
        
        ordenes.add(new JCheckBox("Mesa 4: 2x Café Moca, 1x Ticket to Ride"));
        ordenes.add(new JCheckBox("Mesa 2: 1x Catan (Devolución)"));
        
        panel.add(ordenes, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelAdmin() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("<html><h2 style='text-align:center;'>Panel de Control - Administrador</h2></html>", SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel graficas = new JPanel(new GridLayout(2, 2, 10, 10));
        graficas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        graficas.add(crearGraficaPastel());
        graficas.add(crearGraficaBarras());
        
        JPanel panelLinea = crearGraficaLineas();
        
        JPanel centro = new JPanel(new BorderLayout());
        centro.add(graficas, BorderLayout.CENTER);
        centro.add(panelLinea, BorderLayout.SOUTH);
        
        panel.add(centro, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearGraficaPastel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(65, 105, 225)); 
                g2.fillArc(100, 30, 150, 150, 90, 210);
                g2.setColor(new Color(220, 20, 60)); 
                g2.fillArc(100, 30, 150, 150, 300, 150);
                

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 10));
                g2.drawString("■ Copias Inventario (Azul)", 10, 200);
                g2.drawString("■ Copias Préstamo (Rojo)", 170, 200);
            }
        };
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder("Disponibilidad Ticket To Ride"));
        return panel;
    }

    private JPanel crearGraficaBarras() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                
                g2.setColor(Color.BLACK);
                g2.drawLine(30, 20, 30, 170); 
                g2.drawLine(30, 170, 350, 170); 
                
                int[] cafe = {20, 40, 60, 50, 30};
                int[] juegos = {50, 30, 90, 40, 80};
                
                for(int i = 0; i < 5; i++) {
                    int x = 50 + (i * 60);

                    g2.setColor(new Color(220, 20, 60));
                    g2.fillRect(x, 170 - cafe[i], 15, cafe[i]);

                    g2.setColor(new Color(65, 105, 225));
                    g2.fillRect(x + 15, 170 - juegos[i], 15, juegos[i]);

                    g2.setColor(Color.BLACK);
                    g2.drawString("Día " + (i+1), x, 185);
                }
            }
        };
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder("Ventas por periodo (Últimos 5 días)"));
        return panel;
    }

    private JPanel crearGraficaLineas() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.BLACK);
                g2.drawLine(40, 20, 40, 150); 
                g2.drawLine(40, 150, 800, 150); 
                
                int[] reservas = {10, 25, 15, 45, 60, 30, 40};
                String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
                
                g2.setColor(new Color(220, 20, 60));  
                g2.setStroke(new BasicStroke(2));
                
                for(int i = 0; i < 6; i++) {
                    int x1 = 80 + (i * 110);
                    int y1 = 150 - reservas[i];
                    int x2 = 80 + ((i+1) * 110);
                    int y2 = 150 - reservas[i+1];
                    
                    g2.drawLine(x1, y1, x2, y2);
                    g2.fillOval(x1 - 4, y1 - 4, 8, 8); 
                }
                g2.fillOval(80 + (6 * 110) - 4, 150 - reservas[6] - 4, 8, 8); 
                
                g2.setColor(Color.BLACK);
                for(int i = 0; i < 7; i++) {
                    g2.drawString(dias[i], 70 + (i * 110), 170);
                }
            }
        };
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(850, 220));
        panel.setBorder(BorderFactory.createTitledBorder("Evolución Reservas Semana 15"));
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new interfaz().setVisible(true);
        });
    }
}