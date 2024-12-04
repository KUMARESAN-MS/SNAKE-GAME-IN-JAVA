import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        GameBoard board = new GameBoard();
        frame.add(board);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class GameBoard extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private LinkedList<Point> snake;
    private Point food;
    private String direction = "RIGHT";
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private int score = 0;
    private JButton startButton, restartButton;

    public GameBoard() {
        setLayout(null);
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Initialize Start Button
        startButton = new JButton("Start Game");
        startButton.setBounds(200, 250, 200, 50);
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setFocusable(false);
        startButton.addActionListener(e -> startGame());
        add(startButton);

        // Initialize Restart Button
        restartButton = new JButton("Restart");
        restartButton.setBounds(200, 320, 200, 50);
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setFocusable(false);
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> restartGame());
        add(restartButton);
    }

    private void startGame() {
        startButton.setVisible(false);
        initGame();
        gameStarted = true;
        repaint();
    }

    private void initGame() {
        snake = new LinkedList<>();
        snake.add(new Point(5, 5));
        generateFood();
        direction = "RIGHT";
        score = 0;
        gameOver = false;

        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(150, this);
        timer.start();
    }

    private void generateFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(30), rand.nextInt(30)); // 30x30 grid
    }

    private void restartGame() {
        restartButton.setVisible(false);
        startGame();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameStarted) {
            drawStartScreen(g);
            return;
        }

        if (gameOver) {
            drawGameOverScreen(g);
            restartButton.setVisible(true);
            return;
        }

        // Draw Snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * 20, p.y * 20, 20, 20);
        }

        // Draw Food
        g.setColor(Color.RED);
        g.fillRect(food.x * 20, food.y * 20, 20, 20);

        // Draw Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + score, 10, 20);
    }

    private void drawStartScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String title = "Snake Game";
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(title, (600 - metrics.stringWidth(title)) / 2, 200);

       //g.setFont(new Font("Arial", Font.PLAIN, 20));
        //g.drawString("Press the 'Start Game' button to begin!", 150, 250);
    }

    private void drawGameOverScreen(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String message = "Game Over";
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(message, (600 - metrics.stringWidth(message)) / 2, 200);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String scoreMessage = "Score: " + score;
        g.drawString(scoreMessage, (600 - metrics.stringWidth(scoreMessage)) / 2, 250);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            checkCollision();
            repaint();
        }
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case "UP" -> newHead.y--;
            case "DOWN" -> newHead.y++;
            case "LEFT" -> newHead.x--;
            case "RIGHT" -> newHead.x++;
        }
        snake.addFirst(newHead);

        if (newHead.equals(food)) {
            score++;
            generateFood();
        } else {
            snake.removeLast();
        }
    }

    private void checkCollision() {
        Point head = snake.getFirst();
        // Check if snake collides with the walls
        if (head.x < 0 || head.x >= 30 || head.y < 0 || head.y >= 30) {
            gameOver = true;
            timer.stop();
        }

        // Check if snake collides with itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                timer.stop();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> { if (!direction.equals("DOWN")) direction = "UP"; }
            case KeyEvent.VK_DOWN -> { if (!direction.equals("UP")) direction = "DOWN"; }
            case KeyEvent.VK_LEFT -> { if (!direction.equals("RIGHT")) direction = "LEFT"; }
            case KeyEvent.VK_RIGHT -> { if (!direction.equals("LEFT")) direction = "RIGHT"; }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
