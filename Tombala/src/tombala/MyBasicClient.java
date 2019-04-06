package tombala;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Furkan Cankaya
 */
public class MyBasicClient {
    public class ListenClientThread extends Thread {

        private MyBasicClient client;
        private String message;
        public ListenClientThread(MyBasicClient myclient) {
            this.client = myclient;
        }

        // client dinleme fonksiyonum
        @Override
        public void run() {
            while (cSocket.isConnected()) {
                try {
                    message = client.cInStream.readObject().toString();
                    // If message contains client ID
                    System.out.println("Message = " + message);
                    if(message.length() == 1){
                        FXMLDocumentController.clientID = Integer.valueOf(message);
                        System.out.println("Message = " + message);
                    }
                    else if(message.contains("fc")){
                        String tmp = message.split(":")[1];
                        String [] array = tmp.split(",");
                        int counter = 0;
                        for(int i = 0; i < FXMLDocumentController.myCardNumber.length; i++){
                            for(int j = 0; j < FXMLDocumentController.myCardNumber[0].length; j++){
                                FXMLDocumentController.myCardNumber[i][j] = Integer.parseInt(array[counter]);
                                counter += 1;
                            }
                        }
                    }
                    else if(message.contains("sc")){
                        String tmp = message.split(":")[1];
                        String [] array = tmp.split(",");
                        int counter = 0;
                        for(int i = 0; i < FXMLDocumentController.opponentCardNumber.length; i++){
                            for(int j = 0; j < FXMLDocumentController.opponentCardNumber[0].length; j++){
                                FXMLDocumentController.opponentCardNumber[i][j] = Integer.parseInt(array[counter]);
                                counter += 1;
                            }
                        }
                    }
                    else if(message.contains("start")){
                        System.out.println("Message1 = " + message);
                        FXMLDocumentController.isGameReady = true;
                    }
                    else if(message.contains("num")){
                        FXMLDocumentController.currentNumber = Integer.parseInt(message.split(":")[1]);
                        FXMLDocumentController.isCardChanged = true;
                    }
                    else if(message.contains("win")){
                        FXMLDocumentController.isGameFinished = true;
                        FXMLDocumentController.winnerID = Integer.parseInt(message.split(":")[1]);
                        
                        
                    }
                    
                    
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(MyBasicClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    Socket cSocket;
    String ip;
    int port;
    private ObjectOutputStream cOutStream;
    private ObjectInputStream cInStream;

    public MyBasicClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startClient() throws IOException {
        cSocket = new Socket(this.ip, this.port);
        cOutStream = new ObjectOutputStream(cSocket.getOutputStream());
        cInStream = new ObjectInputStream(cSocket.getInputStream());
        ListenClientThread listenThread = new ListenClientThread(this);
        listenThread.start();
                
    }

    public void closeClient() throws IOException {
        cSocket.close();
    }
    
    public void sendMessage(Object message) throws IOException{
        cOutStream.writeObject(message);
    }
    
}
