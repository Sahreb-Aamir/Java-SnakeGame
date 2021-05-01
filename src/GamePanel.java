import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    final static int SCREEN_HEIGHT = 600;
    final static int SCREEN_WIDTH = 600;
    final static int UNIT_SIZE = 25; //How large each square will be
    final static int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // Total number of units in the game
    final static int DELAY = 100;
    final int x[] = new int[GAME_UNITS]; //Size of the snake in X plane
    final int y[] = new int[GAME_UNITS]; //Size of the snake in Y plane
    private JButton again;
    int bodyParts = 7; //Initial size of Snake
    int foodEaten = 0;
    int foodX;
    int foodY;
    char direction = 'R'; //Starting direction Snake is moving
    boolean running = false;
    Timer timer;
    Random random;
    boolean gameOn;


    GamePanel(){
        random = new Random();
        gameOn = false;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH , SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter()); //Listener to key's pressed.
        again = new JButton ("Play Again?");
        again.setVisible(false);
        this.add(again);
        startGame();
    }
    public void startGame(){
        createFood();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics G){
        super.paintComponent(G);
        draw(G);
    }
    public void draw(Graphics G){
        if(running) {
            //Create Grid
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                G.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);//Draw lines for X axis
//                G.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);//Draw lines for Y axis
//            }

            G.setColor(Color.blue);
            G.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if ( i == 0 ) {
                    G.setColor(Color.GREEN);
                    G.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    G.setColor(new Color(43, 172, 0));
                    //G.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    G.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            G.setColor(Color.RED);
            G.setFont( new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(G.getFont());
            G.drawString("Score : " + foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score : " + foodEaten))/2, G.getFont().getSize());
        }
        else{
            gameOver(G);
        }

    }
    public void createFood(){
        foodX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE); //Create random int within bounds of units Horizontal
        foodY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE); //Create random int within bounds of units Vertical
        foodX *= UNIT_SIZE; //Multiply to get pixel location of where food will be placed.
        foodY *= UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkFood(){
        if ((x[0] == foodX) && (y[0] == foodY)){
            bodyParts++;
            foodEaten++;
            createFood();
        }
    }
    public void checkObstructions(){
        //Check if head crashed into the body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && y[0] == y[i]){
                running = false;
            }
        }
        //Check if head crashes in Horizontal walls
        if ( x[0] < 0 || x[0] > SCREEN_WIDTH){
            running = false;
        }
        //Check if head crashes into Vertical walls
        if ( y[0] < 0 || y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics G){
        G.setColor(Color.RED);
        G.setFont( new Font("Ink Free", Font.BOLD, 100));
        FontMetrics metrics = getFontMetrics(G.getFont());
        G.drawString("Good Try!", (SCREEN_WIDTH - metrics.stringWidth("Good Try!"))/2, SCREEN_HEIGHT/2);
        G.setColor(Color.RED);
        G.setFont( new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(G.getFont());
        G.drawString("Score : " + foodEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score : " + foodEaten))/2, SCREEN_HEIGHT/2 + G.getFont().getSize());

        //Show play again button
        again.setLocation((SCREEN_WIDTH - again.getWidth()) / 2, SCREEN_HEIGHT/2 + G.getFont().getSize() + 20 );
        again.setVisible(true);
        again.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                SnakeGame.main(null);
            }
        });
    }

    private void resume(){
        gameOn = false;
        timer.start();
    }

    private void pause(){
        gameOn = true;
        timer.stop();
    }


    @Override
    public void actionPerformed( ActionEvent e ) {
        if(running){
            move();
            checkFood();
            checkObstructions();
        }
        repaint();
    }


    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed( KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(gameOn){
                        resume();
                    }
                    else{
                        pause();
                    }
            }
        }
    }
}
