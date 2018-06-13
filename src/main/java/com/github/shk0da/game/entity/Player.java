package com.github.shk0da.game.entity;

import com.github.shk0da.game.graphics.Font;
import com.github.shk0da.game.graphics.Sprite;
import com.github.shk0da.game.states.PlayState;
import com.github.shk0da.game.util.KeyHandler;
import com.github.shk0da.game.util.MouseHandler;
import com.github.shk0da.game.util.Vector2f;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Player extends Entity {

    private static final Font font = new Font("font.png", 10, 10);

    private final Vector2f origin;

    public Player(Sprite sprite, Vector2f origin, int size) {
        super(sprite, origin, size);
        this.origin = origin;
        acc = 2f;
        maxSpeed = 0.5f;
        bounds.setWidth(42);
        bounds.setHeight(20);
        bounds.setXOffset(12);
        bounds.setYOffset(40);
    }

    private void move() {
        if (up) {
            dy -= acc;
            if (dy < -maxSpeed) {
                dy = -maxSpeed;
            }
        } else {
            if (dy < 0) {
                dy += deacc;
                if (dy > 0) {
                    dy = 0;
                }
            }
        }
        if (down) {
            dy += acc;
            if (dy > maxSpeed) {
                dy = maxSpeed;
            }
        } else {
            if (dy > 0) {
                dy -= deacc;
                if (dy < 0) {
                    dy = 0;
                }
            }
        }
        if (left) {
            dx -= acc;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else {
            if (dx < 0) {
                dx += deacc;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }
        if (right) {
            dx += acc;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        } else {
            if (dx > 0) {
                dx -= deacc;
                if (dx < 0) {
                    dx = 0;
                }
            }
        }
    }

    private void resetPosition() {
        System.out.println("Reseting Player... ");
        pos.x = origin.x;
        PlayState.map.x = 0;

        pos.y = origin.y;
        PlayState.map.y = 0;

        setAnimation(RIGHT, sprite.getSpriteArray(RIGHT), 10);

    }

    public void update(Enemy enemy) {
        super.update();

        if (attack && hitBounds.collides(enemy.getBounds())) {
            System.out.println("Hit!");
        }

        if (!fallen) {
            move();
            if (!tc.collisionTile(dx, 0)) {
                PlayState.map.x += dx;
                pos.x += dx;
            }
            if (!tc.collisionTile(0, dy)) {
                PlayState.map.y += dy;
                pos.y += dy;
            }
        } else {
            if (ani.hasPlayedOnce()) {
                resetPosition();
                dx = 0;
                dy = 0;
                fallen = false;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (attack) {
            Sprite.drawArray(g, font, "Attack!",
                    new Vector2f(
                            hitBounds.getPos().getWorldVar().x + hitBounds.getXOffset() + 10,
                            hitBounds.getPos().getWorldVar().y + hitBounds.getYOffset() - 40
                    ), 32, 20);
        }

        g.drawImage(ani.getImage(), (int) (pos.getWorldVar().x), (int) (pos.getWorldVar().y), size, size, null);
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        if (!fallen) {
            if (key.up.down) {
                up = true;
            } else {
                up = false;
            }
            if (key.down.down) {
                down = true;
            } else {
                down = false;
            }
            if (key.left.down) {
                left = true;
            } else {
                left = false;
            }
            if (key.right.down) {
                right = true;
            } else {
                right = false;
            }

            if (key.attack.down || mouse.getButton() == MouseEvent.BUTTON1) {
                attack = true;
            } else {
                attack = false;
            }
        } else {
            up = false;
            down = false;
            right = false;
            left = false;
        }
    }
}
