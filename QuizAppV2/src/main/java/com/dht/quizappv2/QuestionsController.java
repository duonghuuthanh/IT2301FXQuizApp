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
import com.dht.services.FlyweightFactory;
import com.dht.services.LevelServices;
import com.dht.services.questions.BaseQuestionServices;
import com.dht.services.questions.CategoryQuestionDecorator;
import com.dht.services.questions.KeywordQuestionDecorator;
import com.dht.services.questions.LevelQuestionDecorator;
import com.dht.services.questions.QuestionServices;
import com.dht.services.questions.UpdateQuestionServices;
import com.dht.utils.JdbcConnection;
import com.dht.utils.MyAlert;
import com.dht.utils.MyConfigs;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
    @FXML private TextArea txtContent;
    @FXML private ComboBox<Category> cbCates;
    @FXML private ComboBox<Category> cbSearchCates;
    @FXML private ComboBox<Level> cbLevels;
    @FXML private ComboBox<Level> cbSearchLevels;
    @FXML private VBox vboxChoices;
    @FXML private TableView<Question> tbQuestions;
    @FXML private TextField txtSearch;
    @FXML private ToggleGroup toggleChoice;
    
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.cbCates.setItems(FXCollections.observableList(FlyweightFactory.getData(MyConfigs.questionService, "categories")));
            this.cbSearchCates.setItems(FXCollections.observableList(FlyweightFactory.getData(MyConfigs.questionService, "categories")));
            this.cbLevels.setItems(FXCollections.observableList(FlyweightFactory.getData(MyConfigs.levelService, "levels")));
            this.cbSearchLevels.setItems(FXCollections.observableList(FlyweightFactory.getData(MyConfigs.levelService, "levels")));
            
            this.loadColumns();
            this.tbQuestions.setItems(FXCollections.observableList(MyConfigs.questionService.list()));
        } catch (SQLException ex) {
            MyAlert.getInstance().showMsg("Đã có lỗi xảy ra, lý do: " + ex.getMessage());
        }
        
        this.txtSearch.textProperty().addListener(e -> {
            try {
                BaseQuestionServices s = new KeywordQuestionDecorator(MyConfigs.questionService, this.txtSearch.getText());
                
                this.tbQuestions.getItems().clear();
                this.tbQuestions.setItems(FXCollections.observableList(s.list()));
            } catch (SQLException ex) {
                Logger.getLogger(QuestionsController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        });
        
        this.cbSearchCates.getSelectionModel().selectedItemProperty().addListener(e -> {
            BaseQuestionServices s = new CategoryQuestionDecorator(MyConfigs.questionService, this.cbSearchCates.getSelectionModel().getSelectedItem().getId());
            
            this.tbQuestions.getItems().clear();
            try {
                this.tbQuestions.setItems(FXCollections.observableList(s.list()));
            } catch (SQLException ex) {
                Logger.getLogger(QuestionsController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        });
        
        this.cbSearchLevels.getSelectionModel().selectedItemProperty().addListener(e -> {
            BaseQuestionServices s = new LevelQuestionDecorator(MyConfigs.questionService, this.cbSearchLevels.getSelectionModel().getSelectedItem().getId());
            
            this.tbQuestions.getItems().clear();
            try {
                this.tbQuestions.setItems(FXCollections.observableList(s.list()));
            } catch (SQLException ex) {
                Logger.getLogger(QuestionsController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        });
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
            MyConfigs.uquestionService.addQuestion(q);
            
            MyAlert.getInstance().showMsg("Them cau hoi thanh cong!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            MyAlert.getInstance().showMsg("Them cau hoi that bai!");
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
        colAction.setCellFactory(e -> {
            TableCell cell = new TableCell();
            
            Button btn = new Button("Xóa");
            btn.setOnAction(event -> {
                Optional<ButtonType> t = MyAlert.getInstance().showMsg("Xóa câu hỏi sẽ xóa luôn các lựa chọn của nó. Bạn chắc chắn xóa?", Alert.AlertType.CONFIRMATION);
                if (t.isPresent() && t.get().equals(ButtonType.OK)) {
                    try {
                        Question q = (Question)cell.getTableRow().getItem();
                        if (MyConfigs.uquestionService.deletQuestion(q.getId())) {
                            MyAlert.getInstance().showMsg("Xóa câu hỏi thành công!");
                            this.tbQuestions.getItems().remove(q);
                        } else
                             MyAlert.getInstance().showMsg("Xóa câu hỏi thất bại!", Alert.AlertType.ERROR);
                    } catch (SQLException ex) {
                        MyAlert.getInstance().showMsg("Hệ thống có lỗi, lý do: " + ex.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });
            
            cell.setGraphic(btn);
            
            return cell;
        });
        
        this.tbQuestions.getColumns().addAll(colId, colContent, colAction);
    }
    
}
