import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){

        this.add(new GamePanel());
        this.setTitle("Sahreb's Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //What to do on close
        this.setResizable(false);
        this.pack(); //Wraps JFrame around GameFrame so that it will be the correct size.
        this.setVisible(true);
        this.setLocationRelativeTo(null); //Shows the window in the middle of the screen.


    }
}
