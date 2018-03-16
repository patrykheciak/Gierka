package pl.patrykheciak.gierka.entities;

import android.content.res.Resources;
import android.graphics.Canvas;

import pl.patrykheciak.gierka.UserInput;

public class World {

    private Highway highway;
    private PlayersCar playersCar;
    private ObstacleCar[] obstacleCars;
    private Reward reward;


    private OnGameStatusListener callback;
    private int lives = 3;
    private int score = 0;
    private boolean gameIsOver = false;
    private int width;
    private int height;

    public World(Resources resources, OnGameStatusListener callback) {
        highway = new Highway(resources);
        playersCar = new PlayersCar(resources);
        obstacleCars = new ObstacleCar[4];
        for (int i = 0; i < obstacleCars.length; i++) {
            obstacleCars[i] = new ObstacleCar(resources, i);
        }
        reward = new Reward(resources);
        this.callback = callback;
    }


    public void update(UserInput userInput) {
        if (!gameIsOver) {

            highway.update();
            playersCar.setInput(userInput);
            playersCar.update();
            for (ObstacleCar c : obstacleCars) {
                c.update();
                boolean isColliding = playersCar.isColliding(c);
                if (isColliding) {
                    lives--;
                    if (lives == 0)
                        gameIsOver = true;
                    if (callback != null)
                        callback.onLivesChanged(lives);
                }
            }
            reward.update();

            if (playersCar.isColliding(reward)) {
                score += 1000;
            }

            score++;
            if (callback != null) {
                callback.onScoreChanged(score);
            }
        }
    }

    public void render(Canvas canvas) {

        highway.render(canvas);
        playersCar.render(canvas);
        for (ObstacleCar c : obstacleCars) {
            c.render(canvas);
        }
        reward.render(canvas);
    }

    public void setScreenSize(int width, int height) {
        this.width = width;
        this.height = height;
        highway.setScreenSize(width, height);
        playersCar.setScreenSize(width, height);
        for (ObstacleCar c : obstacleCars) {
            c.setScreenSize(width, height);
        }
        reward.setScreenSize(width, height);
    }

    public void reset() {
        score = 0;
        lives = 3;
        gameIsOver = false;

        playersCar.init();
        playersCar.setScreenSize(width, height);
        for (ObstacleCar c : obstacleCars) c.initRandom();
        reward.initRandom();

        if (callback != null) {
            callback.onLivesChanged(lives);
            callback.onScoreChanged(score);
        }
    }


    public interface OnGameStatusListener {
        void onLivesChanged(int lives);

        void onScoreChanged(int score);
    }
}
