import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Sim453 {
    static int width = 140;
    static int height = 140;
    static int delay = 250;
    static final int[] iterationCount = {0};
    static boolean[][] grid;
    static boolean[][] tempGrid;

    // initialize grid with random pattern or pattern specified by user
    public static void init(String pattern) {
        grid = new boolean[width][height];
        tempGrid = new boolean[width][height];

        Random rand = new Random();
        if (pattern.equals("R")) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    grid[i][j] = rand.nextBoolean();
                }
            }
        } else if (pattern.equals("B")) {
            // initialize By Flop pattern
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    grid[i][j] = false;
                }
            }
            grid[4][1] = true;
            grid[4][2] = true;
            grid[2][2] = true;
            grid[6][3] = true;
            grid[6][5] = true;
            grid[4][6] = true;
            grid[4][7] = true;
            grid[2][6] = true;
            grid[1][4] = true;
            grid[2][4] = true;
            grid[3][4] = true;
            grid[4][4] = true;
            grid[5][4] = true;
        } else if (pattern.equals("C")) {
            // initialize Crab pattern
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    grid[i][j] = false;
                }
            }
            grid[9][1] = true;
            grid[10][1] = true;
            grid[9][2] = true;
            grid[8][2] = true;
            grid[10][3] = true;
            grid[12][4] = true;
            grid[13][4] = true;
            grid[11][5] = true;
            grid[10][7] = true;
            grid[13][7] = true;
            grid[2][8] = true;
            grid[3][8] = true;
            grid[9][8] = true;
            grid[10][8] = true;
            grid[1][9] = true;
            grid[2][9] = true;
            grid[8][9] = true;
            grid[3][10] = true;
            grid[8][10] = true;
            grid[10][10] = true;
            grid[5][11] = true;
            grid[6][11] = true;
            grid[9][11] = true;
            grid[5][12] = true;
            grid[6][12] = true;
        }
    }

    // get the number of alive cells surrounding a cell at (x, y)
    public static int getAliveNeighbours(int x, int y) {
        int aliveNeighbours = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    // wrap around the grid
                    int x_coord = (x + i + width) % width;
                    int y_coord = (y + j + height) % height;
                    if (grid[x_coord][y_coord]) {
                        aliveNeighbours++;
                    }
                }
            }
        }
        return aliveNeighbours;
    }

    // update the state of the grid for the next iteration
    public static void update() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int aliveNeighbours = getAliveNeighbours(i, j);
                if (grid[i][j]) {
                    // cell is currently alive
                    if (aliveNeighbours < 2 || aliveNeighbours > 3) {
                        tempGrid[i][j] = false;
                    } else {
                        tempGrid[i][j] = true;
                    }
                } else {
                    // cell is currently dead
                    if (aliveNeighbours == 3) {
                        tempGrid[i][j] = true;
                    } else {
                        tempGrid[i][j] = false;
                    }
                }
            }
        }
        // copy tempGrid back to grid
        for (int i = 0; i < width; i++) {
            System.arraycopy(tempGrid[i], 0, grid[i], 0, height);
        }
    }

    public static void main(String[] args) {
        // parse command line arguments
        int iterations = 0;
        if (args.length > 0) {
            try {
                iterations = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Error: iterations argument must be an integer");
                System.exit(1);
            }
        }
        String pattern = args[1];
        init(pattern);

        // create GUI
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Conway's Game of Life");

        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        if (grid[i][j]) {
                            g.setColor(Color.BLACK);
                        } else {
                            g.setColor(Color.WHITE);
                        }
                        g.fillRect(i * 4, j * 4, 4, 4);
                    }
                }
            }
        };
        panel.setPreferredSize(new Dimension(width * 4, height * 4));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        // start animation
        int finalIterations = iterations;
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (iterationCount[0] < finalIterations) {
                    update();
                    panel.repaint();
                    iterationCount[0]++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }
}
