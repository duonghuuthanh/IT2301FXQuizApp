/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dht.services.exam;

import com.dht.pojo.Question;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author admin
 */
public abstract class ExamStrategy {
    public abstract List<Question> getQuestions() throws SQLException;
}
