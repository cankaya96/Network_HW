/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mybasicserver;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Furkan Cankaya
 */
public class sClient {

    public class ListenClientThread extends Thread {

        private sClient client;

        public ListenClientThread(sClient gClient) {
            this.client = gClient;

        }
        // client dinleme fonksiyonum
        public void run() {
            try {
                while (this.client.socket.isConnected()) {
                    Object rMessage = this.client.cInStream.readObject();
                    String message = rMessage.toString();
                    
                    // if one of the client send win message
                    if(message.contains("win")){
                        String clientId = message.split(":")[0];
                        // if message from first game
                        if (Integer.parseInt(clientId) < 3){
                            MyBasicServer.isThereWinner[0] = true;
                        }
                        // if message from second game
                        else{
                            MyBasicServer.isThereWinner[1] = true;
                        }
                        MyBasicServer.winnerClientID = clientId;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(sClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(sClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public static int readyClientNum = 0;
    public static int clientNo=0;
    int no;
    Socket socket;
    ObjectInputStream cInStream;

    ObjectOutputStream cOutStream;

    ListenClientThread listenThread;

    public sClient(Socket gSocket) throws IOException {
        sClient.clientNo++;
        this.no=sClient.clientNo;
        this.socket = gSocket;
        this.cInStream = new ObjectInputStream(this.socket.getInputStream());

        this.cOutStream = new ObjectOutputStream(this.socket.getOutputStream());

        this.listenThread = new ListenClientThread(this);
        this.listenThread.start();
            
    }

    public void sendMessage(Object message) throws IOException {
        this.cOutStream.writeObject(message);
    }

  

}
