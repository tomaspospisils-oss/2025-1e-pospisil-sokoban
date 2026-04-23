package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuView {

    private final Scene scene;

    public MenuView(Main app) {
        Text title = new Text("JavaBan");
        title.getStyleClass().add("menu-title");

        Button playBtn = new Button("START GAME");
        Button levelsBtn = new Button("LEVEL SELECTION");
        Button quitBtn = new Button("EXIT");

        playBtn.getStyleClass().add("menu-button");
        levelsBtn.getStyleClass().add("menu-button");
        quitBtn.getStyleClass().add("menu-button");

        playBtn.setOnAction(e -> app.startGame());
        quitBtn.setOnAction(e -> app.quit());

        VBox layout = new VBox(15, title, playBtn, levelsBtn, quitBtn);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("menu-container");

        scene = new Scene(layout, 400, 500);

        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }
}