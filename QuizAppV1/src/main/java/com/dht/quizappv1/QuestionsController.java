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
import com.dht.services.FlyweightFactory;
import com.dht.services.LevelServices;
import com.dht.services.questions.BaseQuestionServices;
import com.dht.services.questions.KeywordQuestionDecorator;
import com.dht.services.questions.QuestionServices;
import com.dht.utils.Configs;
import com.dht.utils.MyAlert;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private TableView<Question> tbQuestions;
    @FXML private TextField txtSearch;
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.cbCates.setItems(FXCollections.observableList(FlyweightFactory.getData(Configs.quesService, "categories")));
            this.cbLevel.setItems(FXCollections.observableList(FlyweightFactory.getData(Configs.lvlService, "levels")));
            
            this.loadColumns();
            this.tbQuestions.setItems(FXCollections.observableList(Configs.quesService.list()));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        this.txtSearch.textProperty().addListener(e -> {
            try {
                BaseQuestionServices s = new KeywordQuestionDecorator(Configs.quesService, this.txtSearch.getText());
                this.tbQuestions.setItems(FXCollections.observableList(s.list()));
            } catch (SQLException ex) {
                Logger.getLogger(QuestionsController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        });
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
            
            Configs.quesService.addQuestion(b.build());
            MyAlert.getInstance().showMsg("Thêm câu hỏi thành công!");
        } catch (SQLException ex) {
            MyAlert.getInstance().showMsg("Thêm câu hỏi thất bại, lý do: " + ex.getMessage());
        } catch (Exception ex) {
            MyAlert.getInstance().showMsg("Dữ liệu không hợp lệ!");
        }
    }
    
    private void loadColumns() {
        TableColumn colId = new TableColumn("Id");
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colId.setPrefWidth(100);
        
        TableColumn colContent = new TableColumn("Nội dung câu hỏi");
        colContent.setCellValueFactory(new PropertyValueFactory("content"));
        colContent.setPrefWidth(300);
        
        TableColumn colAction = new TableColumn();
        colAction.setCellFactory(p -> {
            TableCell cell = new TableCell();
            
            Button btn = new Button("Xóa");
            btn.setOnAction(event -> {
                Optional<ButtonType> t = MyAlert.getInstance().showMsg("Xóa câu hỏi thì các lựa chọn cũng bị xóa theo. Bạn chắc chắn xóa không?", Alert.AlertType.CONFIRMATION);
                if (t.isPresent() && t.get().equals(ButtonType.OK)) {
                    Question q = (Question)cell.getTableRow().getItem();
                    
                    try {
                        if (Configs.quesService.deletQuestion(q.getId()) == true) {
                            MyAlert.getInstance().showMsg("Xóa câu hỏi thành công!");
                            this.tbQuestions.getItems().remove(q);
                        } else
                            MyAlert.getInstance().showMsg("Xóa câu hỏi thất bại!", Alert.AlertType.WARNING);
                    } catch (SQLException ex) {
                         MyAlert.getInstance().showMsg("Hệ thống bị lỗi, lý do: " + ex.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });
            
            cell.setGraphic(btn);
            
            return cell;
        });
        
        
        this.tbQuestions.getColumns().addAll(colId, colContent, colAction);
    }
}
