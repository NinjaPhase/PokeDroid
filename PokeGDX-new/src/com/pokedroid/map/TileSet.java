package com.pokedroid.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.pokedroid.story.Story;

/**
 * <p>The {@code TileSet} will contain a list of tiles stored in some form
 * of array, list or dictionary.</p>
 *
 * <p>Each tile has a different properties/data attached to it so that each
 * individual tile can have a unique function when stepped on by an entity
 * or when rendered.</p>
 *
 * @author NinjaPhase
 * @version 02 August 2016
 */
public class TileSet implements Disposable {

    private String name;
    private int tileWidth, tileHeight;
    private int[] tileData;
    private TextureRegion[] tiles;

    /**
     * <p>Constructs a new TileSet.</p>
     *
     * @param story The story this tileset belongs to.
     * @param jsonValue The json value to build the tileset.
     */
    public TileSet(Story story, JsonValue jsonValue) {
        this.name = jsonValue.getString("name", "unknown-reference");
        this.tileWidth = jsonValue.getInt("tile_width", 32);
        this.tileHeight = jsonValue.getInt("tile_height", 32);
        Texture t = story.getResourceManager().get(Texture.class, jsonValue.getString("tileset_path", "null"));
        int rows = t.getHeight()/tileHeight;
        int cols = t.getWidth() / tileWidth;
        tiles = new TextureRegion[rows*cols];
        for(int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                tiles[x + (y * cols)] = new TextureRegion(t, x*tileWidth, y*tileHeight, tileWidth, tileHeight);
            }
        }
        tileData = new int[rows*cols];
    }

    @Override
    public void dispose() {

    }

    /**
     * @param i The tile index.
     * @return The texture of the tile.
     */
    public TextureRegion getTile(int i) {
        return this.tiles[i];
    }

    /**
     * @return The name of the tileset.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The width of an individual tile.
     */
    public int getTileWidth() {
        return this.tileWidth;
    }

    /**
     * @return The height of an individual tile.
     */
    public int getTileHeight() {
        return this.tileHeight;
    }

    /**
     * @return The number of tiles in the tileset.
     */
    public int getTileCount() {
        return this.tiles.length;
    }

    @Override
    public String toString() {
        return "<TileSet " + getName() + ">";
    }

}
