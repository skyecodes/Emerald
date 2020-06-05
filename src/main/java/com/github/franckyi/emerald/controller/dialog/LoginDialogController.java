package com.github.franckyi.emerald.controller.dialog;

import com.github.franckyi.emerald.data.User;
import com.github.franckyi.emerald.service.web.CallHandler;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.authenticate.AuthenticateRequest;
import com.github.franckyi.emerald.util.Emerald;
import com.github.franckyi.emerald.util.UserManager;
import com.github.franckyi.emerald.util.WebServiceManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.tinylog.Logger;

import java.util.function.Consumer;

public class LoginDialogController extends DialogController<Void> {
    @FXML
    private Label errorLabel;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXButton loginButton;

    private Consumer<User> loginAction;

    @Override
    protected void initialize() {
        super.initialize();
        errorLabel.visibleProperty().bind(errorLabel.textProperty().isNotEmpty());
        usernameField.setValidators(new RequiredFieldValidator("Please enter your username."));
        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) usernameField.resetValidation();
        });
        passwordField.setValidators(new RequiredFieldValidator("Please enter your password."));
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) passwordField.resetValidation();
        });
    }

    public void setLoginAction(Consumer<User> loginAction) {
        this.loginAction = loginAction;
    }

    public void setErrorText(String errorText) {
        errorLabel.setText(errorText);
    }

    @Override
    public void beforeShowing() {
        super.beforeShowing();
        User user = Emerald.getUser().get();
        if (user != null) {
            usernameField.setText(user.getUserName());
        }
    }

    @Override
    public void afterHiding() {
        super.afterHiding();
        errorLabel.setText(null);
        usernameField.clear();
        passwordField.clear();
        usernameField.resetValidation();
        passwordField.resetValidation();
        loginButton.setDisable(false);
    }

    @FXML
    private void loginAction() {
        if (!usernameField.validate() || !passwordField.validate()) return;
        loginButton.setDisable(true);
        String username = usernameField.getText();
        String password = passwordField.getText();
        Logger.info("Logging in with username \"{}\"...", username);
        CallHandler.builder(WebServiceManager.getMojangAuthService().authenticate(new AuthenticateRequest(username, password)))
                .onResponse(response -> {
                    Logger.info("Login succeeded");
                    User user = new User(username, response.getSelectedProfile().getName(), response.getUser().getId(),
                            response.getSelectedProfile().getId(), response.getAccessToken(), response.getClientToken());
                    Platform.runLater(() -> {
                        Emerald.getUser().set(user);
                        Emerald.getExecutorService().submit(UserManager::save);
                        this.close();
                        if (loginAction != null) {
                            loginAction.accept(user);
                            loginAction = null;
                        }
                    });
                })
                .onFailure(throwable -> {
                    Logger.info(throwable.getLocalizedMessage());
                    Platform.runLater(() -> errorLabel.setText(throwable.getLocalizedMessage()));
                }).build().runAsync();
    }
}
