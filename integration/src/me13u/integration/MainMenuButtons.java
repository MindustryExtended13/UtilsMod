package me13u.integration;

import arc.struct.Seq;
import mindustry.ctype.UnlockableContent;

public class MainMenuButtons {
    public static Seq<MainMenuButton> buttonsSeq = new Seq<>();

    public static void addButton(MainMenuButton button) {
        if(button != null) buttonsSeq.add(button);
    }

    public static void addButton(Object name, Object icon, Runnable handle) {
        MainMenuButton btn = new MainMenuButton();
        btn.handler = handle;
        btn.setName(name);
        btn.setIcon(icon);
        addButton(btn);
    }

    public static void addButton(UnlockableContent content, Runnable handle) {
        addButton(content, content, handle);
    }
}