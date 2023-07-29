package me13u.integration;

import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Table;
import mindustry.ctype.UnlockableContent;

public class MainMenuButton {
    public Runnable handler;
    public Drawable icon;
    public String name;

    public void setName(Object name) {
        String nm;
        if(name instanceof UnlockableContent) {
            nm = ((UnlockableContent) name).localizedName;
        } else {
            nm = String.valueOf(name);
        }
        this.name = nm;
    }

    public void setIcon(Object icon) {
        Drawable drawable = null;
        if(icon instanceof TextureRegion) {
            drawable = new TextureRegionDrawable((TextureRegion) icon);
        } else if(icon instanceof Texture) {
            drawable = new TextureRegionDrawable(new TextureRegion((Texture) icon));
        } else if(icon instanceof UnlockableContent) {
            drawable = new TextureRegionDrawable(((UnlockableContent) icon).uiIcon);
        } else if(icon instanceof Drawable) {
            drawable = (Drawable) icon;
        }
        this.icon = drawable;
    }
}