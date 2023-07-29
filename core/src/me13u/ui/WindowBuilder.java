package me13u.ui;
import arc.Core;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.event.Touchable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.util.Tmp;
import me13u.UtilsData;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

public class WindowBuilder {
    public static TextureRegionDrawable minus;
    public MainPane mainPane = new MainPane();
    public Window window;
    public boolean show = false;
    public boolean resizing = false;
    public boolean moving = false;
    public float lw = -1, lh = -1, lx = -1, ly = -1;
    public int s = 15;

    public boolean psfsEnabled() {
        return !moving && !resizing;
    }

    public WindowBuilder(Window window) {
        this(window, false);
    }

    public WindowBuilder(Window window, boolean slider) {
        this.window = window;
        Vars.ui.hudGroup.fill(t -> {
            if(slider) t.left();
            t.add(mainPane).size(window.minWidth(), window.minHeight()).visible(() -> {
                return show;
            }).update(t2 -> {
                if(s <= 0) {
                    if(lw == -1) lw = t2.getWidth();
                    if(lh == -1) lh = t2.getHeight();
                    if(lx == -1) lx = t2.x;
                    if(ly == -1) ly = t2.y;

                    if(psfsEnabled()) { //Position-Size Fix System (PSFS)
                        if(lx != t2.x || ly != t2.y || lw != t2.getWidth() || lh != t2.getHeight()) {
                            UtilsData.LOGGER.warn("[PSFS] FIXING BUG");
                            Core.app.post(() -> {
                                t2.setPosition(lx, ly);
                                t2.setSize(lw, lh);
                                refreshFloats();
                            });
                        }
                    }
                } else s--;
            });
        });
        window.build();
        window.onOpen();
        refreshFloats();
        build();
    }

    public void resize(float width, float height) {
        window.onResize(width, height, this);
        lw = mainPane.getWidth();
        lh = mainPane.getHeight();
    }

    public void close() {
        show = false;
        window.mainCloseEvent();
    }

    public void open() {
        show = true;
    }

    public void refreshFloats() {
        refreshFloats(null);
    }

    public void refreshFloats(Runnable runnable) {
        float lw = mainPane.getWidth();
        float lh = mainPane.getHeight();
        float lx = mainPane.x;
        float ly = mainPane.y;
        if(runnable != null) {
            runnable.run();
        }
        Core.app.post(() -> {
            mainPane.setPosition(lx, ly);
            mainPane.setSize(lw, lh);
        });
    }

    public void build() {
        Table cont = new Table();
        cont.table(content -> {
            content.setBackground(Tex.pane2);
            content.add(window).grow();
        }).grow().row();
        cont.table(footer -> {
            footer.setBackground(Tex.pane2);
            footer.top().right();
            footer.button(Icon.resize, Styles.clearNonei, () -> {}).size(24)
                    .get().addListener(new ScaleInputListener());
        }).growX().row();

        mainPane.table(t -> {
            t.setBackground(Tex.buttonEdge3);
            t.left();
            ImageButton.ImageButtonStyle style = Styles.clearNonei;
            if(window.closeButton()) {
                t.button(Icon.cancel, style, this::close).padRight(6).size(24);
            }
            boolean[] b = {false};
            t.button(Icon.cancel, style, () -> {
                refreshFloats(() -> {
                    if(window.opened) {
                        window.onClose();
                    } else {
                        window.onOpen();
                    }
                });
            }).update(btn -> {
                if(window.opened != b[0]) {
                    btn.getStyle().imageUp = window.opened ? minus : Icon.add;
                    refreshFloats();
                }
                b[0] = window.opened;
            }).size(24);

            t.add(window.getTitle()).padLeft(6);
            t.touchable = Touchable.enabled;
            t.addListener(new DragHandleListener());
        }).growX().row();
        mainPane.add(cont).grow().visible(() -> window.opened);
    }

    //InputListeners from Informatis
    //https://github.com/Sharlottes/Informatis/blob/master/src/informatis/ui/windows/Window.java

    private class ScaleInputListener extends TouchPosInputListener {
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button) {
            super.touchUp(event, x, y, pointer, button);
            resize(mainPane.getWidth(), mainPane.getHeight());
            resizing = false;
        }

        @Override
        public void touchDragged(InputEvent event, float dx, float dy, int pointer) {
            Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(dx, dy));
            float w = v.x - lastX;
            float h = v.y - lastY;

            // will soft-lock if initial size is smaller than minimum
            // so don't do that!
            if(mainPane.getWidth() + w < window.minWidth() || mainPane.getWidth() + w > window.maxWidth()) w = 0;
            if(mainPane.getHeight() - h < window.minHeight() || mainPane.getHeight() - h > window.maxHeight()) h = 0;
            mainPane.sizeBy(w, -h);
            mainPane.moveBy(0, h);
            lastX = v.x;
            lastY = v.y;

            ly += h;
            resizing = true;
        }
    }

    private class DragHandleListener extends TouchPosInputListener {
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button) {
            super.touchUp(event, x, y, pointer, button);
            moving = false;
        }

        @Override
        public void touchDragged(InputEvent event, float dx, float dy, int pointer) {
            Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(dx, dy));
            mainPane.setPosition(mainPane.x + (v.x - lastX), mainPane.y + (v.y - lastY));
            lastX = v.x;
            lastY = v.y;
            lx = mainPane.x;
            ly = mainPane.y;
            moving = true;
        }
    }

    private static class TouchPosInputListener extends InputListener {
        protected float lastX, lastY;

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
            Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(x, y));
            lastX = v.x;
            lastY = v.y;
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float dx, float dy, int pointer) {
            Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(dx, dy));
            lastX = v.x;
            lastY = v.y;
        }
    }
}