/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dht.utils;

import com.dht.services.CategoryService;
import com.dht.services.LevelServices;
import com.dht.services.questions.QuestionServices;
import com.dht.services.questions.UpdateQuestionServices;

/**
 *
 * @author admin
 */
public class MyConfigs {

    public static final UpdateQuestionServices uquestionService = new UpdateQuestionServices();
    public static final QuestionServices questionService = new QuestionServices();
    public static final LevelServices levelService = new LevelServices();
    public static final CategoryService cateSerivice = new CategoryService();
    
    public static final int NUM_QUESTIONS = 10;
    public static final double[] RATES = {0.2, 0.4, 0.4};
    
}
