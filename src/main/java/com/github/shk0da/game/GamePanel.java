package com.github.shk0da.game;

import com.github.shk0da.game.states.GameStateManager;
import com.github.shk0da.game.util.KeyHandler;
import com.github.shk0da.game.util.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    private static final int MUBR = 5; // Must Update before render
    private static final double TARGET_FPS = 60;
    private static final double GAME_HERTZ = 1000.0;

    private static final int BILLION = 1_000_000_000;
    private static final double TBU = BILLION / GAME_HERTZ; // Time Before Update
    private static final double TTBR = BILLION / TARGET_FPS; // Total time before render

    private int width;
    private int height;
    private int oldFrameCount;
    private int oldTickCount;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D graphics2D;

    private MouseHandler mouse;
    private KeyHandler key;

    private GameStateManager gameStateManager;

    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocus();

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics2D = (Graphics2D) image.getGraphics();

        mouse = new MouseHandler(this);
        key = new KeyHandler(this);

        gameStateManager = new GameStateManager(this);
        running = true;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public int getOldFrameCount() {
        return oldFrameCount;
    }

    public int getOldTickCount() {
        return oldTickCount;
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this, "GameThread");
            thread.start();
        }
    }

    public void run() {
        double lastUpdateTime = System.nanoTime();
        double lastRenderTime;

        int frameCount = 0;
        int lastSecondTime = (int) (lastUpdateTime / BILLION);
        oldFrameCount = 0;

        int tickCount = 0;
        oldTickCount = 0;

        while (running) {
            double now = System.nanoTime();
            int updateCount = 0;
            while (((now - lastUpdateTime) > TBU) && (updateCount < MUBR)) {
                update();
                input(mouse, key);
                lastUpdateTime += TBU;
                updateCount++;
                tickCount++;
            }

            if (now - lastUpdateTime > TBU) {
                lastUpdateTime = now - TBU;
            }

            input(mouse, key);
            render();
            draw();
            lastRenderTime = now;
            frameCount++;

            int thisSecond = (int) (lastUpdateTime / BILLION);
            if (thisSecond > lastSecondTime) {
                if (frameCount != oldFrameCount) {
                    oldFrameCount = frameCount;
                }

                if (tickCount != oldTickCount) {
                    oldTickCount = tickCount;
                }
                tickCount = 0;
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while (now - lastRenderTime < TTBR && now - lastUpdateTime < TBU) {
                Thread.yield();
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    System.out.println("ERROR: yielding thread");
                }
                now = System.nanoTime();
            }
        }
    }

    public void update() {
        gameStateManager.update();
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        gameStateManager.input(mouse, key);
    }

    public void render() {
        if (graphics2D != null) {
            graphics2D.setColor(new Color(62, 65, 78));
            graphics2D.fillRect(0, 0, width, height);
            gameStateManager.render(graphics2D);
        }
    }

    public void draw() {
        this.getGraphics().drawImage(image, 0, 0, width, height, null);
        this.getGraphics().dispose();
    }
}
