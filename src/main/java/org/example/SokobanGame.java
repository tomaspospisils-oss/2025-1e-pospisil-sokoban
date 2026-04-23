package org.example;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class SokobanGame {

    private static final int VELIKOST_DLAZDICE = 50;
    private static final int SLOUPCU = 8;
    private static final int RADKU   = 8;

    private final int[][] mapa = {
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 0, 1},
            {1, 0, 3, 0, 1, 2, 0, 1},
            {1, 0, 0, 0, 0, 2, 0, 1},
            {1, 0, 3, 0, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1}
    };

    private int hracX = 1, hracY = 1;
    private List<Bod> bedny = new ArrayList<>();
    private String zprava = "";
    private boolean konecHry = false;


    public Scene vytvorScenu(Main aplikace) {
        Canvas platno = new Canvas(SLOUPCU * VELIKOST_DLAZDICE, RADKU * VELIKOST_DLAZDICE);
        GraphicsContext gc = platno.getGraphicsContext2D();

        StackPane koren = new StackPane(platno);
        Scene scena = new Scene(koren);

        resetujHru();

        scena.setOnKeyPressed(e -> {

            if (e.getCode() == KeyCode.ESCAPE) {
                aplikace.showMenu();
                return;
            }

            if (konecHry) {
                resetujHru();
                vykresli(gc);
                return;
            }

            int dx = 0, dy = 0;
            if      (e.getCode() == KeyCode.W) dy = -1;
            else if (e.getCode() == KeyCode.S) dy =  1;
            else if (e.getCode() == KeyCode.A) dx = -1;
            else if (e.getCode() == KeyCode.D) dx =  1;

            if (dx != 0 || dy != 0) {
                pohniHracem(dx, dy);
                zkontrolujStavHry();
                vykresli(gc);
            }
        });

        vykresli(gc);
        return scena;
    }

    private void resetujHru() {
        hracX = 1;
        hracY = 1;
        bedny.clear();
        bedny.add(new Bod(2, 2));
        bedny.add(new Bod(2, 4));
        zprava   = "";
        konecHry = false;
    }

    private void pohniHracem(int dx, int dy) {
        int dalsiX = hracX + dx;
        int dalsiY = hracY + dy;

        if (mapa[dalsiY][dalsiX] == 1) return;

        Bod bedna = najdiBednuNa(dalsiX, dalsiY);
        if (bedna != null) {
            int bednaDalsiX = dalsiX + dx;
            int bednaDalsiY = dalsiY + dy;

            boolean zedZaBednou   = mapa[bednaDalsiY][bednaDalsiX] == 1;
            boolean bednaZaBednou = najdiBednuNa(bednaDalsiX, bednaDalsiY) != null;

            if (zedZaBednou || bednaZaBednou) return;

            bedna.x = bednaDalsiX;
            bedna.y = bednaDalsiY;
        }

        hracX = dalsiX;
        hracY = dalsiY;
    }

    private void zkontrolujStavHry() {
        boolean vsechnyNaCili = bedny.stream().allMatch(b -> mapa[b.y][b.x] == 2);
        if (vsechnyNaCili) {
            zprava   = "YOU WIN";
            konecHry = true;
            return;
        }

        for (Bod b : bedny) {
            if (mapa[b.y][b.x] != 2 && jeBednaZasekana(b)) {
                zprava   = "YOU LOSE";
                konecHry = true;
                break;
            }
        }
    }

    private boolean jeBednaZasekana(Bod b) {
        boolean zedNahore = mapa[b.y - 1][b.x] == 1;
        boolean zedDole   = mapa[b.y + 1][b.x] == 1;
        boolean zedVlevo  = mapa[b.y][b.x - 1] == 1;
        boolean zedVpravo = mapa[b.y][b.x + 1] == 1;

        return (zedNahore && zedVlevo)
                || (zedNahore && zedVpravo)
                || (zedDole   && zedVlevo)
                || (zedDole   && zedVpravo);
    }

    private Bod najdiBednuNa(int x, int y) {
        return bedny.stream()
                .filter(b -> b.x == x && b.y == y)
                .findFirst()
                .orElse(null);
    }

//mapa vykreslit
    private void vykresli(GraphicsContext gc) {

        for (int y = 0; y < RADKU; y++) {
            for (int x = 0; x < SLOUPCU; x++) {

                if (mapa[y][x] == 1) {
                    gc.setFill(Color.LIGHTGRAY);
                } else {
                    gc.setFill(Color.DARKSLATEGRAY);
                }
                gc.fillRect(x * VELIKOST_DLAZDICE, y * VELIKOST_DLAZDICE,
                        VELIKOST_DLAZDICE, VELIKOST_DLAZDICE);

                if (mapa[y][x] == 2) {
                    gc.setFill(Color.LIMEGREEN);
                    gc.fillOval(
                            x * VELIKOST_DLAZDICE + 10,
                            y * VELIKOST_DLAZDICE + 10,
                            30, 30
                    );
                }
            }
        }

        gc.setFill(Color.BROWN);
        for (Bod b : bedny) {
            gc.fillRect(
                    b.x * VELIKOST_DLAZDICE + 5,
                    b.y * VELIKOST_DLAZDICE + 5,
                    VELIKOST_DLAZDICE - 10,
                    VELIKOST_DLAZDICE - 10
            );
        }

        gc.setFill(Color.RED);
        gc.fillOval(
                hracX * VELIKOST_DLAZDICE + 5,
                hracY * VELIKOST_DLAZDICE + 5,
                VELIKOST_DLAZDICE - 10,
                VELIKOST_DLAZDICE - 10
        );

        if (!zprava.isEmpty()) {
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            gc.fillText(zprava, 100, RADKU * VELIKOST_DLAZDICE / 2);
        }
    }

    private static class Bod {
        int x, y;

        Bod(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}