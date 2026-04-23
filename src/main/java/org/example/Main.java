package org.example;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        showMenu();
        stage.setTitle("JavaBan");
        stage.show();
    }

    public void showMenu() {
        MenuView menu = new MenuView(this);
        stage.setScene(menu.getScene());
    }

    public void startGame() {
        GameView game = new GameView(this);
        stage.setScene(game.getScene());
    }

    public void quit() {
        stage.close();
    }

    public static void main(String[] args) {
        launch();
    }
}
