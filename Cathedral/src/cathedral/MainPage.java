package cathedral;

import asciiPanel.AsciiCharacterData;
import asciiPanel.Render;
import asciiPanel.TileTransformer;
import combatsystem.BodyComponent;
import combatsystem.BodyPart;
import combatsystem.Weapon;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class MainPage extends Page {

    private Viewer viewer;
    private Render[] currentRender;
    private final char V_BAR = '\u00B3';
    private final char DOUBLE_V_BAR = '\u00BA';
    private final char DOUBLE_H_BAR = '\u00CD';
    private final char B_L_DOUBLE_CORNER = '\u00C8';
    private final char T_L_DOUBLE_CORNER = '\u00C9';
    private final char B_R_DOUBLE_CORNER = '\u00BC';
    private final char T_R_DOUBLE_CORNER = '\u00BB';
    //indexes, not size 101 slots, 100 is the last index
    private final int PANEL_WIDTH = 99;
    private final int PANEL_CENTER_WIDTH = 50;
    private final int PANEL_HEIGHT = 34;
    private final int PANEL_CENTER_HEIGHT = 17;
    private double enemyHeight = Controller.getInstance().getCurrentEnemy().getHeight();
    private List<String> enemyWep;
    private BodyComponent focusRef;

    public MainPage() {
        viewer = new Viewer();
//        viewer.addRenderArray(VideoLib.getIntroCutscene());
//        viewer.addRenderArray(VideoLib.getEnemySprite(0, PANEL_CENTER_WIDTH, 1, Color.WHITE, Color.BLACK));
        viewer.play();
        enemyWep = new LinkedList<>();
        for (Weapon w : Controller.getInstance().getCurrentEnemy().getWeaponInventory().getWeapons()) {
            enemyWep.add(w.getName());
        }
        focusRef = BodyComponent.HEAD;
    }

    @Override
    public Render[] getDefaultRender() {
        List<Render> temp = new LinkedList<>();
//        enemy sprite
        Render[] sprite = VideoLib.getEnemySprite(0, PANEL_CENTER_WIDTH, 1, Color.WHITE, Color.BLACK);
        for (Render r : sprite) {
            temp.add(r);
        }
        //VERTICAL
        for (int i = 1; i < PANEL_HEIGHT; i++) {
            temp.add(new Render(DOUBLE_V_BAR, PANEL_CENTER_WIDTH, i, Color.WHITE, Color.BLACK));
            temp.add(new Render(DOUBLE_V_BAR, PANEL_WIDTH, i, Color.WHITE, Color.BLACK));
        }
        //corners
        temp.add(new Render(T_L_DOUBLE_CORNER, PANEL_CENTER_WIDTH, 0, Color.WHITE, Color.BLACK));
        temp.add(new Render(B_L_DOUBLE_CORNER, PANEL_CENTER_WIDTH, PANEL_HEIGHT, Color.WHITE, Color.BLACK));
        temp.add(new Render(T_R_DOUBLE_CORNER, PANEL_WIDTH, 0, Color.WHITE, Color.BLACK));
        temp.add(new Render(B_R_DOUBLE_CORNER, PANEL_WIDTH, PANEL_HEIGHT, Color.WHITE, Color.BLACK));
        //HORIZONTAL
        for (int i = 0; i < PANEL_CENTER_WIDTH - 2; i++) { // -2 from l & r corner
            temp.add(new Render(DOUBLE_H_BAR, PANEL_CENTER_WIDTH + 1 + i, 0, Color.WHITE, Color.BLACK));
            temp.add(new Render(DOUBLE_H_BAR, PANEL_CENTER_WIDTH + 1 + i, PANEL_HEIGHT, Color.WHITE, Color.BLACK));
        }
        //UI DATA
        temp.add(new Render("h: " + enemyHeight, PANEL_WIDTH - 6, 5, Color.WHITE, Color.BLACK));
        for (int i = 0; i < enemyWep.size(); i++) {
            temp.add(new Render(enemyWep.get(i), PANEL_WIDTH - 5, 7 + i, Color.WHITE, Color.BLACK));
        }

        //Return render[]
        Render[] ret = new Render[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            ret[i] = temp.get(i);
        }
        return ret;
    }

    @Override
    public Render[] getUpdateRender() {
        if (viewer.isFinished()) {
            List<Render> temp = new LinkedList<>();

            //ret render[]
            Render[] ret = new Render[temp.size()];
            for (int i = 0; i < temp.size(); i++) {
                ret[i] = temp.get(i);
            }
            return ret;
//            return getDefaultRender();
        } else {
            //viewer render
            return currentRender = viewer.getCurrentRender();

        }
    }

    @Override
    public Command pageAction(int key) {
        switch (key) {
            case 37://left
                leftFocus();
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        c.addAnimation(VideoLib.getBodyComponentRange(focusRef), highlight(c.getCurrentEnemy().getBodyPart(focusRef)));
                    }
                };
            case 38://up
                upFocus();
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        c.addAnimation(VideoLib.getBodyComponentRange(focusRef), highlight(c.getCurrentEnemy().getBodyPart(focusRef)));
                    }
                };
            case 39://right
                rightFocus();
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        c.addAnimation(VideoLib.getBodyComponentRange(focusRef), highlight(c.getCurrentEnemy().getBodyPart(focusRef)));
                    }
                };
            case 40://down
                downFocus();
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        c.addAnimation(VideoLib.getBodyComponentRange(focusRef), highlight(c.getCurrentEnemy().getBodyPart(focusRef)));
                    }
                };
            case 32://space bar
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        c.addActionToQueue(c.getPlayerAction(focusRef));
                    }
                };
        }
        return new Command() {
            @Override
            public void exe(Controller c) {
            }
        };
    }

    private void leftFocus() {
        switch (focusRef) {
            case HEAD:
            case TORSO:
                focusRef = BodyComponent.LARM;
                break;
            case RARM:
                focusRef = BodyComponent.TORSO;
                break;
            case RLEG:
                focusRef = BodyComponent.LLEG;
        }
    }

    private void rightFocus() {
        switch (focusRef) {
            case HEAD:
            case TORSO:
                focusRef = BodyComponent.RARM;
                break;
            case LARM:
                focusRef = BodyComponent.TORSO;
                break;
            case LLEG:
                focusRef = BodyComponent.RLEG;
        }
    }

    private void upFocus() {
        switch (focusRef) {
            case TORSO:
                focusRef = BodyComponent.HEAD;
                break;
            case RARM:
            case LARM:
                focusRef = BodyComponent.HEAD;
                break;
            case LLEG:
            case RLEG:
                focusRef = BodyComponent.TORSO;
        }
    }

    private void downFocus() {
        switch (focusRef) {
            case HEAD:
                focusRef = BodyComponent.TORSO;
                break;
            case TORSO:
                focusRef = BodyComponent.LLEG;
                break;
            case LARM:
                focusRef = BodyComponent.LLEG;
                break;
            case RARM:
                focusRef = BodyComponent.RLEG;
                break;
        }
    }

    private TileTransformer highlight(BodyPart bp) {
        final Color c;
        if (bp.isIsImpaired()) {
            c = Color.RED;
        } else if (bp.isWounded()) {
            c = Color.ORANGE;
        } else {
            c = Color.BLACK;
        }
        return new TileTransformer() {
            @Override
            public void transformTile(int x, int y, AsciiCharacterData data) {
                data.backgroundColor = Color.GREEN;
                data.foregroundColor = c;
            }
        };
    }

    @Override
    public Color getBackgroundColor() {
        return Color.BLACK;
    }

    @Override
    public Color getForegroundColor() {
        return Color.WHITE;
    }

    @Override
    public void playViewer() {
        viewer.play();
    }
}
