package caro;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author 
 */
public class Server {

    public ServerSocket listener;
  
    public Thread serverThread;
    static Vector<ClientHandle> clients = new Vector<ClientHandle>();
    
    class ClientHandle extends Thread {
        Socket soc;
        DataOutputStream dos;
        DataInputStream dis;
        boolean connecting = true;
        public ClientHandle(Socket soc) {
            try {
                this.soc = soc;
                dos = new DataOutputStream(soc.getOutputStream());
                dis = new DataInputStream(soc.getInputStream());
                System.out.print("next step");
            } catch (Exception e) {
                //TODO: handle exception
                connecting = false;
            }
        }
    }
    
    public Runnable serverAccept = new Runnable() {

        @Override
        public void run() {
            try {

                System.out.println("Tic Tac Toe Server is Running");

                while (true) {

                    Game game = new Game();

                    Game.Player playerX = game.new Player(listener.accept(), 'X');

                    Game.Player playerO = game.new Player(listener.accept(), 'O');
                                                        
                    playerX.setOpponent(playerO);
                    playerO.setOpponent(playerX);
                    game.currentPlayer = playerX;
                    playerX.start();
                    playerO.start();
                    

                }
            } catch (Exception ex) {
                System.out.println("SERVER DISCONNECT...");
               
               
            } finally {
                try {
                    listener.close();

                } catch (Exception e) {

                }
            }
        }

    };

    public Server() {
    
        try {
            listener = new ServerSocket(8328);
            /* while(true) {
                Socket soc = listener.accept();
                ClientHandle clh = new ClientHandle(soc);
                clients.add(clh);
                if(clients.size() > 1000) {
                    System.out.println("Error because joiner > 1000");
                } else {
                    Vector<> room = new Vector<>();
                    for(int i=0; i<clients.size()/2; i+=2) {
                                           
                    }
                }
                serverThread = new Thread(serverAccept);        
                serverThread.start();
            }      */       
        } catch (Exception ex) {

        }
        serverThread = new Thread(serverAccept);        
        serverThread.start();
        
    }

   

    /**
     * A two-player game.
     */
    public class Game {

        /*
         * 16X16 BOARD
         * 
         */
        private Player[][] board = new Player[16][16];
        private int[][] status = new int[16][16];

        public Game() {
            for (int row = 0; row < 16; row++) {
                for (int col = 0; col < 16; col++) {
                    board[row][col] = null;
                    status[row][col] = 0;
                }
            }
        }

      
        Player currentPlayer;  

        /*
         * 
         * CHECK CURRENPLAYER WIN OR NOT ?
         */
        public boolean hasWinner(int row, int col) {

            Check myCheck = new Check(16, 16);
            int prePlayer = (currentPlayer.mark == 'X' ? 2 : 1);
            System.out.println("Player " + prePlayer + "Win?" + myCheck.checkIt(row, col, status, prePlayer));
            if (myCheck.checkIt(row, col, status, prePlayer)) {
                
                return true;
               
            } else {
                return false;
            }

        }

        /*
        * CHECK THIS BOARD IS FULL ?
         */
        public boolean boardFilledUp() {
            for (int row = 0; row < 16; row++) {
                for (int col = 0; col < 16; col++) {
                    if (board[row][col] == null) {
                        return false;
                    }
                }
            }
            return true;

        }

        
        public synchronized boolean legalMove(int location, Player player) {
            System.out.println("location " + location);
            int row = location / 16;
            int col = location % 16;
            if (player == currentPlayer && board[row][col] == null  ) {
                board[row][col] = currentPlayer;
                status[row][col] = (currentPlayer.mark == 'X' ? 1 : 2);
                currentPlayer = currentPlayer.opponent;
                currentPlayer.otherPlayerMoved(location);
                return true;
            }
            return false;

        }

       
        /* 
            EVERY PLAYER IS A THREAD
         */
        class Player extends Thread { 

            char mark;
            Player opponent;
            Socket socket;
            BufferedReader input;
            PrintWriter output;

            /*
               PROMT WELCOME TO START      
             */
            public Player(Socket socket, char mark) {
                this.socket = socket;
                this.mark = mark;
                try {
                    input = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    output = new PrintWriter(socket.getOutputStream(),true);
                    System.out.println("sent: " + "WELCOME " + mark);
                    output.println("WELCOME " + mark);
                    output.println("MESSAGE Wait another connects");
                   
                    
                } catch (IOException e) {
                    System.out.println("Player died: " + e);
                }
            }

            /*
             * SET ENEMY
             */
            public void setOpponent(Player opponent) {
                this.opponent = opponent;
            }

            /*
             * 
             */
            public void otherPlayerMoved(int location) {
                System.out.println("sent: OPPONENT_MOVED " + location);
                output.println("OPPONENT_MOVED " + location);

                System.out.println(
                        hasWinner(location / 16, location % 16) ? "DEFEAT" : boardFilledUp() ? "TIE" : "");
                output.println(
                        hasWinner(location / 16, location % 16) ? "DEFEAT" : boardFilledUp() ? "TIE" : "");

            }

            /*
                RUN OF THREAD'S PLAYER
             */
            public void run() {
                try {
                    // TWO PLAYER CONNECTED
                    output.println("MESSAGE All is connected");
                
                    Client.isStartGame = true ; 
                
                    // FIRST PLAYER : X
                    if (mark == 'X') {
                        output.println("MESSAGE First turn");
                       
                       
                    }
                    
                    // 
                    while (true) {
                        String command = input.readLine();
                        if (command.startsWith("MOVE")  ) {
                            int location = Integer.parseInt(command.substring(5));
                            if (legalMove(location, this)) {
                                output.println("VALID_MOVE");
                                output.println(hasWinner(location / 16, location % 16) ? "VICTORY"
                                        : boardFilledUp() ? "TIE"
                                        : "");
                            } else {
                                output.println("MESSAGE This is not your turn");
                            }
                        } else if (command.startsWith("QUIT")) {
           
                            return;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Player died: " + e);
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

    }

}
