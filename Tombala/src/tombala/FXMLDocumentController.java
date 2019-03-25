/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tombala;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
    private Button TombalaB;
    @FXML
    private Text opponentText;
    @FXML
    private GridPane UserPane;
    @FXML
    private GridPane OpponentPane;
    
   
    // Function for control clicks on Start Game button
    @FXML
    private void startGameAction(ActionEvent event) throws IOException{
        System.out.println("Game Waiting...");
        // TODO : Send play game request to Server
        
        // Set visibility of object for search opponent page
        loadNewScene("Search");
        
        loadNewScene("Game");
        // Each Row
        for (int i = 0; i < 9; i++) {
            // Each column
            for (int j = 0; j < 3; j++) {
                Label curr = new Label("25");
                
                UserPane.add(curr, i, j);
            }
        }
    }
    
    // Function for control clicks on Exit Game button
    @FXML
    private void exitGameAction(ActionEvent event) {
        System.exit(0);
    }
    
    // Function for go back to Main Menu
    @FXML
    private void BackToMenuAction(ActionEvent event) throws IOException {
        loadNewScene("Main");
    }
    
    // Helper Functions
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
            
        }
        
    }
    
                
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
