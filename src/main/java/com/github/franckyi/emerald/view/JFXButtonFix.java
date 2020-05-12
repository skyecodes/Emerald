package com.github.franckyi.emerald.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.skins.JFXButtonSkin;
import javafx.scene.control.Skin;

public class JFXButtonFix extends JFXButton {

    @Override
    protected Skin<?> createDefaultSkin() {
        return new JFXButtonSkinFix(this);
    }

    private static class JFXButtonSkinFix extends JFXButtonSkin {
        public JFXButtonSkinFix(JFXButton button) {
            super(button);
        }

        @Override
        protected void updateChildren() {
            try {
                super.updateChildren();
            } catch (Throwable t) {
            } // avoid error in logs
        }
    }
}
