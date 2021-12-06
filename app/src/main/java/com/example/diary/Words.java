package com.example.diary;

import android.provider.BaseColumns;

public class Words {
    private int id;
    private String word;
    private String meaning;
    private String example;
    public Words(String word,String meaning,String example)
    {
        this.word=word;
        this.meaning=meaning;
        this.example=example;
    }
    public int getId() {
        return id;
    }
    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getExample() {
        return example;
    }



    public void setId(int id)
    {
        this.id=id;
    }
    public void setWord(String word)
    {
        this.word=word;
    }
    public void setMeaning(String meaning)
    {
        this.meaning=meaning;
    }
    public void setExample(String example)
    {
        this.example=example;
    }

    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME="words";
        public static final String COLUMN_NAME_WORD="word";//列：单词
        public static final String COLUMN_NAME_MEANING="meaning";//列：单词含义
        public static final String COLUMN_NAME_EXAMPLE="example";//单词示例
    }

}