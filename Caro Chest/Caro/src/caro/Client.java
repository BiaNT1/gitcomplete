
package caro;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class Client {

    
    private JLabel messageLabel = new JLabel("");
    private String URLIcon;
    private String URLOpenentICon;
    public ImagePanel background = new ImagePanel("picture/main.png", 0, 0, 800, 600);
    public ImagePanel waitingPicture = new ImagePanel("picture/waiting2.png", 500, 350, 300, 225);
    public ImagePanel attackPicture = new ImagePanel("picture/attack1.png", 500, 350, 300, 225);
    public ImagePanel bgChat ;
    public ImagePanel waitingConnectPicture = new ImagePanel("picture/WaitingConnect.png", 500, 350, 300, 225);
    private ImagePanel[][] board = new ImagePanel[16][16];
    private ImagePanel currentSquare;
    private JTextArea areaChat;
    private JButton btnChat;
    private JTextField txtChat;    
    public static BackButton myBackButton;


    public SoundPlayer mySoundPlayer = new SoundPlayer();
    //properties to access
    private static int PORT = 8328;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

   //Properties status
    public static boolean isStartGame;
    char mark;

    public Client(String serverAddress) {
        try {
            isStartGame = false;
            // Setup networking
            socket = new Socket(serverAddress, PORT);
            input = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception ex) {
        }

        // Layout GUI
        ImagePanel messagePanel = new ImagePanel("picture/message.png", 20, 500, 480, 40);
        messageLabel.setBounds(20, 4, 300, 30);
        messagePanel.add(messageLabel);
        background.add(messagePanel);

        myBackButton = new BackButton("towLanPlayerPanel");
        myBackButton.setEnabled(false);
        background.add(myBackButton);
        background.repaint();
        
        areaChat = new JTextArea();
        areaChat.setBounds(500, 100, 300, 200);
        background.add(areaChat);

        txtChat = new JTextField();
        txtChat.setBounds(500,300,200,50);
        background.add(txtChat);

        btnChat = new JButton();
        btnChat.setText("Enter");
        btnChat.setBounds(700,300,90,50);
        background.add(btnChat);

        

        // add to waiting pictrue

        background.add(waitingConnectPicture);

        ImagePanel boardPanel = new ImagePanel("picture/table.png", 20, 20, 480, 480);

        boardPanel.setLayout(null);
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                final int k = row;
                final int l = col;
                board[row][col] = new ImagePanel("picture/khung.png", col * 30, row * 30, 30, 30);
                board[row][col].repaint();
                background.repaint();

                board[row][col].addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                      // play sound 

                        if (GamePanel.canPlaySound) {
                            mySoundPlayer.playSound("sound/kick.mp3");
                        }

                        currentSquare = board[k][l];

                        System.out.println("Client : isStartGame = " + isStartGame);
                        if (isStartGame) {
                            System.out.println("MOVE " + (k * 16 + l));
                            output.println("MOVE " + (k * 16 + l));
                        }

                    }

                });
                boardPanel.add(board[row][col]);
                board[row][col].repaint();
                background.repaint();

            }

        }
        background.add(boardPanel);

    }

   
    public void play() {
        String response;
        try {
            response = input.readLine();
            if (response.startsWith("WELCOME")) {

                mark = response.charAt(8);
                URLIcon = (mark == 'X' ? "picture/khung1.png" : "picture/khung2.png");
                URLOpenentICon = (mark == 'X' ? "picture/khung2.png" : "picture/khung1.png");

                if (mark == 'X') {
                    waitingPicture.setPicture("picture/waiting2.png");
                    attackPicture.setPicture("picture/attack1.png");
                } else if (mark == 'O') {
                    waitingPicture.setPicture("picture/waiting1.png");
                    attackPicture.setPicture("picture/attack2.png");

                    isStartGame = true;
                    background.remove(waitingConnectPicture);
                    background.add(waitingPicture);

                }

            }
            while (true) {
                response = input.readLine();
                background.repaint();
                Main.myFrame.repaint();
                if (response.startsWith("VALID_MOVE")) {

                    currentSquare.setPicture(URLIcon);

                    background.remove(attackPicture);

                    background.add(waitingPicture);

                    messageLabel.setText("");
                    background.repaint();
                    Main.myFrame.repaint();

                } else if (response.startsWith("OPPONENT_MOVED")) {

                    background.remove(waitingPicture);

                    background.add(attackPicture);

                    int location = Integer.parseInt(response.substring(15));
                    int row = location / 16;
                    int col = location % 16;
                    board[row][col].setPicture(URLOpenentICon);
                    background.repaint();
                    Main.myFrame.repaint();
                    myBackButton.setEnabled(true); 

                    messageLabel.setText("");

                } else if (response.startsWith("VICTORY")) {
                       // Sound
                    if (GamePanel.canPlaySound) {
                        mySoundPlayer.playSound("sound/win.mp3");
                    }
                    
                    // Notifications
                    Icon myIcon = new ImageIcon(getClass().getResource("picture/winner" + (mark == 'X' ? '1' : '2') + ".gif"));
                    JOptionPane.showMessageDialog(null, null, "Winn", JOptionPane.INFORMATION_MESSAGE, myIcon);
                 

                    messageLabel.setText("YOU WIN");
                    Main.myFrame.repaint();
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    messageLabel.setText("YOU LOST");
                    // Sound
                    if (GamePanel.canPlaySound) {
                        mySoundPlayer.playSound("sound/GameOver.mp3");
                    }
                    
                    // Notifications
                    Icon myIcon = new ImageIcon(getClass().getResource("picture/loser" + (mark == 'X' ? '1' : '2') + ".gif"));
                    JOptionPane.showMessageDialog(null, null, "Thua!", JOptionPane.INFORMATION_MESSAGE, myIcon);

                  
                    // 

                    Main.myFrame.repaint();
                    break;
                } else if (response.startsWith("TIE")) {
                    messageLabel.setText("PLAYER TIE");
                    Main.myFrame.repaint();
                    break;
                } else if (response.startsWith("MESSAGE")) {

                    if ("MESSAGE First turn".equals(response)) {
                        background.remove(waitingConnectPicture);
                        background.add(attackPicture);
                        messageLabel.setText("GAME STARTING....");
                    } else if ("MESSAGE All is connected".equals(response)) {
                        isStartGame = true;
                        messageLabel.setText("ACCESSTING");
                    } else if ("MESSAGE This is not your turn".equals(response)) {
                        messageLabel.setText("NOT MY TURN");
                    } else if ("MESSAGE Wait another connects".equals(response)) {
                        messageLabel.setText("WAIT TO ANOTHER PLAYER ACCESS");
                    } else {
                        messageLabel.setText(response.substring(8));
                    }

                    Main.myFrame.repaint();
                }
            }
            output.println("QUIT");
        } catch (Exception ex) {

        } finally {
            try {
                socket.close();
            } catch (Exception ex) {
            }

        }
    }

    /*
     * The methord: Do you want restart game?
     */
    public boolean wantsToPlayAgain() {

        int response = JOptionPane.showConfirmDialog(null,
                "PLAY AGAIN?",
                "GAME CARO",
                JOptionPane.YES_NO_OPTION);
        isStartGame = true;
        return response == JOptionPane.YES_OPTION;

    }

  /*   public ChatPanel() {

    } */

    /**
     * Runs the client as an application.
     */
   
}
