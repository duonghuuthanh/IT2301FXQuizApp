/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.dht.quizappv1;

import com.dht.pojo.Choice;
import com.dht.pojo.Question;
import com.dht.services.exam.ExamStrategy;
import com.dht.services.exam.ExamTypes;
import com.dht.services.exam.FixedStrategy;
import com.dht.services.exam.SpecificStrategy;
import com.dht.utils.MyAlert;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
public class ExamController implements Initializable {
    @FXML private ComboBox<ExamTypes> cbTypes;
    @FXML private ListView<Question> lvQuestions;
    @FXML private TextField txtNum;
    private Map<Integer, Choice> answers = new HashMap<>();
    private List<Question> questions;
    private ExamStrategy s;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.cbTypes.setItems(FXCollections.observableArrayList(ExamTypes.values()));
        
        this.txtNum.setVisible(false);
        this.cbTypes.getSelectionModel().selectedItemProperty().addListener(evt -> {
            if (this.cbTypes.getSelectionModel().getSelectedItem() == ExamTypes.SPECIFIC)
                this.txtNum.setVisible(true);
            else
                this.txtNum.setVisible(false);
        });
        
        this.lvQuestions.setCellFactory(param -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question q, boolean e) {
                super.updateItem(q, e); 
                
                if (q == null || e == true)
                    setGraphic(null);
                else {
                    VBox v = new VBox(5);
                    v.setStyle(("-fx-border-width: 1; -fx-border-color: gray; -fx-padding: 8"));
                    
                    Text t = new Text(q.getContent());
                    v.getChildren().add(t);
                    
                    ToggleGroup g = new ToggleGroup();
                    for (var c: q.getChoies()) {
                        RadioButton r = new RadioButton(c.getContent());
                        r.setToggleGroup(g);
                        
                        // bổ sung update UI
                        if (answers.get(q.getId()) == c)
                            r.setSelected(true);
                        
                        r.setOnAction(evt -> {
                            if (r.isSelected())
                                answers.put(q.getId(), c);
                        });
                        
                        v.getChildren().add(r);
                    }
                    
                    setGraphic(v);
                }
            }
            
        });
    }    
    
    public void startHandle(ActionEvent e) {
        s = new FixedStrategy();
        if (this.cbTypes.getSelectionModel().getSelectedItem() == ExamTypes.SPECIFIC)
            s = new SpecificStrategy(Integer.parseInt(this.txtNum.getText()));
        try {
            this.questions = s.getQuestions();
            this.lvQuestions.setItems(FXCollections.observableList(this.questions));
        } catch (SQLException ex) {
            Logger.getLogger(ExamController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void markHandle(ActionEvent e) {
        int count = 0;
        for (var c: answers.values())
            if (c.isCorrect())
                count++;
        
        MyAlert.getInstance().showMsg(String.format("Bạn làm đúng %d/%d!", count, questions.size()));
    }
    
    public void saveHandle(ActionEvent e) {
        Optional<ButtonType> t = MyAlert.getInstance().showMsg("Bạn chắc chắn lưu đề thi?", Alert.AlertType.CONFIRMATION);
        if (t.isPresent() && t.get() == ButtonType.OK) {
            try {
                s.saveExam(this.questions);
                MyAlert.getInstance().showMsg("Đề thi lưu thành công!");
            } catch (SQLException ex) {
                MyAlert.getInstance().showMsg("Đề thi lưu thất bại, lý do: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
