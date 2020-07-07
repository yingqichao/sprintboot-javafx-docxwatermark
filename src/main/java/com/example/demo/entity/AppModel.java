package com.example.demo.entity;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AppModel
{
    private StringProperty text = new SimpleStringProperty();
    private BooleanProperty isWord = new SimpleBooleanProperty();

    public AppModel()
    {
        this.text = new SimpleStringProperty();
        this.isWord = new SimpleBooleanProperty();
    }

    public StringProperty textProperty() {
        return text;
    }

    public final String getText() {
        return textProperty().get();
    }

    public final void setText(String text) {
        textProperty().set(text);
    }

    public boolean isIsWord() {
        return isWord.get();
    }

    public BooleanProperty isWordProperty() {
        return isWord;
    }

    public void setIsWord(boolean isWord) {
        this.isWord.set(isWord);
    }
}