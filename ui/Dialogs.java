package ui;

import javax.swing.*;
import java.awt.*;

public class Dialogs {

    private static final String TITULO = "🔐 Sistema de Gestión de Usuarios";

  
    public static void exito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO,
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO,
            JOptionPane.ERROR_MESSAGE);
    }

    public static void advertencia(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO,
            JOptionPane.WARNING_MESSAGE);
    }

     
      @param etiqueta 
      @return 
     
    public static String pedirTexto(String etiqueta) {
        String valor = JOptionPane.showInputDialog(null, etiqueta, TITULO,
            JOptionPane.QUESTION_MESSAGE);
        if (valor == null) return null;
        return valor.trim().isEmpty() ? null : valor.trim();
    }

     
     
     @param etiqueta 
     @return 
     
    public static String pedirPassword(String etiqueta) {
        JPasswordField campoPassword = new JPasswordField(20);
        campoPassword.setEchoChar('●'); 
        
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel(etiqueta), BorderLayout.NORTH);
        panel.add(campoPassword, BorderLayout.CENTER);

        campoPassword.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent e) {
                campoPassword.requestFocusInWindow();
            }