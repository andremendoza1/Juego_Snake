package juego_snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class JuegoSnake2 implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int ANCHO_TABLERO = 20;
    private static final int ALTO_TABLERO = 20;
    private static final int TAMANO_CELDA = 20;
    private static final int VELOCIDAD_INICIAL = 200;
    private static final int DURACION_PARTIDA = 3 * 60 * 1000; // 3 minutos en milisegundos

    private transient JFrame ventana;
    private transient JPanel panelJuego1;
    private transient JPanel panelJuego2;
    private transient TimerTask task;
    private transient java.util.Timer timer;

    private List<Point> serpiente1;
    private List<Point> serpiente2;
    private Point comida;
    private int direccion1;
    private int direccion2;
    private int velocidad;
    private long tiempoInicio;

    private String nombreJugador1;
    private String nombreJugador2;
    private int puntuacion1;
    private int puntuacion2;

    public JuegoSnake2(String nombreJugador1, String nombreJugador2) {
        this.nombreJugador1 = nombreJugador1;
        this.nombreJugador2 = nombreJugador2;

        serpiente1 = new ArrayList<>();
        serpiente1.add(new Point(ANCHO_TABLERO / 2, ALTO_TABLERO / 4));
        serpiente2 = new ArrayList<>();
        serpiente2.add(new Point(ANCHO_TABLERO / 2, 3 * ALTO_TABLERO / 4));

        direccion1 = KeyEvent.VK_RIGHT;
        direccion2 = KeyEvent.VK_D;
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
                    panelJuego1.repaint();
                    panelJuego2.repaint();
                }
            }
        };
    }

    private void generarComida() {
        int x = (int) (Math.random() * ANCHO_TABLERO);
        int y = (int) (Math.random() * ALTO_TABLERO);
        comida = new Point(x, y);
    }

    private void cambiarDireccion(KeyEvent e, int jugador) {
        int nuevaDireccion = e.getKeyCode();
        if (jugador == 1) {
            if ((nuevaDireccion == KeyEvent.VK_LEFT && direccion1 != KeyEvent.VK_RIGHT)
                    || (nuevaDireccion == KeyEvent.VK_RIGHT && direccion1 != KeyEvent.VK_LEFT)
                    || (nuevaDireccion == KeyEvent.VK_UP && direccion1 != KeyEvent.VK_DOWN)
                    || (nuevaDireccion == KeyEvent.VK_DOWN && direccion1 != KeyEvent.VK_UP)) {
                direccion1 = nuevaDireccion;
            }
        } else if (jugador == 2) {
            if ((nuevaDireccion == KeyEvent.VK_A && direccion2 != KeyEvent.VK_D)
                    || (nuevaDireccion == KeyEvent.VK_D && direccion2 != KeyEvent.VK_A)
                    || (nuevaDireccion == KeyEvent.VK_W && direccion2 != KeyEvent.VK_S)
                    || (nuevaDireccion == KeyEvent.VK_S && direccion2 != KeyEvent.VK_W)) {
                direccion2 = nuevaDireccion;
            }
        }
    }

    private void mover() {
        Point cabeza1 = new Point(serpiente1.get(0));
        Point cabeza2 = new Point(serpiente2.get(0));

        switch (direccion1) {
            case KeyEvent.VK_LEFT:
                cabeza1.x--;
                break;
            case KeyEvent.VK_RIGHT:
                cabeza1.x++;
                break;
            case KeyEvent.VK_UP:
                cabeza1.y--;
                break;
            case KeyEvent.VK_DOWN:
                cabeza1.y++;
                break;
        }

        switch (direccion2) {
            case KeyEvent.VK_A:
                cabeza2.x--;
                break;
            case KeyEvent.VK_D:
                cabeza2.x++;
                break;
            case KeyEvent.VK_W:
                cabeza2.y--;
                break;
            case KeyEvent.VK_S:
                cabeza2.y++;
                break;
        }

        if (cabeza1.equals(comida)) {
            serpiente1.add(0, cabeza1);
            generarComida();
            puntuacion1++;
        } else {
            serpiente1.remove(serpiente1.size() - 1);
            serpiente1.add(0, cabeza1);
        }

        if (cabeza2.equals(comida)) {
            serpiente2.add(0, cabeza2);
            generarComida();
            puntuacion2++;
        } else {
            serpiente2.remove(serpiente2.size() - 1);
            serpiente2.add(0, cabeza2);
        }

        // Verificar si la serpiente 1 se golpea a sí misma o a la serpiente 2
        for (int i = 1; i < serpiente1.size(); i++) {
            if (cabeza1.equals(serpiente1.get(i)) || cabeza1.equals(serpiente2.get(i))) {
                terminarJuego();
                return;
            }
        }

        // Verificar si la serpiente 2 se golpea a sí misma o a la serpiente 1
        for (int i = 1; i < serpiente2.size(); i++) {
            if (cabeza2.equals(serpiente2.get(i)) || cabeza2.equals(serpiente1.get(i))) {
                terminarJuego();
                return;
            }
        }

        // Verificar si la serpiente 1 se sale del tablero
        if (cabeza1.x < 0 || cabeza1.x >= ANCHO_TABLERO || cabeza1.y < 0 || cabeza1.y >= ALTO_TABLERO) {
            terminarJuego();
            return;
        }

        // Verificar si la serpiente 2 se sale del tablero
        if (cabeza2.x < 0 || cabeza2.x >= ANCHO_TABLERO || cabeza2.y < 0 || cabeza2.y >= ALTO_TABLERO) {
            terminarJuego();
            return;
        }
    }

    private void dibujar(Graphics g, int jugador) {
        if (jugador == 1) {
            List<Point> cuerpo = serpiente1;
            g.setColor(Color.GREEN);
            for (Point punto : cuerpo) {
                g.fillRect(punto.x * TAMANO_CELDA, punto.y * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);
            }
        } else if (jugador == 2) {
            List<Point> cuerpo = serpiente2;
            g.setColor(Color.BLUE);
            for (Point punto : cuerpo) {
                g.fillRect(punto.x * TAMANO_CELDA, punto.y * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);
            }
        }

        g.setColor(Color.RED);
        g.fillRect(comida.x * TAMANO_CELDA, comida.y * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);
    }

    private void terminarJuego() {
        timer.cancel();
        timer.purge();
        ventana.dispose();
        System.out.println("¡Juego terminado!");
        System.out.println("Puntuación " + nombreJugador1 + ": " + puntuacion1);
        System.out.println("Puntuación " + nombreJugador2 + ": " + puntuacion2);
    }

    public void iniciarJuego() {
        tiempoInicio = System.currentTimeMillis();

        ventana = new JFrame("Juego Snake");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);

        panelJuego1 = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujar(g, 1);
            }
        };

        panelJuego1.setPreferredSize(new Dimension(ANCHO_TABLERO * TAMANO_CELDA, ALTO_TABLERO * TAMANO_CELDA));
        panelJuego1.setBackground(Color.BLACK);

        panelJuego2 = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujar(g, 2);
            }
        };

        panelJuego2.setPreferredSize(new Dimension(ANCHO_TABLERO * TAMANO_CELDA, ALTO_TABLERO * TAMANO_CELDA));
        panelJuego2.setBackground(Color.BLACK);

        ventana.add(panelJuego1, BorderLayout.WEST);
        ventana.add(panelJuego2, BorderLayout.EAST);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        ventana.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                cambiarDireccion(e, 1);
            }
        });

        panelJuego1.setFocusable(true);
        panelJuego1.requestFocusInWindow();

        panelJuego1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                cambiarDireccion(e, 1);
            }
        });

        panelJuego2.setFocusable(true);
        panelJuego2.requestFocusInWindow();

        panelJuego2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                cambiarDireccion(e, 2);
            }
        });

        timer.scheduleAtFixedRate(task, 0, velocidad);
    }

    public static void main(String[] args) {
        JuegoSnake2 juego = new JuegoSnake2("Jugador 1", "Jugador 2");
        juego.iniciarJuego();
    }
}

