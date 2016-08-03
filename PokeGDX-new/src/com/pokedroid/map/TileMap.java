package com.pokedroid.map;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.pokedroid.story.Story;

/**
 * <p>The {@code TileMap} will be used as a resource for a player
 * and other entities to walk around on and navigate through a game
 * world.</p>
 *
 * @author NinjaPhase
 * @version 02 August 2016
 */
public class TileMap implements Disposable {

    private int width, height;
    private String name;
    private int[][] tiles;
    private TileSet tileset;

    /**
     * <p>Constructs a new {@code TileMap} from a given
     * JSON file.</p>
     *
     * @param story The story this map belongs to.
     * @param jsonValue The json file.
     */
    public TileMap(Story story, JsonValue jsonValue) {
        this.name = jsonValue.getString("name", "unknown-reference");
        this.width = jsonValue.getInt("width", 0);
        this.height = jsonValue.getInt("height", 0);
        int layers = jsonValue.getInt("layers", 0);
        this.tiles = new int[layers][this.width*this.height];
        JsonValue tileMap = jsonValue.get("tiles");
        for(int l = 0; l < layers; l++) {
            for (int i = 0; i < tiles[l].length; i++) {
                this.tiles[l][i] = tileMap.get(l).getInt(i);
            }
        }
        this.tileset = story.getResourceManager().get(TileSet.class, jsonValue.getString("tileset", "null"));
    }

    @Override
    public void dispose() {

    }

    /**
     * @param l The layer.
     * @param x The x position.
     * @param y The y position.
     * @return The tile at x and y.
     */
    public int getTileAt(int l, int x, int y) {
        return this.tiles[l][x + (y * width)];
    }

    /**
     * @return The name of this {@code TileMap}.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The width of the {@code TileMap}.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return The height of the {@code Tilemap}.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return The layer count of the {@code TileMap}.
     */
    public int getLayerCount() {
        return this.tiles.length;
    }

    /**
     * @return The tileset this map is using.
     */
    public TileSet getTileset() {
        return tileset;
    }

    @Override
    public String toString() {
        return "<TileMap " + this.getName() + ">";
    }

}
