/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.dht.quizappv2;

import com.dht.pojo.Question;
import com.dht.services.QuestionServices;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    
    private static final QuestionServices questionSerices = new QuestionServices();
    private List<Question> questions;
    private int currentQuestion = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void handleStart(ActionEvent event) {
        int num = Integer.parseInt(this.txtNum.getText());
        
        try {
            this.questions = questionSerices.getQuestions(num);
            
            this.currentQuestion = 0;
            this.loadQuestion();
        } catch (SQLException ex) {
            Logger.getLogger(PracticeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void handleCheck(ActionEvent event) {
        Question q = this.questions.get(this.currentQuestion);
        for (int i = 0; i < q.getChoices().size(); i++)
            if (q.getChoices().get(i).isCorrect() == true) {
                RadioButton r = (RadioButton)vboxChoices.getChildren().get(i);
                if (r.isSelected())
                    this.txtResult.setText("CHÍNH XÁC!");
                else
                    this.txtResult.setText("SAI RỒI!");
                
                break;
            }
    }
    
    public void handleNext(ActionEvent event) {
        if (this.currentQuestion < this.questions.size() - 1) {
            this.currentQuestion++;
            this.loadQuestion();
        }
    }
    
    private void loadQuestion() {
        Question q = this.questions.get(this.currentQuestion);
        
        this.txtContent.setText(q.getContent());
        
        vboxChoices.getChildren().clear();
        ToggleGroup t = new ToggleGroup();
        for (var c: q.getChoices()) {
            RadioButton r = new RadioButton(c.getContent());
            r.setToggleGroup(t);
            
            vboxChoices.getChildren().add(r);
        }
    }
}
