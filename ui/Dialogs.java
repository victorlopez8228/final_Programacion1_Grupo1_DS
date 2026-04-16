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

     
    public static String pedirTexto(String etiqueta) {
        String valor = JOptionPane.showInputDialog(null, etiqueta, TITULO,
            JOptionPane.QUESTION_MESSAGE);
        if (valor == null) return null;
        return valor.trim().isEmpty() ? null : valor.trim();
    }

     
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
            public void ancestorRemoved(javax.swing.event.AncestorEvent e) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent e) {}
        });

        int resultado = JOptionPane.showConfirmDialog(null, panel, TITULO,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) return null;

        String pass = new String(campoPassword.getPassword());
        return pass.trim().isEmpty() ? null : pass;
    }

    
     
    public static boolean confirmar(String pregunta) {
        int resp = JOptionPane.showConfirmDialog(null, pregunta, TITULO,
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return resp == JOptionPane.YES_OPTION;
    }
     
    public static int mostrarMenu(String mensaje, String[] opciones) {
        return JOptionPane.showOptionDialog(
            null,
            mensaje,
            TITULO,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,       
            opciones,   
            opciones[0]
        );
    }

     
    public static String seleccionarDeLista(String mensaje, String[] opciones) {
        return (String) JOptionPane.showInputDialog(
            null,
            mensaje,
            TITULO,
            JOptionPane.QUESTION_MESSAGE,
            null,       
            opciones,   
            opciones.length > 0 ? opciones[0] : null
        );
    }
}