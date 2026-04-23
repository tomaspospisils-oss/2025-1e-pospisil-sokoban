package org.example;
import javafx.scene.Scene;

public class GameView {
    private Scene scene;

    public GameView(Main app) {
        SokobanGame game = new SokobanGame();
        scene = game.vytvorScenu(app);
    }

    public Scene getScene() {
        return scene;
    }
}
