/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.dht.quizappv2;

import com.dht.pojo.Category;
import com.dht.pojo.Choice;
import com.dht.pojo.Level;
import com.dht.pojo.Question;
import com.dht.services.CategoryService;
import com.dht.services.LevelServices;
import com.dht.services.QuestionServices;
import com.dht.utils.JdbcConnection;
import com.dht.utils.MyAlert;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class QuestionsController implements Initializable {
    @FXML private TextArea txtContent;
    @FXML private ComboBox<Category> cbCates;
    @FXML private ComboBox<Level> cbLevels;
    @FXML private VBox vboxChoices;
    @FXML private ToggleGroup toggleChoice;
    
    private final static CategoryService cateSerivice = new CategoryService();
    private final static LevelServices levelService = new LevelServices();
    private final static QuestionServices questionService = new QuestionServices();
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.cbCates.setItems(FXCollections.observableList(cateSerivice.getCates()));
            this.cbLevels.setItems(FXCollections.observableList(levelService.getLevels()));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }    
    
    public void addChoice(ActionEvent event) {
        HBox h = new HBox();
        h.getStyleClass().add("Main");
        
        RadioButton r = new RadioButton();
        r.setToggleGroup(toggleChoice);
        TextField txt = new TextField();
        
        h.getChildren().addAll(r, txt);
        
        this.vboxChoices.getChildren().add(h);
    }
    
    public void addQuestion(ActionEvent event) {
        Question.Builder b = new Question.Builder(this.txtContent.getText(), 
                this.cbCates.getSelectionModel().getSelectedItem(), 
                this.cbLevels.getSelectionModel().getSelectedItem());
        
        for (var c: vboxChoices.getChildren()) {
            HBox h = (HBox) c;
            
            Choice choice = new Choice(((TextField)h.getChildren().get(1)).getText(), 
                    ((RadioButton)h.getChildren().get(0)).isSelected());
            
            b.addChoice(choice);
        }
        
        Question q = b.build();
        try {
            questionService.addQuestion(q);
            
            MyAlert.getInstance().showMsg("Them cau hoi thanh cong!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            MyAlert.getInstance().showMsg("Them cau hoi that bai!");
        }
    }
}
