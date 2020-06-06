package com.github.franckyi.emerald.view;

import com.github.franckyi.emerald.EmeraldApp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Label;

public class ExceptionDialog extends JFXDialog {
    public ExceptionDialog(Throwable t) {
        this(t, "Error");
    }

    public ExceptionDialog(Throwable t, String text) {
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(text));
        layout.setBody(new Label(t.getClass().getName() + ": " + t.getMessage()));
        JFXButton btn = new JFXButton("CLOSE");
        btn.getStyleClass().add("dialog-close");
        btn.setOnAction(e -> this.close());
        layout.setActions(btn);
        this.setContent(layout);
        this.setDialogContainer(EmeraldApp.getInstance().getMainController().getRoot());
    }
}
