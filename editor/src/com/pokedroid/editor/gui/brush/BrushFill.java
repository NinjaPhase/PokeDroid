package com.pokedroid.editor.gui.brush;

import com.pokedroid.editor.map.TileMap;

import java.awt.Graphics2D;

/**
 * <p>The {@code BrushFill} is used to fill in an
 * area on the map.</p>
 */
public class BrushFill implements Brush {

    @Override
    public void drawOverlay(Graphics2D g, TileMap map, TileSelection sel, int mX, int mY) {

    }

    @Override
    public void startPaint(TileMap map, TileSelection sel, int layer, int x, int y) {
        floodFill(map, sel, x, y, layer, sel.tiles[0], map.getTile(layer, x, y));
    }

    @Override
    public void dragPaint(TileMap map, TileSelection sel, int layer, int x, int y) {

    }

    @Override
    public void stopPaint(TileMap map, TileSelection sel, int layer, int x, int y) {

    }

    private static void floodFill(TileMap map, TileSelection sel, int x, int y, int layer,
                                  int newTile, int oldTile) {
        if(!map.isValid(x, y))
            return;
        if(map.getTile(layer, x, y) != oldTile)
            return;

        map.setTile(layer, x, y, newTile);

        floodFill(map, sel, x + 1, y, layer, newTile, oldTile);
        floodFill(map, sel, x - 1, y, layer, newTile, oldTile);
        floodFill(map, sel, x, y + 1, layer, newTile, oldTile);
        floodFill(map, sel, x, y - 1, layer, newTile, oldTile);
    }

    @Override
    public String toString() {
        return "Fill";
    }

}
