/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tombala;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javax.swing.JOptionPane;

/**
 *
 * @author furkancankaya
 */
public class FXMLDocumentController implements Initializable {
    // GUI Elements initialization
    @FXML
    private Button startButton;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView searchImage;
    @FXML
    private ImageView searchText;
    @FXML
    private ImageView gameNameImage;
    
    @FXML
    private Button backToMenuB;
    @FXML
    private Button FirstCinkoB;
    @FXML
    private Button SecondCinkoB;
    @FXML
    private Label InfoLabel;
    @FXML
    private Button TombalaB;
    @FXML
    private Text opponentText;
    @FXML
    private GridPane UserPane;
    @FXML
    private GridPane OpponentPane;
    @FXML
    private Label currentNumLabel;
    
    MyBasicClient myClient;
    
    public static int clientID = -1;
    public static boolean isGameReady = false;
    public static boolean isCardChanged = false;
    public static boolean isGameFinished = false;
    public static int winnerID = -1;
    public static int currentNumber;
    public static int[][] myCardNumber = new int[3][9];
    public static int[][] opponentCardNumber = new int[3][9];
    
    ArrayList<Integer> allNumbers = new ArrayList<Integer>();
   
    // Function for control clicks on Start Game button
    @FXML
    private void startGameAction(ActionEvent event) throws IOException, InterruptedException, ClassNotFoundException{
        myClient = new MyBasicClient("127.0.0.1", 7000);
        myClient.startClient();
        myClient.sendMessage("Start");
        
        System.out.println("Game Waiting...");
        // TODO : Send play game request to Server
        
        // Set visibility of object for search opponent page
        loadNewScene("Search");
        
        
        
        System.out.println("ClientID = " + clientID);
        // Send request with one seconds for are there any opponent(Infinite Loop)
        
        loadNewScene("Game");
        
        // Set all cards
        setNumbersOnCard(myCardNumber, 0); // Set Numbers for User
        setNumbersOnCard(opponentCardNumber, 1); // Set Numbers for Opponent
        
        // Game controls
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(() -> {
                if(isGameFinished){
                    if(winnerID == clientID){
                        InfoLabel.setText("TEBRİKLER ! KAZANDINIZ.");
                    }else{
                        InfoLabel.setText("ÜZGÜNÜZ KAYBETTİNİZ...");
                    }
                  
                }
                if(currentNumber != 0){
                    currentNumLabel.setText("Çıkan Sayı : " + String.valueOf(currentNumber));
                    isCardChanged = false;
                    allNumbers.add(currentNumber);
                    controlNumberOnCard(currentNumber);
                }
            });
        }
        }, 100, 1000);
        
    
        
    }
    
    // Function for control clicks on Exit Game button
    @FXML
    private void exitGameAction(ActionEvent event) {
        System.exit(0);
    }
    
    // Function for go back to Main Menu
    @FXML
    private void BackToMenuAction(ActionEvent event) throws IOException, InterruptedException {
        // Clear All Pane
        Node node = UserPane.getChildren().get(0);
        UserPane.getChildren().clear();
        UserPane.getChildren().add(0,node);
        
        node = OpponentPane.getChildren().get(0);
        OpponentPane.getChildren().clear();
        OpponentPane.getChildren().add(0,node);
        
        loadNewScene("Main");
    }
    
    
    @FXML
    private void FirstCinkoAction(ActionEvent event) throws IOException {
        if(CinkoNumberControl() == 1){
            if(clientID == 1 || clientID == 2){
                System.out.println(String.valueOf(clientID) + ". oyuncu birinci çinkoyu tamamladı !");
            }
            else{
                System.out.println(String.valueOf(clientID-2) + ". oyuncu birinci çinkoyu tamamladı !");
            }
        }
    }
    @FXML
    private void SecondCinkoAction(ActionEvent event) throws IOException {
        if(CinkoNumberControl() == 2){
            if(clientID == 1 || clientID == 2){
                System.out.println(String.valueOf(clientID) + ". oyuncu ikinci çinkoyu tamamladı !");
            }
            else{
                System.out.println(String.valueOf(clientID-2) + ". oyuncu ikinci çinkoyu tamamladı !");
            }
        }
    }
    @FXML
    private void TombalaAction(ActionEvent event) throws IOException {
        if(controlTombalaSituation()){
            myClient.sendMessage(String.valueOf(clientID)+":win");
        }
    }
    
    
    // Helper Functions
    private boolean controlTombalaSituation(){
        // Each Row
        int counter = 0;
        for (int i = 0; i < myCardNumber.length; i++) {
            // Each column
            for (int j = 0; j < myCardNumber[0].length; j++) {
                if(allNumbers.contains(myCardNumber[i][j])){
                    counter += 1;
                }
            }
        }
        if(counter == 13){
            return true;
        }
        return false;
        
    }
    
    private void controlNumberOnCard(int number){
        for(int i = 0; i < myCardNumber.length; i++){
            for(int j = 0; j < myCardNumber[0].length; j++){
                if(myCardNumber[i][j] == number){
                    fillNumberInUserCard(i, j);
                }
                if(opponentCardNumber[i][j] == number){
                    fillNumberInOpponentCard(i, j);
                }
            }
        }
    }
    
    private void fillNumberInUserCard(int i, int j){
        Rectangle rectangle = new Rectangle(0, 0, 50, 50);
        rectangle.setFill(Color.RED);
        GridPane.setRowIndex(rectangle, i);
        GridPane.setColumnIndex(rectangle, j);
        UserPane.getChildren().addAll(rectangle);
        
    }
    
    private void fillNumberInOpponentCard(int i, int j){
        Rectangle rectangle = new Rectangle(0, 0, 15, 20);
        rectangle.setFill(Color.RED);
        GridPane.setRowIndex(rectangle, i);
        GridPane.setColumnIndex(rectangle, j);
        OpponentPane.getChildren().addAll(rectangle);
        
    }
    
    private void setNumbersOnCard(int[][] numbers, int isUser){
        // Each Row
        for (int i = 0; i < 3; i++) {
            // Each column
 
            for (int j = 0; j < 9; j++) {          
                if(numbers[i][j] != -1){
                    Label curr = new Label(String.valueOf(numbers[i][j]));
                    if(isUser == 0){
                        UserPane.add(curr, j, i);
                        UserPane.setHalignment(curr, javafx.geometry.HPos.CENTER);
                    }else{
                        OpponentPane.add(curr, j, i);
                        OpponentPane.setHalignment(curr, javafx.geometry.HPos.CENTER);
                    }
                
                }
                
                
            }
        }
    }
    
    public int CinkoNumberControl(){
        
        
        return 1;
        
    }
    
    private void loadNewScene(String Name) throws IOException, InterruptedException{
        
        if(Name.contains("Main")){
            // Set visibility of object for new page
            startButton.setVisible(true);
            exitButton.setVisible(true);
            searchImage.setVisible(false);
            searchText.setVisible(false);
            backToMenuB.setVisible(false);
            FirstCinkoB.setVisible(false);
            SecondCinkoB.setVisible(false);
            TombalaB.setVisible(false);
            opponentText.setVisible(false);
            UserPane.setVisible(false);
            OpponentPane.setVisible(false);
            gameNameImage.setVisible(true);
            currentNumLabel.setVisible(false);
            InfoLabel.setVisible(false);
            
        }else if (Name.contains("Search")){
            System.out.println("tombala.FXMLDocumentController.loadNewScene()");
            // Set visibility of object for Search page
            startButton.setVisible(false);
            exitButton.setVisible(false);
            searchImage.setVisible(true);
            searchText.setVisible(true);
            backToMenuB.setVisible(true);
            while(true){
                Thread.sleep(1000);
                if(isGameReady){
                    break;
                }
            }
                
            
            
        }else{
            // Set visibility of object for Game page
            searchImage.setVisible(false);
            searchText.setVisible(false);
            FirstCinkoB.setVisible(true);
            SecondCinkoB.setVisible(true);
            TombalaB.setVisible(true);
            opponentText.setVisible(true);
            UserPane.setVisible(true);
            OpponentPane.setVisible(true);
            gameNameImage.setVisible(false);
            currentNumLabel.setVisible(true);
            InfoLabel.setVisible(true);
            backToMenuB.setVisible(false);
            
        }
        
    }
    
    public void giveAlertMessage(String Text){
        Alert alert = new Alert(AlertType.INFORMATION);
        
        alert.setHeaderText(Text);
        alert.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
