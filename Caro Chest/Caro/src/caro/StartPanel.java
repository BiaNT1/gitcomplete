package caro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author 
 */
public class StartPanel extends JPanel {

    SoundPlayer mySound = new SoundPlayer();

    public StartPanel() {
        setLayout(null);
        setBounds(0, 0, 800, 600);

        /*PANEL START GAME */
        ImagePanel background = new ImagePanel("picture/StartGame.png", 0, 0, 800, 600);

        //ADD MORE FUNCTION BUTTON
        JButton oneButton = new JButton("1 Player");
        JButton twoButton = new JButton("2 Players");
        JButton LANButton = new JButton("LAN game");
        JButton exitButton = new JButton("Exit");

        // ADD BOUNDS OF BUTTON
        oneButton.setBounds(350, 300, 100, 30);
        twoButton.setBounds(350, 350, 100, 30);
        LANButton.setBounds(350, 400, 100, 30);
        exitButton.setBounds(350, 450, 100, 30);

        // FUNCTION FOREACH BUTTON
        oneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
             
                if (GamePanel.canPlaySound) {
                    mySound.playSound("sound/coin.mp3"); // PLAYSOUND
                }
                // START MENU MAIN
                Main.myFrame.remove(Main.myStartPanel);
                Main.myGamePanel = new GamePanel();
                Main.myGamePanel.numberPlayer = 1; 
                Main.myFrame.add(Main.myGamePanel);
                Main.myFrame.repaint();

            }
        });
        
// BUTTON2
        twoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            
                if (GamePanel.canPlaySound) {
                    mySound.playSound("sound/coin.mp3"); // SOUND
                }
                
                Main.myFrame.remove(Main.myStartPanel);
                Main.myGamePanel = new GamePanel();
                Main.myGamePanel.numberPlayer = 2;
                Main.myFrame.add(Main.myGamePanel);
                Main.myFrame.repaint();

            }
        });

        //BUTTON3
        LANButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (GamePanel.canPlaySound) {
                    mySound.playSound("sound/coin.mp3"); // SOUND
                }
                
                Main.myFrame.remove(Main.myStartPanel);
                NetworkPanel.joinButton.setEnabled(true);
                Main.myFrame.add(Main.myNetworkPanel);
                Main.myFrame.repaint();

            }
        });

        // BUTTON EXIT
        exitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(oneButton);
        add(twoButton);
        add(LANButton);
        add(exitButton);

        this.add(background);

    }

}
