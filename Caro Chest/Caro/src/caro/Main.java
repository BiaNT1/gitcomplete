package caro;


import javax.swing.JFrame;
import javax.swing.UIManager;

public class Main {

    public static JFrame myFrame;
    public static StartPanel myStartPanel = new StartPanel();
    public static GamePanel myGamePanel = new GamePanel();
    public static NetworkPanel myNetworkPanel = new NetworkPanel();
    public static ImagePanel twoLanPlayerPanel = new ImagePanel("picture/main.png", 0, 0, 800, 400);
    public static boolean startGame;

    public Main() {

        startGame = true; // MARK THIS GAME IS START 

      
        
        //  DEFINE GAME
         
        myFrame = new JFrame("Game");
        myFrame.setResizable(false);
        myFrame.setVisible(true);
        myFrame.setLayout(null);
        myFrame.setBounds(100, 100, 800, 600);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // SOUNDBASED
        MusicPanel myMusicPanel = new MusicPanel();
        myFrame.add(myMusicPanel);

        // SOUNDMAIN 
        SoundPanel mySoundPanel = new SoundPanel();

        // ADD BUTTON SOUND
        myFrame.add(mySoundPanel);
        myFrame.repaint();

        // START MENU GAME
        myFrame.add(myStartPanel);

    }

    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {

        try { //use this library make so beautiful
            UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
        } catch (Exception e) {
        };
        Main myMain = new Main();
    }

}
