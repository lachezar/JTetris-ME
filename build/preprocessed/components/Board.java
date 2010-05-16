/*
 * Board.java
 *
 * Created on ������, 2007, �������� 23, 12:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package components;

/**
 * Represents a board - a 2D matrix with different width and height.
 * Values can be stored on specific coordinates and retrieved.
 * @author lucho
 */
public class Board {
    
    private class Matrix {
        private int color;
        private boolean used;
        
        private Matrix() {
            color = NONE;
            used = false;
        }
        
    }
    
    /**
     * Marks lack of color
     */
    public static final int NONE = -1;
    private static Board board = null;
    private Board.Matrix[][] matrix;
    private int width;
    private int height;
    
    protected Board(int width, int height) {
        matrix = new Board.Matrix[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrix[i][j] = new Board.Matrix();
            }
        }
    }
    
    /**
     * Creates board with specific width and height.
     * Board is a <b>singleton</b> class and only one instance can be created.
     * @param width width of the board
     * @param height height of the board
     * @return instance of Board
     */
    public static Board makeBoard(int width, int height) {
        if (board == null) {
            board = new Board(width, height);
            board.width = width;
            board.height = height;
        }
        
        return board;
    }
    
    /**
     * Resizes the current board
     * @param width width of the new board
     * @param height height of the new board
     */
    public void resizeMatrix(int width, int height) {
        matrix = new Board.Matrix[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrix[i][j] = new Board.Matrix();
            }
        }
        board.width = width;
        board.height = height;
    }
    
    /**
     * Obtains the instance of the Board.
     * Board is a <b>singleton</b> class and only one instance can be created.
     * @return instance of Board
     */
    public static Board getInstance() {
        return board;
    }
    
    /**
     * Obtains the color of a specific coordinates
     * @param x x-axis position
     * @param y y-axis position
     * @return color on that coordinates
     */
    public int getPieceColor(int x, int y) {
        return matrix[x][y].color;
    }
    
    /**
     * Set color on a specific coordinates
     * @param x x-axis position
     * @param y y-axis position
     * @param color color to set on that coordinates
     */
    public void setPieceColor(int x, int y, int color) {
        matrix[x][y].color = color;
    }
    
    /**
     * Set color on the whole board
     * @param color color to set on the whole board
     */
    public void setAllPiecesColor(int color) {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                matrix[i][j].color = color;                
            }
        }
    }
    
    /**
     * Obtains the status of a specific coordinates
     * @param x x-axis position
     * @param y y-axis position
     * @return whether the coordinates are marked as (not) used
     */
    public boolean getPieceUsed(int x, int y) {
        return matrix[x][y].used;
    }
    
    /**
     * Set status on a specific coordinates
     * @param x x-axis position
     * @param y y-axis position
     * @param used marks specific coordinates as (not) used
     */
    public void setPieceUsed(int x, int y, boolean used) {
        matrix[x][y].used = used;
    }
    
    /**
     * Set the whle board as used
     * @param used marks the whole board as (not) used
     */
    public void setAllPiecesUsed(boolean used) {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                matrix[i][j].used = used;                
            }
        }
    }

    /**
     * Obtains the width of the board
     * @return width of the board
     */
    public int getWidth() {
        return width;
    }

    /**
     * Obtains the height of the board
     * @return height of the board
     */
    public int getHeight() {
        return height;
    }

        
}
