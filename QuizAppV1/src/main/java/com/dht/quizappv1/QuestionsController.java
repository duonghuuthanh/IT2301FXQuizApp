/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.dht.quizappv1;

import com.dht.pojo.Category;
import com.dht.pojo.Choice;
import com.dht.pojo.Level;
import com.dht.pojo.Question;
import com.dht.services.CategoryService;
import com.dht.services.LevelServices;
import com.dht.services.QuestionServices;
import com.dht.utils.MyAlert;
import java.net.URL;
import java.sql.SQLException;
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
    @FXML private ComboBox<Category> cbCates;
    @FXML private ComboBox<Level> cbLevel;
    @FXML private TextArea txtContent;
    @FXML private VBox vboxChoices;
    @FXML private ToggleGroup groupChoice;
    
    private static final CategoryService cateService = new CategoryService();
    private static final LevelServices lvlService = new LevelServices();
    private static final QuestionServices quesService = new  QuestionServices();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.cbCates.setItems(FXCollections.observableList(cateService.getCates()));
            this.cbLevel.setItems(FXCollections.observableList(lvlService.getLevels()));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }  
    
    public void handleAddChoice(ActionEvent event) {
        HBox h = new HBox();
        h.getStyleClass().add("Main");
        
        RadioButton r = new RadioButton();
        r.setToggleGroup(groupChoice);
        TextField txt = new TextField();
        txt.setPromptText("Nội dung lựa chọn.");
        txt.getStyleClass().add("Input");
        
        h.getChildren().addAll(r, txt);
        
        this.vboxChoices.getChildren().add(h);
    }
    
    public void handleAddQuestion(ActionEvent event) {
        try {
            Question.Builder b = new Question.Builder(this.txtContent.getText(),
                    this.cbCates.getSelectionModel().getSelectedItem(),
                    this.cbLevel.getSelectionModel().getSelectedItem());
            
            for (var x: this.vboxChoices.getChildren()) {
                HBox h = (HBox) x;
                Choice c = new Choice(((TextField)h.getChildren().get(1)).getText(), 
                        ((RadioButton)h.getChildren().get(0)).isSelected());
                b.addChoice(c);
            }
            
            quesService.addQuestion(b.build());
            MyAlert.getInstance().showMsg("Thêm thành công!");
        } catch (SQLException ex) {
            MyAlert.getInstance().showMsg("Thêm thất bại, lý do: " + ex.getMessage());
        } catch (Exception ex) {
            MyAlert.getInstance().showMsg("Dữ liệu không hợp lệ!");
        }
    }
}
