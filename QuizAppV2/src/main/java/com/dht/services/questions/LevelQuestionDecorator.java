/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dht.services.questions;

import java.util.List;

/**
 *
 * @author admin
 */
public class LevelQuestionDecorator extends QuestionDecorator {
    private int levelId;

    public LevelQuestionDecorator(BaseQuestionServices decorator, int levelId) {
        super(decorator);
        this.levelId = levelId;
    }

    @Override
    public String getSQL(List<Object> params) {
        String sql = this.decorator.getSQL(params) + " AND level_id=?";
        params.add(this.levelId);
        
        return sql;
    }
}
