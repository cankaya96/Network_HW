/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tombala;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
    private Button ContinueButton;
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
    
    
   
    // Function for control clicks on Start Game button
    @FXML
    private void startGameAction(ActionEvent event) throws IOException{
        System.out.println("Game Waiting...");
        // TODO : Send play game request to Server
        
        // Set visibility of object for search opponent page
        loadNewScene("Search");
        
        // Send request with one seconds for are there any opponent(Infinite Loop)
        
        loadNewScene("Game");
        int [] numbersOnCard = new int [12];
        Random rnd = new Random();
        for(int i = 0; i < numbersOnCard.length; i++){
            numbersOnCard[i] = rnd.nextInt(90);
            System.out.println(numbersOnCard[i]);
        }
        setNumbersOnCard(numbersOnCard, 0); // Set Numbers for User
        setNumbersOnCard(numbersOnCard, 1); // Set Numbers for Opponent
        
    }
    
    // Function for control clicks on Exit Game button
    @FXML
    private void exitGameAction(ActionEvent event) {
        System.exit(0);
    }
    
    // Function for go back to Main Menu
    @FXML
    private void BackToMenuAction(ActionEvent event) throws IOException {
        // Clear All Pane
        Node node = UserPane.getChildren().get(0);
        UserPane.getChildren().clear();
        UserPane.getChildren().add(0,node);
        
        node = OpponentPane.getChildren().get(0);
        OpponentPane.getChildren().clear();
        OpponentPane.getChildren().add(0,node);
        
        loadNewScene("Main");
    }
    
    // Helper Functions
    private void controlNumberOnCard(int number){
        
    }
    
    
    private void setNumbersOnCard(int[] numbers, int isUser){
        Random rnd = new Random();
        ArrayList<Integer> filledPlace = new ArrayList<Integer>();
        // Each Row
        for (int i = 0; i < 3; i++) {
            // Each column
            filledPlace.clear();
            for (int j = 0; j < 4; j++) {
                int numberPlace = rnd.nextInt(9);
                while(filledPlace.contains(numberPlace)){
                    numberPlace = rnd.nextInt(9);
                }
                filledPlace.add(numberPlace);             
                Label curr = new Label(String.valueOf(numbers[(i*4) + j]));
                if(isUser == 0){
                    UserPane.add(curr, numberPlace, i);
                    UserPane.setHalignment(curr, javafx.geometry.HPos.CENTER);
                }else{
                    OpponentPane.add(curr, numberPlace, i);
                    OpponentPane.setHalignment(curr, javafx.geometry.HPos.CENTER);
                }
                
            }
        }
    }
    
    
    private void loadNewScene(String Name) throws IOException{
        
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
            ContinueButton.setVisible(false);
        }else if (Name.contains("Search")){
            // Set visibility of object for Search page
            startButton.setVisible(false);
            exitButton.setVisible(false);
            searchImage.setVisible(true);
            searchText.setVisible(true);
            backToMenuB.setVisible(true);
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
            ContinueButton.setVisible(true);
            backToMenuB.setVisible(false);
            
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
