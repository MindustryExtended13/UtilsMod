package me13u.ui;

import me13u.integration.MainMenuButton;
import mindustry.ui.Styles;

import static me13u.UtilsData.*;
import static me13u.integration.MainMenuButtons.*;

public class Sidebar extends Window {
    final float btnS = 50, pad = 3;

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public boolean closeButton() {
        return false;
    }

    @Override
    public float minHeight() {
        return maxWidth() * 3;
    }

    @Override
    public float minWidth() {
        return maxWidth();
    }

    @Override
    public float maxWidth() {
        return btnS * 2.2f;
    }

    @Override
    public void build() {
        pane(body -> {
            for(int i = 0; i < buttonsSeq.size; i++) {
                MainMenuButton btn = buttonsSeq.get(i);
                body.button(btn.icon, Styles.clearNonei, btn.handler).tooltip(btn.name).size(btnS).pad(pad).row();
                LOGGER.info("Created button: {} (index {})", btn.name, i);
            }
        });
    }
}