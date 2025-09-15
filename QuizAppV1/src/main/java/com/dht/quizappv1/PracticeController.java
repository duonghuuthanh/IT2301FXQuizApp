/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.dht.quizappv1;

import com.dht.pojo.Category;
import com.dht.pojo.Level;
import com.dht.pojo.Question;
import com.dht.services.FlyweightFactory;
import com.dht.services.questions.BaseQuestionServices;
import com.dht.services.questions.CategoryQuestionDecorator;
import com.dht.services.questions.LevelQuestionDecorator;
import com.dht.services.questions.LimitedQuestionServicesDecorator;
import com.dht.utils.Configs;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class PracticeController implements Initializable {
    @FXML private TextField txtNum;
    @FXML private Text txtContent;
    @FXML private Text txtResult;
    @FXML private VBox vboxChoices;
    @FXML private ComboBox<Category> cbCates;
    @FXML private ComboBox<Level> cbLevels;
    
    private List<Question> questions;
    private int currentQuestion;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.cbCates.setItems(FXCollections.observableList(FlyweightFactory.getData(Configs.quesService, "categories")));
            this.cbLevels.setItems(FXCollections.observableList(FlyweightFactory.getData(Configs.lvlService, "levels")));
        } catch (SQLException ex) {
            Logger.getLogger(PracticeController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }    
    
    public void handleStart() throws SQLException {
        int num = Integer.parseInt(this.txtNum.getText());
        
        try {
            BaseQuestionServices s = Configs.quesService;
            
            Category c = this.cbCates.getSelectionModel().getSelectedItem();
            if (c != null)
                s = new CategoryQuestionDecorator(s, c.getId());
            
            Level l = this.cbLevels.getSelectionModel().getSelectedItem();
            if (l != null)
                s = new LevelQuestionDecorator(s, l.getId());
            
            s = new LimitedQuestionServicesDecorator(s, num);
            this.questions = s.list();
            
            this.currentQuestion = 0;
            this.loadQuestion();
        } catch (SQLException ex) {
            //Logger.getLogger(PracticeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadQuestion() {
        Question q = this.questions.get(this.currentQuestion);
        this.txtContent.setText(q.getContent());
        
        ToggleGroup g = new ToggleGroup();
        vboxChoices.getChildren().clear();
        for (var c: q.getChoies()) {
            RadioButton r = new RadioButton(c.getContent());
            r.setToggleGroup(g);
            
            vboxChoices.getChildren().add(r);
        }
    }
    
    public void handleNext(ActionEvent event) {
        if (this.currentQuestion < this.questions.size() - 1) {
            this.currentQuestion++;
            this.loadQuestion();
        }
    }
    
    public void handleCheck(ActionEvent event) {
        Question q = this.questions.get(this.currentQuestion);
        for (int i = 0; i < q.getChoies().size(); i++)
            if (q.getChoies().get(i).isCorrect() == true) {
                RadioButton r = (RadioButton) vboxChoices.getChildren().get(i);
                if (r.isSelected()) {
                    this.txtResult.setText("CHÍNH XÁC!");
                    this.txtResult.setStyle("-fx-fill: green");
                } else {
                    this.txtResult.setText("SAI RỒI!");
                    this.txtResult.setStyle("-fx-fill: red");
                }
            }
    }
}
