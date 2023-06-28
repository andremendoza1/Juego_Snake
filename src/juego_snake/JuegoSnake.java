
package juego_snake;

/**
 *
 * @author HUAWEI
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class JuegoSnake implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int ANCHO_TABLERO = 20;
    private static final int ALTO_TABLERO = 20;
    private static final int TAMANO_CELDA = 20;
    private static final int VELOCIDAD_INICIAL = 200;
    private static final int DURACION_PARTIDA = 3 * 60 * 1000; // 3 minutos en milisegundos

    private transient JFrame ventana;
    private transient JPanel panelJuego;
    private transient TimerTask task;
    private transient java.util.Timer timer;

    private List<Point> serpiente;
    private Point comida;
    private int direccion;
    private int velocidad;
    private long tiempoInicio;

    private String nombreJugador;
    private int puntuacion;

    public JuegoSnake(String nombreJugador) {
        this.nombreJugador = nombreJugador;

        serpiente = new ArrayList<>();
        serpiente.add(new Point(ANCHO_TABLERO / 2, ALTO_TABLERO / 2));

        direccion = KeyEvent.VK_RIGHT;
        velocidad = VELOCIDAD_INICIAL;

        generarComida();

        // Crear el temporizador
        timer = new java.util.Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
                if (tiempoTranscurrido >= DURACION_PARTIDA) {
                    terminarJuego();
                } else {
                    mover();
                    panelJuego.repaint();
                }
            }
        };
    }

    private void generarComida() {
        int x = (int) (Math.random() * ANCHO_TABLERO);
        int y = (int) (Math.random() * ALTO_TABLERO);
        comida = new Point(x, y);
    }

    private void cambiarDireccion(KeyEvent e) {
        int nuevaDireccion = e.getKeyCode();
        if ((nuevaDireccion == KeyEvent.VK_LEFT && direccion != KeyEvent.VK_RIGHT)
                || (nuevaDireccion == KeyEvent.VK_RIGHT && direccion != KeyEvent.VK_LEFT)
                || (nuevaDireccion == KeyEvent.VK_UP && direccion != KeyEvent.VK_DOWN)
                || (nuevaDireccion == KeyEvent.VK_DOWN && direccion != KeyEvent.VK_UP)) {
            direccion = nuevaDireccion;
        }
    }

    private void mover() {
        Point cabeza = new Point(serpiente.get(0));
        switch (direccion) {
            case KeyEvent.VK_LEFT:
                cabeza.x--;
                break;
            case KeyEvent.VK_RIGHT:
                cabeza.x++;
                break;
            case KeyEvent.VK_UP:
                cabeza.y--;
                break;
            case KeyEvent.VK_DOWN:
                cabeza.y++;
                break;
        }

        if (cabeza.equals(comida)) {
            serpiente.add(0, cabeza);
            generarComida();
            puntuacion++;
        } else {
            serpiente.remove(serpiente.size() - 1);
            serpiente.add(0, cabeza);
        }

        // Verificar si la serpiente se golpea a sí misma
        for (int i = 1; i < serpiente.size(); i++) {
            if (cabeza.equals(serpiente.get(i))) {
                terminarJuego();
                return;
            }
        }

        // Verificar si la serpiente se sale del tablero
        if (cabeza.x < 0 || cabeza.x >= ANCHO_TABLERO || cabeza.y < 0 || cabeza.y >= ALTO_TABLERO) {
            terminarJuego();
            return;
        }
    }

    private void dibujar(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, ANCHO_TABLERO * TAMANO_CELDA, ALTO_TABLERO * TAMANO_CELDA);

        g.setColor(Color.GREEN);
        for (Point p : serpiente) {
            g.fillRect(p.x * TAMANO_CELDA, p.y * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);
        }

        g.setColor(Color.RED);
        g.fillRect(comida.x * TAMANO_CELDA, comida.y * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);

        g.setColor(Color.WHITE);
        g.drawString("Puntuación: " + puntuacion, 10, 20);

        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
        long tiempoRestante = DURACION_PARTIDA - tiempoTranscurrido;
        String tiempo = String.format("%02d:%02d", tiempoRestante / 60000, (tiempoRestante % 60000) / 1000);
        g.drawString("Tiempo restante: " + tiempo, 10, 40);
    }

    private void terminarJuego() {
        guardarDatosJugador();

        int opcion = JOptionPane.showConfirmDialog(ventana, "Juego terminado\n\nPuntuación: " + puntuacion
                + "\n\n¿Desea iniciar otra partida?", "Game Over", JOptionPane.YES_NO_OPTION);

        ventana.dispose(); // Cerrar la ventana del juego

        if (opcion == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                iniciarJuego();
            });
        } else {
            mostrarRegistrosJugadores();
            Interfaz_Principal.main(null); // Volver a la ventana Interfaz_Principal
        }
    }

    private void guardarDatosJugador() {
        try (FileWriter fileWriter = new FileWriter("datosJugadores.txt", true);
                PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println(nombreJugador + "," + puntuacion);

            System.out.println("Datos del jugador guardados.");

        } catch (IOException e) {
            System.out.println("Error al guardar los datos del jugador: " + e.getMessage());
        }
    }

    private void mostrarRegistrosJugadores() {
        try (FileReader fileReader = new FileReader("datosJugadores.txt");
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String linea;
            StringBuilder registros = new StringBuilder();
            while ((linea = bufferedReader.readLine()) != null) {
                registros.append(linea).append("\n");
            }

            JOptionPane.showMessageDialog(null, "Registros de jugadores:\n\n" + registros.toString(),
                    "Records de Jugadores", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            System.out.println("Error al leer los datos de los jugadores: " + e.getMessage());
        }
    }

    private void iniciarJuego() {
        ventana = new JFrame("Snake");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);

        panelJuego = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujar(g);
            }
        };
        panelJuego.setPreferredSize(new Dimension(ANCHO_TABLERO * TAMANO_CELDA, ALTO_TABLERO * TAMANO_CELDA));
        ventana.add(panelJuego);

        ventana.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                cambiarDireccion(e);
            }
        });

        ventana.pack();
        ventana.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        ventana.setVisible(true);

        JOptionPane.showMessageDialog(ventana, "Presiona OK para comenzar", "Snake",
                JOptionPane.INFORMATION_MESSAGE);

        serpiente.clear();
        serpiente.add(new Point(ANCHO_TABLERO / 2, ALTO_TABLERO / 2));
        direccion = KeyEvent.VK_RIGHT;
        velocidad = VELOCIDAD_INICIAL;
        puntuacion = 0;
        tiempoInicio = System.currentTimeMillis();
        generarComida();

        timer.schedule(task, 0, velocidad);
    }

    public void ejecutarJuego() {
        SwingUtilities.invokeLater(() -> {
            ventana = new JFrame("Snake");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setResizable(false);

            panelJuego = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    dibujar(g);
                }
            };
            panelJuego.setPreferredSize(new Dimension(ANCHO_TABLERO * TAMANO_CELDA, ALTO_TABLERO * TAMANO_CELDA));
            ventana.add(panelJuego);

            ventana.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    cambiarDireccion(e);
                }
            });

            ventana.pack();
            ventana.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
            ventana.setVisible(true);

            JOptionPane.showMessageDialog(ventana, "Presiona OK para comenzar", "Snake",
                    JOptionPane.INFORMATION_MESSAGE);

            tiempoInicio = System.currentTimeMillis();
            timer.schedule(task, 0, velocidad);
        });
    }
}


