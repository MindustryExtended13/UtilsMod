package me13u.ui;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.scene.event.ClickListener;
import arc.scene.event.HandCursorListener;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.world.Block;
import mindustry.world.Tile;

import static arc.Core.*;
import static mindustry.Vars.*;
import static me13u.UtilsData.*;

public class PlaceEditor extends Window {
    public static Block config;
    public static int brushSize = 0;
    Table tmp;

    public void rebuild() {
        float s = 32, pad = 3;
        int rows = (int) (Math.max(minWidth(), getWidth()) / (s + pad * 2.5f));
        tmp.clearChildren();
        int r = 0;
        for(Block block : content.blocks()) {
            //https://github.com/MEEPofFaith/testing-utilities-java/blob/erekir/src/testing/dialogs/BlockDialog.java#L186
            Image image = new Image(block.uiIcon).setScaling(Scaling.fit);
            tmp.add(image).size(s).pad(pad);

            ClickListener listener = new ClickListener();
            image.addListener(listener);
            if(!mobile) {
                image.addListener(new HandCursorListener());
                image.update(() -> image.color.lerp(listener.isOver() || config == block
                        ? Color.white : Color.lightGray, Mathf.clamp(0.4f)));
            }else{
                image.update(() -> image.color.lerp(block == config ? Color.white :
                        Color.lightGray, Mathf.clamp(0.4f)));
            }

            image.clicked(() -> {
                if(input.keyDown(KeyCode.shiftLeft) && Fonts.getUnicode(block.name) != 0){
                    //noinspection ConcatenationWithEmptyString
                    app.setClipboardText((char) Fonts.getUnicode(block.name) + "");
                    ui.showInfoFade("@copied");
                }else{
                    config = block;
                }
            });
            if(r++ % rows == rows - 1) {
                tmp.row();
            }
        }
    }

    static {
        Events.run(EventType.Trigger.update, () -> {
            Tile c = TileDebugger.cur;
            if(config != null && click() && c != null) {
                int tx = c.x;
                int ty = c.y;
                int s = brushSize/2;
                if(s == 0) {
                    setTile(c, config);
                } else {
                    for(int x = tx - s; x < tx + s; x++) {
                        for(int y = ty - s; y < ty + s; y++) {
                            setTile(world.tile(x, y), config);
                        }
                    }
                }
            }
        });
        if(!mobile) {
            Events.run(EventType.Trigger.draw, () -> {
                Tile c = TileDebugger.cur;
                if(config != null && input.shift() && c != null) {
                    Draw.color(Pal.accent);
                    Draw.alpha(0.5f);
                    int tx = c.x;
                    int ty = c.y;
                    int s = brushSize/2;
                    if(s == 0) {
                        Fill.rect(c.worldx(), c.worldy(), 8, 8);
                    } else {
                        for(int x = tx - s; x < tx + s; x++) {
                            for(int y = ty - s; y < ty + s; y++) {
                                Tile out = world.tile(x, y);
                                if(out != null) {
                                    Fill.rect(out.worldx(), out.worldy(), 8, 8);
                                }
                            }
                        }
                    }
                    Draw.reset();
                }
            });
        }
    }

    @Override
    public float minWidth() {
        return super.minWidth() + 20;
    }

    @Override
    public void onResize(float newWidth, float newHeight, WindowBuilder builder) {
        builder.refreshFloats(this::rebuild);
    }

    @Override
    public float minHeight() {
        return super.minHeight() * 2;
    }

    @Override
    public void mainCloseEvent() {
        config = null;
    }

    @Override
    public void build() {
        pane(teams -> {
            teams.table(t -> {
                t.add("").update(l -> {
                    l.setText("Current: " + (config == null ? "none" : config.localizedName));
                }).row();
                if(!mobile) {
                    t.add("Right mouse + shift to place").row();
                }
                t.table(buttons -> {
                    buttons.defaults().size(150, 50).pad(3);
                    buttons.button("Remove current", () -> config = null);
                    buttons.button("Set to air", () -> config = Blocks.air);
                }).row();
                t.table(slider -> {
                    slider.add("Brush size:").row();
                    //noinspection ConcatenationWithEmptyString
                    slider.field(brushSize + "", (str) -> {
                        brushSize = Integer.parseInt(str);
                    }).valid((str) -> {
                        try {
                            int i = Integer.parseInt(str);
                            return i >= 0;
                        } catch(Throwable ignored) {
                            return false;
                        }
                    });
                }).row();
            }).growX().row();

            teams.table(list -> {
                tmp = list;
            }).growX();
        }).grow();
        rebuild();
        rebuild();
    }

    @Override
    public String getTitle() {
        return "Place editor";
    }
}