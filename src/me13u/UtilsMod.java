package me13u;

import arc.Events;
import arc.scene.style.TextureRegionDrawable;
import me13u.integration.MainMenuButtonsLoadEvent;
import me13u.ui.*;
import mindustry.game.EventType;
import mindustry.mod.Mod;

import static me13u.UtilsData.LOGGER;

@SuppressWarnings("unused")
public class UtilsMod extends Mod {
    public UtilsMod() {
        LOGGER.info("Loaded mod constructor");

        LOGGER.info("Loading buttons event");
        Events.on(MainMenuButtonsLoadEvent.class, (ignored) -> {
            LOGGER.info("Loading buttons event call");

            LOGGER.info("Added button: Example");
            UtilsData._func_139431("Team selection", UtilsData.get("team-change"), new TeamSelection());
            UtilsData._func_139431("Tile debugger", UtilsData.get("tile-debugger"), new TileDebugger());
            UtilsData._func_139431("Place editor", UtilsData.get("place-editor"), new PlaceEditor());
        });

        LOGGER.info("Loading client load event");
        Events.on(EventType.ClientLoadEvent.class, (ignored) -> {
            LOGGER.info("Client load event call");
            WindowBuilder.minus = new TextureRegionDrawable(UtilsData.get("minus"));
            Events.fire(new MainMenuButtonsLoadEvent());
            new WindowBuilder(new Sidebar(), true).open();
        });
    }

    @Override
    public void init() {
        LOGGER.info("Inited mod");
    }
}