package entities;

import gamestates.Playing;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabbyArray;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        crabbies = LoadSave.getCrabs();
        System.out.println("size of crabs: " + crabbies.size());
    }

    public void update(int[][] levelData) {
        for (Crabby c : crabbies) {
            c.update(levelData);
        }
    }

    public void draw(Graphics graphics, int xLevelOffset) {
        drawCrabbies(graphics, xLevelOffset);
    }

    private void drawCrabbies(Graphics graphics, int xLevelOffset) {
        for (Crabby c : crabbies) {
            graphics.drawImage(crabbyArray[c.getEnemyState()][c.getAnimationIndex()], (int) (c.getHitbox().x - xLevelOffset - CRABBY_DRAWOFFSET_X), (int) (c.getHitbox().y - CRABBY_DRAWOFFSET_Y), CRABBY_WIDTH, CRABBY_HEIGHT, null);
        }
    }

    private void loadEnemyImages() {
        crabbyArray = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.CRABBY_SPRITE);

        for (int j = 0; j < crabbyArray.length; j++) {
            for (int i = 0; i < crabbyArray[j].length; i++) {
                crabbyArray[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }

}