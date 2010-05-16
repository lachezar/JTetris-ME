/*
 * Figures.java
 *
 * Created on Неделя, 2007, Декември 23, 13:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package components;

import components.Figure;
import components.CustomList;

/**
 * Contains only constant data, which is used frequently
 * @author lucho
 */
public final class Figures {
    
    private Figures() {};
    
    private static final int NONE = -1;
    private static final int CYAN = 0x00FFFF;
    private static final int BLUE = 0x0000FF;
    private static final int ORANGE = 0xFF7F00;
    private static final int YELLOW = 0xFFFF00;
    private static final int GREEN = 0x008000;
    private static final int PURPLE = 0x800080;
    private static final int RED = 0xFF0000;
    
    
    private static final int[][] fig1 = {
        {CYAN},
        {CYAN},
        {CYAN},
        {CYAN}
    };
    
    private static final int[][] fig2 = {
        {BLUE, NONE, NONE},
        {BLUE, BLUE, BLUE}
    };
    
    private static final int[][] fig3 = {
        {NONE, NONE, ORANGE},
        {ORANGE, ORANGE, ORANGE}
    };
    
    private static final int[][] fig4 = {
        {YELLOW, YELLOW},
        {YELLOW, YELLOW}
    };
    
    private static final int[][] fig5 = {
        {NONE, GREEN, GREEN},
        {GREEN, GREEN, NONE}
    };
    
    private static final int[][] fig6 = {
        {NONE, PURPLE, NONE},
        {PURPLE, PURPLE, PURPLE}
    };
       
    private static final int[][] fig7 = {
        {RED, RED, NONE},
        {NONE, RED, RED}
    };
    
    /**
     * Figure of type I
     */
    public static final Figure figureI = new Figure(fig1);
    /**
     * Figure of type J
     */
    public static final Figure figureJ = new Figure(fig2);
    /**
     * Figure of type L
     */
    public static final Figure figureL = new Figure(fig3);
    /**
     * Figure of type O
     */
    public static final Figure figureO = new Figure(fig4);
    /**
     * Figure of type S
     */
    public static final Figure figureS = new Figure(fig5);
    /**
     * Figure of type T
     */
    public static final Figure figureT = new Figure(fig6);
    /**
     * Figure of type Z
     */
    public static final Figure figureZ = new Figure(fig7);
    
    /**
     * List with all needed figures for tetris game.
     * This list will allow the game to produce equal count of figures types
     * always. The game engine will poll a figure from that list until the list is empty
     * in this way equal count of figures of type I~Z will be used in the game. 
     * When the list is empty just make a new one.
     */
    public static final CustomList figuresList = new CustomList(
            new Object[]{
                figureI, figureJ, figureL, figureO, figureS, figureT, figureZ,
                figureI, figureJ, figureL, figureO, figureS, figureT, figureZ,
                figureI, figureJ, figureL, figureO, figureS, figureT, figureZ,
                figureI, figureJ, figureL, figureO, figureS, figureT, figureZ,
                figureI, figureJ, figureL, figureO, figureS, figureT, figureZ
            }
    );
    
}
