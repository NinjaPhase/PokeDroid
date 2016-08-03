package com.pokedroid.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.pokedroid.PokeDroid;
import com.pokedroid.map.TileMap;

/**
 * <p>The {@code SceneMap} is used to allow a player
 * to navigate on a map.</p>
 *
 * <p>Entity logic should also take place within the
 * {@code SceneMap}.</p>
 */
public class SceneMap implements IScene {

    private TileMap map;

    @Override
    public void create(PokeDroid game) {
        map = game.getLoadedStory().getResourceManager().get(TileMap.class, "Route 1");
        System.out.println("SceneMap(" + map.toString() + ")");
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render(Batch batch) {
        for(int l = 0; l < map.getLayerCount(); l++) {
            for (int y = 0; y < map.getHeight(); y++) {
                for (int x = 0; x < map.getWidth(); x++) {
                    if(map.getTileAt(l, x, y) < 0)
                        continue;
                    batch.draw(map.getTileset().getTile(map.getTileAt(l, x, map.getHeight()-y-1)), x * 32, y * 32);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void focusGained() {

    }

    @Override
    public void focusLost() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
}
