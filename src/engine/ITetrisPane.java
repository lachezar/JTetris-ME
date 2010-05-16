/*
 * ITetrisViewer.java
 *
 * Created on ������, 2007, �������� 23, 16:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package engine;

import components.Figure;

/**
 * Rules to be followed when a new <b>Observer</b> of the game is produced.
 * @author lucho
 */
public interface ITetrisPane {
    
    /**
     * The width of each square which forms a figure
     */
    int pieceWidth = 20;
    
    /**
     * The width of the screen
     */
    int width = 0;
    
    /**
     * The height of the screen
     */
    int height = 0;
    
    /**
     * Score of the current game
     */
    int score = 0;
    
    /**
     * Highest score ever achieved
     */
    int highscore = 0;
    
    /**
     * Speed of the game (how fast the figures move)
     */
    int speed = 1;
    
    /**
     * Next figure which will be used in the game
     */
    Figure nextFigure = null;
    
    /**
     * Obtains the width of the squares which forms a figure
     * @return width of the squares whish forms a figure
     */
    int getPieceWidth();

    /**
     * Sets the width of the squares which forms a figure
     * @param pieceWidth the width of the squares which forms a figure
     */
    void setPieceWidth(int pieceWidth);
    
    /**
     * Obtains the score of the current game
     * @return points won in the game
     */
    int getScore();

    /**
     * Sets the score of the current game
     * @param score the number of points which are won
     */
    void setScore(int score);
    
    /**
     * Obtains the highscore
     * @return highest score ever made
     */
    int getHighscore();

    /**
     * Sets the highscore
     * @param highscore highest score ever made
     */
    void setHighscore(int highscore);
    
    /**
     * Obtains the speed of the moving figures on the screen
     * @return the speed of the moving figures on the screen
     */
    int getSpeed();
    
    /**
     * Sets the speed of the moving figures on the screen
     * @param speed the speed of the moving figures on the screen
     */
    void setSpeed(int speed);
    
    /**
     * Obtains the next figure to be shown in the game
     * @return the next figure which will be used
     */
    Figure getNextFigure();

    /**
     * Sets the next figure to be shown in the game
     * @param nextFigure the next figure which will be used
     */
    void setNextFigure(Figure nextFigure);
    
   
}
