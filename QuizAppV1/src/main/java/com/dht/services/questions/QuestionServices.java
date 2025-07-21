/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dht.services.questions;



import com.dht.pojo.Choice;
import com.dht.pojo.Question;
import com.dht.utils.JdbcConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class QuestionServices {
    public List<Question> getQuestions() throws SQLException {
        // B2: thiết lập kết nối
        Connection conn = JdbcConnector.getInstance().connect();

        // B3: thực thi truy vân
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM question");

        List<Question> questions = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String content = rs.getString("content");

            Question q = new Question.Builder(id, content).build();
            questions.add(q);
        }
        
        return questions;
    }
    
    public List<Question> getQuestions(String kw) throws SQLException {
        // B2: thiết lập kết nối
        Connection conn = JdbcConnector.getInstance().connect();

        // B3: thực thi truy vân
        PreparedStatement stm = conn.prepareCall("SELECT * FROM question WHERE content like concat('%', ?, '%')");// SQL Injection
        stm.setString(1, kw);
        
        ResultSet rs = stm.executeQuery();

        List<Question> questions = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String content = rs.getString("content");

            Question q = new Question.Builder(id, content).build();
            questions.add(q);
        }
        
        return questions;
    }
    
    public List<Question> getQuestions(int num) throws SQLException {
        // B2: thiết lập kết nối
        Connection conn = JdbcConnector.getInstance().connect();

        // B3: thực thi truy vân
        PreparedStatement stm = conn.prepareCall("SELECT * FROM question ORDER BY rand() LIMIT ?");// SQL Injection
        stm.setInt(1, num);
        
        ResultSet rs = stm.executeQuery();

        List<Question> questions = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String content = rs.getString("content");

            Question q = new Question.Builder(id, content).addAllChoices(this.getChoicesByQuestionId(id)).build();
            questions.add(q);
        }
        
        return questions;
    }
    
    public List<Choice> getChoicesByQuestionId(int quesId) throws SQLException {
        // B2: thiết lập kết nối
        Connection conn = JdbcConnector.getInstance().connect();

        // B3: thực thi truy vân
        PreparedStatement stm = conn.prepareCall("SELECT * FROM choice WHERE question_id=?");// SQL Injection
        stm.setInt(1, quesId);
        
        ResultSet rs = stm.executeQuery();

        List<Choice> choices = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String content = rs.getString("content");
            boolean correct = rs.getBoolean("is_correct");

            Choice c = new Choice(id, content, correct);
            choices.add(c);
        }
        
        return choices;
    }
    
    public void addQuestion(Question q) throws SQLException {
        Connection cnn = JdbcConnector.getInstance().connect();
        cnn.setAutoCommit(false);
        String sql = "INSERT INTO question(content, hint, image, category_id, level_id) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement stm = cnn.prepareCall(sql);
        stm.setString(1, q.getContent());
        stm.setString(2, q.getHint());
        stm.setString(3, q.getImage());
        stm.setInt(4, q.getCategory().getId());
        stm.setInt(5, q.getLevel().getId());
        
        if (stm.executeUpdate() > 0) {
            int quesId = -1;
            ResultSet r = stm.getGeneratedKeys();
            if (r.next())
                quesId = r.getInt(1);
            
            sql = "INSERT INTO choice(content, is_correct, question_id) VALUES(?, ?, ?)";
            
            stm = cnn.prepareCall(sql);
            for (var c: q.getChoies()) {
                stm.setString(1, c.getContent());
                stm.setBoolean(2, c.isCorrect());
                stm.setInt(3, quesId);
                
                stm.executeUpdate();
            }
            
            cnn.commit();
        } else
            cnn.rollback();
    }
    
    public boolean deleteQuestion(int quesId) throws SQLException {
        // B2: thiết lập kết nối
        Connection conn = JdbcConnector.getInstance().connect();

        // B3: thực thi truy vân
        PreparedStatement stm = conn.prepareCall("DELETE FROM question WHERE id=?");// SQL Injection
        stm.setInt(1, quesId);
        
        return stm.executeUpdate() > 0;
    }
}
