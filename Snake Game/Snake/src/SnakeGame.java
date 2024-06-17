import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Title {
        int x;
        int y;

        Title(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardheight;
    int boardwidth;
    int titleSize = 25;

    // Snake
    Title Snakehead;
    ArrayList<Title> Snakebody;

    // Food
    Title Food;

    // Random
    Random random;

    // Gamelogic
    javax.swing.Timer gameloop;

    // Velocity
    int velocityX;
    int velocityY;
    boolean gameover = false;

    SnakeGame(int boardwidth, int boardheight) {
        this.boardheight = boardheight;
        this.boardwidth = boardwidth;
        setPreferredSize(new Dimension(this.boardheight, this.boardwidth));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        Snakehead = new Title(5, 5);
        Snakebody = new ArrayList<Title>();

        Food = new Title(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameloop = new javax.swing.Timer(100, this);
        gameloop.start();

    }

    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    public void draw(Graphics g) {

        // Grid
        // for (int i = 0; i < boardwidth / titleSize; i++) {
        // //x1,y1,x2,y2
        // g.drawLine(i * titleSize, 0, i * titleSize, boardheight);
        // g.drawLine(0, i * titleSize, boardwidth, i * titleSize);
        // }
        // SnakeHead
        g.setColor(Color.white);
        // g.fillRect(Snakehead.x * titleSize, Snakehead.y * titleSize, titleSize,
        // titleSize);
        g.fill3DRect(Snakehead.x * titleSize, Snakehead.y * titleSize, titleSize, titleSize, true);

        // SnakeBody
        for (int i = 0; i < Snakebody.size(); i++) {
            Title SnakePart = Snakebody.get(i);
            g.fillRect(SnakePart.x * titleSize, SnakePart.y * titleSize, titleSize, titleSize);
            g.fill3DRect(SnakePart.x * titleSize, SnakePart.y * titleSize, titleSize, titleSize, true);
        }

        // Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        if (gameover) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(Snakebody.size()), titleSize - 16, titleSize);
        } else {
            g.drawString("Score: " + String.valueOf(Snakebody.size()), titleSize - 16, titleSize);
        }

        // Food
        g.setColor(Color.red);
        // g.fillRect(Food.x * titleSize, Food.y * titleSize, titleSize, titleSize);
        g.fill3DRect(Food.x * titleSize, Food.y * titleSize, titleSize, titleSize, true);

    }

    public void placeFood() {
        Food.x = random.nextInt(boardwidth / titleSize);
        Food.y = random.nextInt(boardheight / titleSize);
    }

    public boolean collision(Title title1, Title title2) {
        return title1.x == title2.x && title1.y == title2.y;
    }

    public void move() {
        // eat food
        if (collision(Snakehead, Food)) {
            Snakebody.add(new Title(Food.x, Food.y));
            placeFood();
        }

        // Snakebody
        for (int i = Snakebody.size() - 1; i >= 0; i--) {
            Title SnakePart = Snakebody.get(i);
            if (i == 0) {
                SnakePart.x = Snakehead.x;
                SnakePart.y = Snakehead.y;
            } else {
                Title prevSnakePart = Snakebody.get(i - 1);
                SnakePart.x = prevSnakePart.x;
                SnakePart.y = prevSnakePart.y;
            }
        }
        // head
        Snakehead.x += velocityX;
        Snakehead.y += velocityY;

        // gameover

        for (int i = 0; i < Snakebody.size(); i++) {
            Title SnakePart = Snakebody.get(i);

            // collision with snake part
            if (collision(Snakehead, SnakePart)) {
                gameover = true;
            }
        }
        if (Snakehead.x * titleSize < 0 || Snakehead.x * titleSize > boardwidth || Snakehead.y * titleSize < 0
                || Snakehead.y * titleSize > boardheight)

        {
            gameover = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameover) {
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }

        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }

        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }

        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    // Not Need
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
