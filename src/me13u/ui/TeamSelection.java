package me13u.ui;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.event.ChangeListener;
import arc.scene.event.ClickListener;
import arc.scene.event.HandCursorListener;
import arc.scene.event.Touchable;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.game.Team;

public class TeamSelection extends Window {
    Table tmp;

    public void rebuild() {
        float s = 32, pad = 3;
        int rows = (int) (Math.max(minWidth(), getWidth()) / (s + pad * 2.5f));
        tmp.clearChildren();
        int r = 0;
        for(Team team : Team.all) {
            //https://github.com/MEEPofFaith/testing-utilities-java/blob/erekir/src/testing/dialogs/TeamDialog.java#L94
            Image image = tmp.image().size(s).color(team.color).pad(pad).tooltip(team.name).get();
            ClickListener listener = new ClickListener();
            image.addListener(listener);
            if(!Vars.mobile) {
                image.addListener(new HandCursorListener());
                Color lerpColor = team.color.cpy().lerp(Color.white, 0.5f);
                image.update(() -> image.color.lerp(!listener.isOver() ? team.color :
                                lerpColor, Mathf.clamp(0.4f)));
            }

            image.clicked(() -> {
                Vars.player.team(team);
            });

            image.touchable = Touchable.enabled;
            image.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Element actor) {
                    Vars.player.team(team);
                }
            });
            if(r++ % rows == rows - 1) {
                tmp.row();
            }
        }
    }

    @Override
    public void onResize(float newWidth, float newHeight, WindowBuilder builder) {
        builder.refreshFloats(this::rebuild);
    }

    @Override
    public void build() {
        pane(teams -> {
            tmp = teams;
        }).grow();
        rebuild();
        rebuild();
    }

    @Override
    public String getTitle() {
        return "Team selection";
    }
}