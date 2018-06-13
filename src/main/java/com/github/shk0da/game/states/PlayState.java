package com.github.shk0da.game.states;

import com.github.shk0da.game.entity.Enemy;
import com.github.shk0da.game.entity.Player;
import com.github.shk0da.game.graphics.Font;
import com.github.shk0da.game.graphics.Sprite;
import com.github.shk0da.game.titles.TileManager;
import com.github.shk0da.game.util.KeyHandler;
import com.github.shk0da.game.util.MouseHandler;
import com.github.shk0da.game.util.Vector2f;

import java.awt.*;

public class PlayState extends GameState {

    private Font font;
    private Player player;
    private Enemy enemy;
    private TileManager tileManager;


    public static Vector2f map;

    public PlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
        map = new Vector2f();
        Vector2f.setWorldVar(map.x, map.y);

        tileManager = new TileManager("tilemap.xml");
        font = new Font("font.png", 10, 10);

        enemy = new Enemy(new Sprite("littlegirl.png", 48, 48), new Vector2f(
                (gamePanel.getWidth() / 2) - 32 + 150,
                (gamePanel.getHeight() / 2) - 32 + 150),
                64)
        ;
        player = new Player(new Sprite("linkformatted.png"), new Vector2f(
                (gamePanel.getWidth() / 2) - 32,
                (gamePanel.getHeight() / 2) - 32),
                64
        );
    }

    public void update() {
        Vector2f.setWorldVar(map.x, map.y);
        player.update(enemy);
        enemy.update(player);
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        player.input(mouse, key);
    }

    public void render(Graphics2D g) {
        tileManager.render(g);
        String fps = gamePanel.getOldFrameCount() + " FPS";
        Sprite.drawArray(g, font, fps, new Vector2f(gamePanel.getWidth() - fps.length() * 32, 32), 32, 24);

        String tps = gamePanel.getOldTickCount() + " TPS";
        Sprite.drawArray(g, tps, new Vector2f(gamePanel.getWidth() - tps.length() * 32, 64), 32, 24);

        player.render(g);
        enemy.render(g);
    }
}
