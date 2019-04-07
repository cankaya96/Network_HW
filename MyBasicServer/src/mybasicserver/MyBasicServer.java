/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mybasicserver;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author Furkan Cankaya
 */
public class MyBasicServer {

    public class ListenServerThread extends Thread {

        private MyBasicServer server;

        ;

        public ListenServerThread(MyBasicServer gmyserver) {
            this.server = gmyserver;

        }

        // client dinleme fonksiyonum
        @Override
        public void run() {
            try {
                while (!this.server.sSocket.isClosed()) {
                    Socket cSocket = this.server.sSocket.accept();// client kabul eder ve client soketini döndürür
                    sClient newClient = new sClient(cSocket);
                    this.server.clientList.add(newClient);
                    // Send client no to new Client
                    SendMessade(newClient.no, newClient);
                    // If first, two client connect, new game start
                    if (this.server.clientList.size() == 2) {
                        StartGame(this.server.clientList.get(0),this.server.clientList.get(1));
                        GameProcess(this.server.clientList.get(0),this.server.clientList.get(1));
                    }
                    // If second, two client connect, new game start
                    else if(this.server.clientList.size() == 4){
                        StartGame(this.server.clientList.get(2),this.server.clientList.get(3));
                        GameProcess(this.server.clientList.get(2),this.server.clientList.get(3));
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(sClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public ServerSocket sSocket;
    

    public static void main(String[] args) {

    }
    public static boolean [] isThereWinner = {false,false};
    public static String winnerClientID = "";
    public ArrayList<sClient> clientList;
    public ListenServerThread ServerThread;

    public MyBasicServer(int port) {
        try {

            // TODO code application logic here
            sSocket = new ServerSocket(port);
            clientList = new ArrayList<sClient>();

        } catch (IOException ex) {
            Logger.getLogger(MyBasicServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void StartServer() throws IOException {
        //while (!sSocket.isClosed()) {
        this.ServerThread = new ListenServerThread(this);
        this.ServerThread.start();
        //   }
    }

    public void StopServer() throws IOException {
        sSocket.close();
    }

    public void SendBroadcastMessade(Object message) throws IOException {
        for (sClient gclient : clientList) {
            gclient.sendMessage(message);
        }

    }

    public void SendMessade(Object message, sClient gclient) throws IOException {
        gclient.sendMessage(message);
    }
    
    // generate card with random numbers
    public String generateCard() {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        Random rnd = new Random();
        String card = "";
        for (int i = 0; i < 13; i++) {
            int number = rnd.nextInt(89) + 1;
            while (numbers.contains(number)) {
                number = rnd.nextInt(89) + 1;
            }

            numbers.add(number);

        }
        int count = 0;
        for (int i = 0; i < 27; i++) {
            if (i % 2 == 1) {
                card = card + numbers.get(count).toString() + ",";
                count += 1;
            } else {
                card = card + "-1,";
            }
        }

        return card;
    }
    
    // Server Start new Game(Send generated card to User)
    public void StartGame(sClient fplayer,sClient secondplayer) throws IOException {
        String firstPlayerCard = generateCard();
        String secondPlayerCard = generateCard();
        SendMessade("fc:" + firstPlayerCard, fplayer);
        SendMessade("sc:" + secondPlayerCard, fplayer);
        SendMessade("fc:" + secondPlayerCard, secondplayer);
        SendMessade("sc:" + firstPlayerCard, secondplayer);
        SendMessade("start", fplayer);
        SendMessade("start", secondplayer);
    }
    
    // Server Game process with timer(Send new number to client)
    public void GameProcess(sClient fplayer,sClient secondplayer)   {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        Random rnd = new Random();
        java.util.Timer t = new java.util.Timer();

        t.schedule(
                new java.util.TimerTask() {
            @Override
            public void run() {
                
                int number = rnd.nextInt(89) + 1;
                while (numbers.contains(number)) {
                    number = rnd.nextInt(89) + 1;
                }

                numbers.add(number);
                if (!isThereWinner[fplayer.no/3]) {
                    try {
                        SendMessade("num:" + String.valueOf(number), fplayer);
                    } catch (IOException ex) {
                        Logger.getLogger(MyBasicServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        SendMessade("num:" + String.valueOf(number), secondplayer);
                    } catch (IOException ex) {
                        Logger.getLogger(MyBasicServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    try {
                        SendMessade("win:" + winnerClientID , fplayer);
                    } catch (IOException ex) {
                        Logger.getLogger(MyBasicServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        SendMessade("win:" + winnerClientID, secondplayer);
                    } catch (IOException ex) {
                        Logger.getLogger(MyBasicServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isThereWinner[fplayer.no/3] = false;
                    t.cancel();
                }

            }
        },
                3000, 5000
        );
    }
}
