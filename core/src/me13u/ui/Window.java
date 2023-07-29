package me13u.ui;

import arc.Core;
import arc.scene.ui.layout.Table;

public class Window extends Table {
    public boolean opened = false;

    public boolean closeButton() {
        return true;
    }

    public void mainCloseEvent() {
    }

    public float minHeight() {
        return 200;
    }

    public float maxHeight() {
        return Core.graphics.getHeight();
    }

    public float minWidth() {
        return 350;
    }

    public float maxWidth() {
        return Core.graphics.getWidth();
    }

    public void onResize(float newWidth, float newHeight, WindowBuilder builder) {
    }

    public void onOpen() {
        opened = true;
    }

    public void onClose() {
        opened = false;
    }

    public void build() {
    }

    public float titleWidth() {
        return 300;
    }

    public String getTitle() {
        return "null";
    }
}