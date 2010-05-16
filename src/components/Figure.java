/*
 * Figure.java
 *
 * Created on ������, 2007, �������� 23, 13:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package components;

/**
 * Represents shapes (figures), which are designed to be used with Board class.
 * The figures can be easily drawn on board.
 * @author lucho
 */
public class Figure {
    
    private int[][] map;
    
    /**
     * Marks lack of color
     */
    public static final int NONE = -1;
    
    /**
     * Creates new instance of Figure with specific shape
     * @param map 2D array (bitmap) which represents the shape of the figure
     */
    public Figure(int[][] map) {
        this.map = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                this.map[i][j] = map[i][j]; 
            }
        }
    }
    
    /**
     * Creates new instance of Figure with specific shape 
     * and fills the shape with specific color
     * @param map 2D array (bitmap) which represents the shape of the figure
     * @param color which will fill the figure
     */
    public Figure(int[][] map, int color) {
        this.map = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != NONE) {
                    this.map[i][j] = color;
                } else {
                    this.map[i][j] = map[i][j];
                }
            }
        }
    }
    
    /**
     * Creates new instance of Figure form another Figure (Copy constructor)
     * @param figure the figure which will be copied
     */
    public Figure(Figure figure) {
        this.map = new int[figure.getMap().length][figure.getMap()[0].length];
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[0].length; j++) {
                this.map[i][j] = figure.getMap()[i][j]; 
            }
        }
    }

    /**
     * Obtains the bitmap of the current figure.
     * @return 2D array (bitmap) which represents the figure shape
     */
    public int[][] getMap() {
        return map;
    }
    
    /**
     * Rotates the current figure on 90 degrees clockwise.
     */
    public void rotate() {
        int[][] newMap = new int[map[0].length][map.length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                newMap[j][map.length - 1 - i] = map[i][j];
            }
        }
        
        map = newMap;
    }
    
}
