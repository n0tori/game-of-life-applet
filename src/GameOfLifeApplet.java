import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class GameOfLifeApplet extends Applet {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private static final int CELL_SIZE = 15;
    private static final int MAX_GENERATIONS = 1000;

    private boolean[][] grid;
    private boolean running;
    private Timer timer;
    private int generation;
    private int population;

    private Button playButton;
    private Button pauseButton;
    private Button terminateButton;

    
    public void init() {
        setSize(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE + 50); // Space for buttons and counters
        grid = new boolean[WIDTH][HEIGHT];
        running = false;
        generation = 0;
        population = 0;

        initializeRandomGrid();

        // Set up Timer to control the speed of the game
        timer = new Timer(300, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    evolve();
                    repaint();
                    generation++;
                    updatePopulation();
                    if (generation >= MAX_GENERATIONS) {
                        timer.stop();
                        running = false;
                    }
                }
            }
        });

        // Set up buttons
        playButton = new Button("Play");
        pauseButton = new Button("Pause");
        terminateButton = new Button("Terminate");

        playButton.setBounds(10, HEIGHT * CELL_SIZE + 10, 60, 30);
        pauseButton.setBounds(80, HEIGHT * CELL_SIZE + 10, 60, 30);
        terminateButton.setBounds(150, HEIGHT * CELL_SIZE + 10, 80, 30);

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pauseGame();
            }
        });

        terminateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                terminateGame();
            }
        });

        add(playButton);
        add(pauseButton);
        add(terminateButton);
    }

    
    public void start() {
        if (!running) {
            startGame();
        }
    }

    
    public void stop() {
        if (running) {
            timer.stop();
            running = false;
        }
    }

    
    public void paint(Graphics g) {
        super.paint(g);

        // Draw the generation and population counters at the top
        g.setColor(Color.BLACK);
        g.drawString("Generation: " + generation, 10, 20);
        g.drawString("Population: " + population, 10, 40);

        // Draw the grid (shifted down to avoid overlap with the counters)
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (grid[x][y]) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE + 50, CELL_SIZE, CELL_SIZE);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE + 50, CELL_SIZE, CELL_SIZE);
                }
                g.setColor(Color.GRAY);
                g.drawRect(x * CELL_SIZE, y * CELL_SIZE + 50, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void initializeRandomGrid() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                grid[x][y] = Math.random() < 0.3;
            }
        }
        updatePopulation();
    }

    private void evolve() {
        boolean[][] nextGrid = new boolean[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int neighbors = countNeighbors(x, y);
                if (grid[x][y]) {
                    nextGrid[x][y] = (neighbors == 2 || neighbors == 3);
                } else {
                    nextGrid[x][y] = (neighbors == 3);
                }
            }
        }
        grid = nextGrid;
    }

    private int countNeighbors(int x, int y) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = (x + dx + WIDTH) % WIDTH;
                int ny = (y + dy + HEIGHT) % HEIGHT;
                if (grid[nx][ny]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void updatePopulation() {
        population = 0;
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (grid[x][y]) {
                    population++;
                }
            }
        }
    }

    // Control methods for the game
    private void startGame() {
        if (!running) {
            timer.start();
            running = true;
        }
    }

    private void pauseGame() {
        if (running) {
            timer.stop();
            running = false;
        }
    }

    private void terminateGame() {
        timer.stop();
        running = false;
        generation = 0;
        population = 0;
        initializeRandomGrid();
        repaint();
    }
}
