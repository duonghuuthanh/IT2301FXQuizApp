/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dht.utils;

import com.dht.services.CategoryService;
import com.dht.services.LevelServices;
import com.dht.services.questions.QuestionServices;

/**
 *
 * @author admin
 */
public class Configs {

    public static final CategoryService cateService = new CategoryService();
    public static final QuestionServices quesService = new QuestionServices();
    public static final LevelServices lvlService = new LevelServices();
    
}
