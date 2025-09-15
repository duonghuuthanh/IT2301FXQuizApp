/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dht.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class Question {
    private int id;
    private String content;
    private String hint;
    private String image;
    private Category category;
    private Level level;
    private List<Choice> choies;
    
    private Question(Builder b) {
        this.id = b.id;
        this.content = b.content;
        this.hint = b.hint;
        this.image = b.image;
        this.category = b.category;
        this.level = b.level;
        this.choies = b.choies;
    }

    @Override
    public String toString() {
        return this.content; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    
    
    public static class Builder {
        private int id;
        private String content;
        private String hint;
        private String image;
        private Category category;
        private Level level;
        private List<Choice> choies = new ArrayList<>();
        
        public Builder(String content, Category c, Level l) throws Exception {
            if (content == null || content.isEmpty() || c == null || l == null)
                throw new Exception("Invalid data!");
            
            this.content = content;
            this.category = c;
            this.level = l;
        }
        
        public Builder(int id, String content) {
            this.id = id;
            this.content = content;
        }
        
        public Builder addHint(String h) {
            this.hint = h;
            return this;
        }
        
        public Builder addImage(String img) {
            this.image = img;
            return this;
        }
        
        public Builder addChoice(Choice c) {
            this.choies.add(c);
            return this;
        }
        
        public Builder addAllChoices(List<Choice> choices) {
            this.choies.addAll(choices);
            return this;
        }
        
        public Question build() {
            return new Question(this);
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the hint
     */
    public String getHint() {
        return hint;
    }

    /**
     * @param hint the hint to set
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * @return the choies
     */
    public List<Choice> getChoies() {
        return choies;
    }

    /**
     * @param choies the choies to set
     */
    public void setChoies(List<Choice> choies) {
        this.choies = choies;
    }
}
