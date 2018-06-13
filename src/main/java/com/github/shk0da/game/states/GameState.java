package com.github.shk0da.game.states;

import com.github.shk0da.game.GamePanel;
import com.github.shk0da.game.util.KeyHandler;
import com.github.shk0da.game.util.MouseHandler;

import java.awt.*;

public abstract class GameState {

    protected final GameStateManager gameStateManager;
    protected final GamePanel gamePanel;

    public GameState(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        this.gamePanel = gameStateManager.getGamePanel();
    }

    public abstract void update();

    public abstract void input(MouseHandler mouseHandler, KeyHandler keyHandler);

    public abstract void render(Graphics2D graphics2D);
}
