package com.github.franckyi.emerald.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public interface Controller<M> extends Initializable {
    void initialize(M model);

    @Override
    default void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
