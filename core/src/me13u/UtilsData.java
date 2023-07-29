package me13u;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.input.KeyCode;
import me13.core.logger.ILogger;
import me13.core.logger.LoggerFactory;
import me13u.integration.MainMenuButtons;
import me13u.ui.Window;
import me13u.ui.WindowBuilder;
import mindustry.content.Blocks;
import mindustry.game.Team;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.AirBlock;
import mindustry.world.blocks.environment.Floor;

import static arc.Core.*;
import static mindustry.Vars.*;

public class UtilsData {
    public static final ILogger LOGGER = LoggerFactory.build("ME13");
    public static final String MOD_NAME = "me13u";

    public static TextureRegion get(String name) {
        return Core.atlas.find(MOD_NAME + "-" + name);
    }

    public static void _func_139431(Object name, Object icon, Window window) {
        WindowBuilder builder = new WindowBuilder(window);
        MainMenuButtons.addButton(name, icon, builder::open);
    }

    public static boolean click() {
        return mobile ? input.isTouched() : input.keyDown(KeyCode.mouseRight) && input.shift();
    }

    public static float finpow(float value) {
        return value > 0.5f ? 1f - value : value;
    }

    public static void setTile(Tile tile, Block block) {
        setTile(tile, block, player.team());
    }

    public static void setTile(Tile tile, Block block, Team team) {
        setTile(tile, block, team, 0);
    }

    public static void setTile(Tile tile, Block block, Team team, int rotation) {
        if(tile == null) return;
        if(block == null) block = Blocks.air;
        if(team == null) team = Team.derelict;
        rotation = rotation % 4;
        if(block instanceof Floor && !(block instanceof AirBlock)) {
            tile.setFloor((Floor) block);
        } else {
            tile.setNet(block, team, rotation);
        }
    }

    static {
        LOGGER.info("Loaded mod logger");
        LOGGER.info("Mod name: {}", MOD_NAME);
    }
}