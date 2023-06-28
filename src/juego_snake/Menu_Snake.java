
package juego_snake;

import javax.swing.JOptionPane;

/**
 *
 * @author HUAWEI
 */

       

public class Menu_Snake extends javax.swing.JFrame {

    private String nombreJugador;

    /**
     * Creates new form Menu_Snake
     */
    public Menu_Snake() {
        initComponents();
        this.setLocationRelativeTo(null);
      
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Boton_Instrucciones = new javax.swing.JButton();
        Boton_Jugador2 = new javax.swing.JButton();
        Boton_Jugador1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SNAKE");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 255, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Boton_Instrucciones.setText("Instrucciones");
        Boton_Instrucciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boton_InstruccionesActionPerformed(evt);
            }
        });
        jPanel1.add(Boton_Instrucciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 280, 170, 66));

        Boton_Jugador2.setText("2 Jugadores");
        Boton_Jugador2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boton_Jugador2ActionPerformed(evt);
            }
        });
        jPanel1.add(Boton_Jugador2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 280, 170, 66));

        Boton_Jugador1.setText("1 Jugador");
        Boton_Jugador1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boton_Jugador1ActionPerformed(evt);
            }
        });
        jPanel1.add(Boton_Jugador1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 170, 66));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 420));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Boton_Jugador2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boton_Jugador2ActionPerformed
        String nombreJugador1 = JOptionPane.showInputDialog("Ingrese el nombre del jugador 1:");
                String nombreJugador2 = JOptionPane.showInputDialog("Ingrese el nombre del jugador 2:");
                
                JuegoSnake2 juego = new JuegoSnake2(nombreJugador1, nombreJugador2);
                juego.iniciarJuego();
                 this.setVisible(false);
               
               
               
    }//GEN-LAST:event_Boton_Jugador2ActionPerformed

    private void Boton_Jugador1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boton_Jugador1ActionPerformed
           String nombreJugador = JOptionPane.showInputDialog(null, "Ingrese su nombre:", "Snake",
                JOptionPane.QUESTION_MESSAGE);

        if (nombreJugador != null && !nombreJugador.isEmpty()) {
            JuegoSnake juego = new JuegoSnake(nombreJugador);
            juego.ejecutarJuego();
        } else {
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    
    }//GEN-LAST:event_Boton_Jugador1ActionPerformed

    private void Boton_InstruccionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boton_InstruccionesActionPerformed
        Instrucciones Manual = new Instrucciones();
        Manual.setVisible(true);
        Manual.setLocationRelativeTo(null);
        this.setVisible(false);

    }//GEN-LAST:event_Boton_InstruccionesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu_Snake.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu_Snake.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu_Snake.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu_Snake.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu_Snake().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton_Instrucciones;
    private javax.swing.JButton Boton_Jugador1;
    private javax.swing.JButton Boton_Jugador2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}