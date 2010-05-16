/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package engine;

import javax.microedition.lcdui.Graphics;
import components.Figure;
import components.Board;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

/**
 *
 * @author lucho
 */
public class Pane implements ITetrisPane {
    
    private Graphics g;
    private int width;
    private int height;
    private int heightOffset = 0;
    private int pieceWidth;
    private int score = 0;
    private int highscore = 0;
    private int speed = 1;
    private Figure currentFigure;
    private Figure nextFigure;
    private Board board;
    private boolean drawTheBoard = true;
    private boolean drawStats = true;
    private boolean drawGameOver = false;
    private boolean drawPause = false;
    private int fontHeight;
    private int boardOffset;
    private Image splashImage;
    private final String TEXT_HIGHSCORE_WIDTH = "Highscore: 000000";
    
    //private Image charMap;
    //private CustomFonts f;
    
    public Pane(Graphics g, int width, int height) {
        this.g = g;
        this.width = width;
        this.height = height;
        board = Board.getInstance();
    }
    
    void paint() {

        if (drawTheBoard) {
            g.setColor(0x000000);
            g.fillRect(0, 0, board.getWidth()*pieceWidth + 2, height);

            g.setColor(0xFF0000);
            g.drawRect(0, heightOffset-2, board.getWidth()*pieceWidth + 1, board.getHeight()*pieceWidth + 1);

            drawBoard(1, heightOffset-1, 0x000000);
            drawTheBoard = false;
        }
        
        if (drawStats) {
            fontHeight = g.getFont().getHeight();
            boardOffset = board.getWidth()*pieceWidth + 4;
            
            g.setColor(0x000000);
            g.fillRect(board.getWidth()*pieceWidth + 2, 0, width - (board.getWidth()*pieceWidth + 2), height);
            
            drawScore(score);

            drawHighscore(highscore);

            drawSpeed(speed);

            if (nextFigure != null) {
                if (2 + heightOffset + 4*fontHeight + 5*pieceWidth < height) {
                    drawNextFigure(nextFigure, boardOffset + pieceWidth, 2+heightOffset+4*fontHeight);
                } else {
                    drawNextFigure(nextFigure, boardOffset + g.getFont().stringWidth("Next: "), 2+heightOffset+3*fontHeight);
                }
            }
            
            drawStats = false;
        }
        
        if (drawGameOver) {
            g.setColor(0x999999);
            Font oldFont = g.getFont();
            g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
            g.drawString("Game", (board.getWidth()*pieceWidth+1)/2, (board.getHeight()*pieceWidth)/2+heightOffset-g.getFont().getHeight(), Graphics.BOTTOM | Graphics.HCENTER);
            g.drawString("Over", (board.getWidth()*pieceWidth+1)/2, (board.getHeight()*pieceWidth)/2+heightOffset, Graphics.BOTTOM | Graphics.HCENTER);
            g.setFont(oldFont);
            
            drawGameOver = false;
        }
        
        if (drawPause) {
            g.setColor(0x000000);
            g.fillRect(0, 0, width, height);
            Font oldFont = g.getFont();
            g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
            g.setColor(0xEEEEEE);
            g.drawString("Pause", width/2, g.getFont().getHeight(), Graphics.TOP | Graphics.HCENTER);
            g.drawString("Press # to resume", width/2, 3*g.getFont().getHeight(), Graphics.TOP | Graphics.HCENTER);
            g.setFont(oldFont);
            
            drawPause = false;
        }
    }
    
    private void drawBoard(int x, int y, int bgColor) {
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                int color = board.getPieceColor(i, j);
                if (color != Board.NONE) {
                    g.setColor(color);
                    g.fillRect(x + i*pieceWidth, y + j*pieceWidth, pieceWidth, pieceWidth);
                }
            }
        }
    }
    
    private void drawNextFigure(Figure fig, int x, int y) {
        g.setColor(0xFF0000);
        g.drawString("Next: ", boardOffset, heightOffset+3*fontHeight, 0);
        int[][] map = fig.getMap();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != Figure.NONE) {
                    g.setColor(map[i][j]);
                    g.fillRect(x + j*pieceWidth, y + i*pieceWidth, pieceWidth, pieceWidth);
                }
            }
        }
    }
    
    /*private Image getCharMap() {
        if (charMap == null) {
            // write pre-init user code here
            try {
                charMap = Image.createImage("/map.gif");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            // write post-init user code here
        }
        return charMap;
    }*/
    
    private Image getSplashImage() {
        if (splashImage == null) {
            // write pre-init user code here
            try {
                splashImage = Image.createImage("/splash.gif");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            // write post-init user code here
        }
        return splashImage;
    }
    
    public int calculateTextWidth(String str) {
        return g.getFont().stringWidth(str);
    }
    
    public void setBoardDrawingFlag() {
        drawTheBoard = true;
    }
    
    public void setStatsDrawingFlag() {
        drawStats = true;
    }
    
    public void setGameOverFlag() {
        drawGameOver = true;
    }
    
    public void setPauseDrawingFlag() {
        drawPause = true;
    }
    
    public void drawSplashScreen() {
        g.setColor(0x3399CC);
        g.fillRect(0, 0, getWidth(), getHeight());
        splashImage = getSplashImage();
        g.drawImage(splashImage, width/2, height/2, Graphics.HCENTER | Graphics.VCENTER);
    }
    
    private void drawScore(int score) {
        g.setColor(0x00FF00);
        g.drawString("Score: " + String.valueOf(score), boardOffset, heightOffset, 0);
    }
    
    private void drawHighscore(int highscore) {
        g.setColor(0xFF00FF);
        g.drawString(((boardOffset + g.getFont().stringWidth(TEXT_HIGHSCORE_WIDTH) < width) ? "Highscore: " : "Hs: ") + String.valueOf(highscore), boardOffset, heightOffset+2*fontHeight, 0);
    }
    
    private void drawSpeed(int speed) {
        g.setColor(0xFFFF00);
        g.drawString("Speed: " + String.valueOf(speed), boardOffset, heightOffset+fontHeight, 0);
    }
    
    public int getPieceWidth() {
        return pieceWidth;
    }
    
    public void setPieceWidth(int pieceWidth) {
        this.pieceWidth = pieceWidth;
        heightOffset = height - pieceWidth * board.getHeight();
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Figure getNextFigure() {
        return nextFigure;
    }

    public void setNextFigure(Figure nextFigure) {
        this.nextFigure = nextFigure;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
