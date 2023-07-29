package me13u.ui;

import arc.Core;
import arc.Events;
import arc.math.geom.Vec2;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.world.Tile;

public class TileDebugger extends Window {
    public static Tile cur;

    static {
        Events.run(EventType.Trigger.update, () -> {
            if(Vars.state.isPlaying()) {
                Vec2 mw = Core.input.mouseWorld();
                cur = Vars.world.tileWorld(mw.x, mw.y);
            } else {
                cur = null;
            }
        });
    }

    @Override
    public float minHeight() {
        return super.minHeight() - 25;
    }

    @Override
    public float maxHeight() {
        return minHeight();
    }

    @Override
    public void build() {
        final Label[] label = new Label[1];
        final Image[] image = new Image[1];
        pane(t -> {
            t.left();
            image[0] = t.image().size(48).pad(3).get();
            label[0] = t.add("text").pad(3).get();
        }).grow();
        update(() -> {
            if(cur != null) {
                UnlockableContent type;
                if(cur.build != null) {
                    type = cur.build.block;
                } else {
                    type = cur.floor();
                }

                label[0].setText(type.localizedName + "\n[gray][" + cur.x + ", " + cur.y + "]#" + type.name);
                image[0].setDrawable(type.uiIcon);
            }
        });
    }

    @Override
    public String getTitle() {
        return "Tile debugger";
    }
}