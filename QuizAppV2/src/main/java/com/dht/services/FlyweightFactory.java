/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dht.services;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class FlyweightFactory {

    private static final Map<String, List> cachedData = new HashMap<>();

    public static <E> List<E> getData(BaseService s, String key) throws SQLException {
        if (cachedData.containsKey(key)) {
            return cachedData.get(key);
        } else {
            System.out.println(Math.random());
            List<E> ls = s.list();
            cachedData.put(key, ls);

            return ls;
        }
    }
}
