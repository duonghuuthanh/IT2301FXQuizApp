/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dht.services.questions;

import com.dht.pojo.Choice;
import com.dht.pojo.Question;
import com.dht.services.BaseService;
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
public abstract class BaseQuestionServices extends BaseService<Question> {
    public abstract String getSQL(List<Object> params);

    @Override
    public PreparedStatement getStm(Connection conn) throws SQLException {
        List<Object> params = new ArrayList<>();
        PreparedStatement stm = conn.prepareCall(this.getSQL(params));
        for (int i = 0; i < params.size(); i++)
            stm.setObject(i + 1, params.get(i));
        
        return stm;
    }

    @Override
    public List<Question> getResults(ResultSet rs) throws SQLException {
        List<Question> questions = new ArrayList<>();
        while (rs.next()) {
            Question q = new Question.Builder(rs.getInt("id"), rs.getString("content")).build();

            questions.add(q);
        }
        
        return questions;
    }
    
    
  
    public List<Choice> getChoicesByQuestionId(int questionId) throws SQLException {
        Connection conn = JdbcConnector.getInstance().connect(); 

        PreparedStatement stm = conn.prepareCall("SELECT * FROM choice WHERE question_id=?");
        stm.setInt(1, questionId);
        
        ResultSet rs = stm.executeQuery();

        List<Choice> choices = new ArrayList<>();
        while (rs.next()) {
            Choice c = new Choice(rs.getInt("id"), rs.getString("content"), rs.getBoolean("is_correct"));

            choices.add(c);
        }
        
        return choices;
    }
    
     public boolean deletQuestion(int questionId) throws SQLException {
        Connection conn = JdbcConnector.getInstance().connect();
        PreparedStatement stm = conn.prepareCall("DELETE FROM question WHERE id=?");
        stm.setInt(1, questionId);
        return stm.executeUpdate() > 0;
    }

    public void addQuestion(Question q) throws SQLException, Exception {
        Connection conn = JdbcConnector.getInstance().connect();
        conn.setAutoCommit(false);
        String sql = "INSERT INTO question(content, hint, image, category_id, level_id) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement stm = conn.prepareCall(sql);
        stm.setString(1, q.getContent());
        stm.setString(2, q.getHint());
        stm.setString(3, q.getImage());
        stm.setInt(4, q.getCategory().getId());
        stm.setInt(5, q.getLevel().getId());
        if (stm.executeUpdate() > 0) {
            ResultSet rs = stm.getGeneratedKeys();
            int questionId = -1;
            if (rs.next()) {
                questionId = rs.getInt(1);
            } else {
                throw new Exception("Ch\u00e8n c\u00e2u h\u1ecfi kh\u00f4ng th\u00e0nh c\u00f4ng!");
            }
            sql = "INSERT INTO choice(content, is_correct, question_id) VALUES(?, ?, ?)";
            stm = conn.prepareCall(sql);
            for (Choice c : q.getChoies()) {
                stm.setString(1, c.getContent());
                stm.setBoolean(2, c.isCorrect());
                stm.setInt(3, questionId);
                stm.executeUpdate();
            }
            conn.commit();
        } else {
            conn.rollback();
        }
    }
}
