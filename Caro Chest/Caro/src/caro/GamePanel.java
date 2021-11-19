package caro;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 * @author 
 */
public class GamePanel extends JPanel {

    public static int winner; // player 1 win -> 1 ;  player 2 win -> 2  ; computer win->3 ;draw ->0   
    public int height = 16 ; 
    public int width  = 16 ;   
    public int numberPlayer;
    public int player; 
    public StatusBoard myStatus; 
    public int address; 
  
    public SoundPlayer mySound = new SoundPlayer(); 
    public static boolean canPlaySound = true; 
    
     
    public static ImagePanel backgroundPanel; 
    public ImagePanel tablePanel; 
    public BackButton myBackButton; 
   
    public Computer myComputer; 
    public Check myCheck; 
  
 
    public MouseAdapter myAction; 
 
 


    public GamePanel() {
        
        winner = -1 ; 
        setBounds(0, 0, 800, 600);
        setLayout(null);

        height = 16;
        width = 16;

        myCheck = new Check(height, width);
        myStatus = new StatusBoard(height, width);
        myComputer = new Computer(height, width);

        // add button back to menu 
        myBackButton = new BackButton("GamePanel");
        add(myBackButton);

        // mainPanel 
        player = 1;  // PLAYER 1

        backgroundPanel = new ImagePanel("picture/main.png", 0, 0, 800, 600);

        // PANEL TABLE
        tablePanel = new ImagePanel("picture/table.png", 20, 20, 480, 480);

        
        ImagePanel[][] mySquare = new ImagePanel[16][16];
       normalGame();
       
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                myStatus.statusBoard[i][j] = 0;
                mySquare[i][j] = new ImagePanel("picture/khung.png", i * 30, j * 30, 30, 30);
                tablePanel.add(mySquare[i][j]);

                // action of normal game
             

                mySquare[i][j].addMouseListener(myAction);
            }
        }

        repaint();

        //
        add(tablePanel);

        add(backgroundPanel);

    }

    public void normalGame() {
        player = 1;
        myAction = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (canPlaySound) {
                    mySound.playSound("sound/kick.mp3");
                }

                if (Main.startGame == true) {
                    Main.myFrame.repaint();
                    ImagePanel a = (ImagePanel) e.getComponent();
                    address = tablePanel.getComponentZOrder(a);  
                    //System.out.println("adress " + address );
                    int row = address % 16;
                    int col = address / 16;
                    // System.out.println("index " + row +" " +col +" " +status[row][col] );

                    if (myStatus.statusBoard[row][col] == 0) {

                        if (player == 1) {

                            a.setPicture("picture/khung1.png");

                            myStatus.setStatus(row, col, player);
                            repaint();

                            System.out.println("index " + row + " " + col + " " + myStatus.statusBoard[row][col]);
                            // kiem tra 
                      
                            if (myCheck.checkIt(row, col, myStatus.statusBoard, player) == true) {
                                Main.startGame = false;
                                winner = 1;
                                winnerFrame myWinnerFrame = new winnerFrame(1);

                                System.out.println(" Player 1  win !");
                            } else if (myCheck.isDraw(myStatus.statusBoard)) {
                                Main.startGame = false;
                                winner = 0;
                                winnerFrame myWinnerFrame = new winnerFrame(0);
                                System.out.println("Draw");
                            }
                            player = 2;

                            if (numberPlayer == 1) { 

                                
                                myComputer.calculateEvalBoard(player, myStatus.statusBoard);
                                // myComputer.printEvalBoard(); 
                                do {
                                    Computer myComputer = new Computer(height, width);
                                    myComputer.calculateEvalBoard(player, myStatus.statusBoard);
                                    myComputer.FindMove(myStatus.statusBoard);
                                    row = myComputer.optimalX;
                                    col = myComputer.optimalY;

                                } while (myStatus.statusBoard[row][col] != 0);

                                System.out.println("COm index " + row + " " + col + " " + myStatus.statusBoard[row][col]);
                                ImagePanel b = new ImagePanel("picture/khung2.png", col * 30, row * 30, 30, 30);
                                tablePanel.add(b);
                                repaint();
                                myStatus.statusBoard[row][col] = 2;
                                if (myCheck.checkIt(row, col, myStatus.statusBoard, player) == true) {
                                    Main.startGame = false;
                                    winner = 3;
                                    winnerFrame myWinnerFrame = new winnerFrame(3);

                                    System.out.println(" Computer win !");
                                } else if (myCheck.isDraw(myStatus.statusBoard)) {
                                    Main.startGame = false;
                                    winner = 0;
                                    winnerFrame myWinnerFrame = new winnerFrame(0);
                                    System.out.println("Draw");
                                }
                                player = 1;

                            }

                        } else if (player == 2) {
                            a.setPicture("picture/khung2.png");
                            myStatus.setStatus(row, col, player);
                            repaint();
                            System.out.println("index " + row + " " + col + " " + myStatus.statusBoard[row][col]);
                            
                           
                            if (myCheck.checkIt(row, col, myStatus.statusBoard, player) == true) {
                                Main.startGame = false;
                                winner = 2;
                                winnerFrame myWinnerFrame = new winnerFrame(2);
                                System.out.println(" Player 2 win !");

                            } else if (myCheck.isDraw(myStatus.statusBoard)) {
                                Main.startGame = false;
                                winner = 0;
                                winnerFrame myWinnerFrame = new winnerFrame(0);
                                System.out.println("Draw");
                            }
                            player = 1;

                        }

                    }

                }

            }

        };
    }
}
