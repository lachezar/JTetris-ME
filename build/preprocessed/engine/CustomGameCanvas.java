/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package engine;

import javax.microedition.lcdui.game.GameCanvas;
import components.Board;
import components.CustomList;
import components.Figure;
import components.Figures;
import java.util.Random;
import javax.microedition.rms.RecordStore;
import midlet.MainMidlet;

/**
 *
 * @author lucho
 */
public class CustomGameCanvas extends GameCanvas implements Runnable {
    
    private final Figure[] figures = {
        Figures.figureI,
        Figures.figureJ,
        Figures.figureL,
        Figures.figureO,
        Figures.figureS,
        Figures.figureT,
        Figures.figureZ
    };
    
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private int pieceWidth;
    private final String TEXT_MAX_WIDTH_SAMPLE = "Score: 000000";
    private Figure currentFigure;
    private Figure nextFigure;
    private int score = 0;
    private int highscore = 0;
    private final int POINTS_PER_FIGURE = 7;
    private final int POINTS_PER_LINE = 100;
    private final int COMBOS_TO_NEXT_SPEED_LEVEL = 15;
    private int speed = 1;
    private int combos = 0;
    private int figX;
    private int figY;
    private int wait = 10;
    private int cw = 0;
    private Random rand = new Random();
    private int pressedKey;
    private boolean releasedKey;
    private boolean shouldExit = false;
    private boolean paused = false;
    private boolean pausedByUser = false;
    private CustomList figuresList;
    private Pane pane;
    private long keyRepeatTimer;
    private final int KEY_REPEAT_WAIT_TIME = 300;
    
    private String RECORDSTORE_NAME = "hs";
    private int RECORDSTORE_INDEX = 1;
    
    private long startTimer;
    private long endTimer;
    private long adjustSleepTimer;
    private int sleep;
    private Board board;
    private boolean gameOver;
    private MainMidlet midlet;
    private boolean ignoreKeyPress = true;

    public CustomGameCanvas(MainMidlet midlet) {
        super(false);
        this.midlet = midlet;
        board = Board.makeBoard(BOARD_WIDTH, BOARD_HEIGHT);
        pane = new Pane(getGraphics(), getWidth(), getHeight());
        pieceWidth = calculatePieceWidth();
        pane.setPieceWidth(pieceWidth);
    }
    
    public void run() {
        showSplash(2500);
        addCommand(midlet.getExitCommand());
        addCommand(midlet.getNewGameCommand());
        setCommandListener(midlet);
        
        wait = 10;
        cw = 0;
        figuresList = new CustomList(Figures.figuresList);
        currentFigure = new Figure((Figure)figuresList.remove(rand.nextInt()%figuresList.lenght()));        
        nextFigure = new Figure((Figure)figuresList.remove(rand.nextInt()%figuresList.lenght()));        
        pane.setScore(0);
        highscore = readHighscore();
        pane.setHighscore(highscore);
        pane.setSpeed(1);
        pane.setNextFigure(nextFigure);
        figX = board.getWidth() / 2 - 2;
        figY = 0;
        pane.setBoardDrawingFlag();
        pane.setStatsDrawingFlag();
        gameOver = false;
        ignoreKeyPress = false;
        endTimer = System.currentTimeMillis();
 
        /// NOTE: 
        //  Remove those debug variables after the game is ready!
//        Graphics g = getGraphics();
//        int dbgval = 0;
//        int lag = 0;
        ////
        
        while (true) {
            
            startTimer = System.currentTimeMillis();
            
            adjustSleepTimer = (startTimer - endTimer) - 50;
            if (adjustSleepTimer < 0 ) adjustSleepTimer = 0;
            
            if (pausedByUser) paused = true;
            
            if (wait == cw && !gameOver) {
                
                cleanBoard();
                drawFigureOnBoard(currentFigure, figX, figY);
                
                
                figY++;
                cw = 0;
                if (checkToStop()) {
                    
                    if (checkForGameEnd(figX, 1, currentFigure)) {
                        if (highscore < score) {
                            writeHighscore(score);
                            highscore = readHighscore();
                            pane.setHighscore(highscore);
                        }
                        gameOver = true;
                        pane.setGameOverFlag();
                        pane.setStatsDrawingFlag();
                        //shouldRestart = true;
                    } else {
                    
                        figX = adjustFigX(figX, currentFigure);
                        int[][] currentFigureMap = currentFigure.getMap();
                        for (int i = 0; i < currentFigureMap.length; i++) {
                            for (int j = 0; j < currentFigureMap[0].length; j++) {
                                if (currentFigureMap[i][j] != Figure.NONE) {
                                    board.setPieceUsed(figX + j, figY + i - 1, true);
                                }
                            }
                        }

                        if (removeLines()) {
                            score += POINTS_PER_FIGURE;
                        }

                        currentFigure = new Figure(nextFigure);
                        nextFigure = new Figure((Figure)figuresList.remove(rand.nextInt()%figuresList.lenght()));        
                        if (figuresList.lenght() == 0) {
                            figuresList = new CustomList(Figures.figuresList);
                        }
                        
                        pane.setNextFigure(nextFigure);
                        
                        figX = board.getWidth() / 2 - 2;
                        figY = 0;
                        score -= POINTS_PER_FIGURE;
                        if (score < 0) score = 0;
                        
                        pane.setScore(score);

                        speed = combos / COMBOS_TO_NEXT_SPEED_LEVEL;
                        wait = 10 - speed;

                        pane.setSpeed(speed + 1);
                        
                        if (pressedKey != 0 && (pressedKey == KEY_NUM5 || pressedKey == KEY_NUM8 || getGameAction(pressedKey) == DOWN)) {
                            pressedKey = 0;
                            keyRepeatTimer = 0;
                            releasedKey = true;                        
                        }
                        
                    }
                    pane.setStatsDrawingFlag();
                }
                pane.setBoardDrawingFlag();
            }
            cw++;
            
            pane.paint();
            
            //Debug data!
//            g.setColor(0x000000);
//            g.fillRect(getWidth()-3*g.getFont().charWidth('0'), 0, 3*g.getFont().charWidth('0'), g.getFont().getHeight());
//            g.setColor(0xFFFF00);
//            g.drawString(String.valueOf(lag), getWidth()-3*g.getFont().charWidth('0'), 0, 0);
            ///
            
            flushGraphics();
            
            if (!releasedKey && !ignoreKeyPress) {
                if (keyRepeatTimer == -1) {
                    processKey();
                } else if (keyRepeatTimer + KEY_REPEAT_WAIT_TIME < System.currentTimeMillis()) {
                    keyRepeatTimer = -1;
                }
            }
            
            if (shouldExit) return;
            
            endTimer = System.currentTimeMillis();
            
            sleep = 50 - (int) (endTimer - startTimer) - (int) adjustSleepTimer;
//            lag = (int) (endTimer - startTimer);
            
            if (sleep < 0) {
                sleep = 0;
            } else if (sleep > 50) {
                sleep = 50;
            }
            
            if (!paused) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } else {
                synchronized(this) {
                    while (paused) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        
    }
    
    public void keyPressed(int key) {
        
        if (!ignoreKeyPress) {
            pressedKey = key;
            releasedKey = false;
            keyRepeatTimer = System.currentTimeMillis();
            processKey();
        }

    }

    public void keyReleased(int key) {
        
        if (!ignoreKeyPress) {
            keyRepeatTimer = 0;
            releasedKey = true;
        }
        
    }
    
    private void processKey() {
        if (pressedKey != 0 && !gameOver) {
            if (pressedKey == KEY_NUM2 || getGameAction(pressedKey) == UP) {
                Figure temp = new Figure(currentFigure);
                temp.rotate();
                int[][] tempMap = temp.getMap();
                for (int i = 0; i < tempMap.length; i++) {
                    for (int j = 0; j < tempMap[0].length; j++) {
                        int x = adjustFigX(figX, temp);
                        
                        if (figY + i >= board.getHeight() || board.getPieceUsed(x + j, figY + i)) {
                            return;
                        }
                    }
                }
                currentFigure.rotate();
            } else if ((pressedKey == KEY_NUM4 || getGameAction(pressedKey) == LEFT) && figX > 0 && checkLeft()) {
                figX = adjustFigXOnRotate(figX, currentFigure);
                figX--;
            } else if ((pressedKey == KEY_NUM6 || getGameAction(pressedKey) == RIGHT) && figX + currentFigure.getMap()[0].length < board.getWidth() && checkRight()) {
                figX = adjustFigXOnRotate(figX, currentFigure);
                figX++;
            } else if (pressedKey == KEY_NUM5 || pressedKey == KEY_NUM8 || getGameAction(pressedKey) == DOWN) {
                int i;
                figX = adjustFigX(figX, currentFigure);
                int[][] cfmap = currentFigure.getMap();
                int[] border = calculateFigureBorder(currentFigure);
                for (i = figY; i < board.getHeight() - cfmap.length; i++) {
                    for (int j = 0; j < cfmap[0].length; j++) {
                        if (board.getPieceUsed(figX + j, i + border[j] + 1)) {
                            figY = i;
                            return;
                        }
                    }
                }
                figY = i;
            // uncomment to add pause functionality
            } else if (pressedKey == KEY_POUND) { // especially added because of Boby
                pausedByUser = !pausedByUser;
                if (!pausedByUser) {
                    resume();
                } else {
                    pane.setPauseDrawingFlag();
                }
            } else {
                //if the key is not control-key then don't draw
                return;
            }
            
            cleanBoard();
            drawFigureOnBoard(currentFigure, figX, figY);
            pane.setBoardDrawingFlag();
        }
    }
    
    public void restartGame() {
        board.setAllPiecesColor(Board.NONE);
        board.setAllPiecesUsed(false);
        score = 0;
        pane.setScore(score);
        speed = 0;
        pane.setSpeed(speed + 1);
        combos = 0;
        wait = 10;
        cw = 0;
        figuresList = new CustomList(Figures.figuresList);
        currentFigure = new Figure((Figure)figuresList.remove(rand.nextInt()%figuresList.lenght()));        
        nextFigure = new Figure((Figure)figuresList.remove(rand.nextInt()%figuresList.lenght()));        
        pane.setNextFigure(nextFigure);
        figX = board.getWidth() / 2 - 2;
        figY = 0;
        pane.setBoardDrawingFlag();
        pane.setStatsDrawingFlag();
        gameOver = false;
    }
    
    private void drawFigureOnBoard(Figure fig, int x, int y) {
        x = adjustFigX(x, currentFigure);
        int[][] figMap = fig.getMap();
        for (int i = 0; i < figMap.length; i++) {
            for (int j = 0; j < figMap[0].length; j++) {
                if (figMap[i][j] != Figure.NONE) {
                    board.setPieceColor(x + j, y + i, figMap[i][j]);
                }
            }
        }
    }
    
    private void cleanBoard() {
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                if (!board.getPieceUsed(i, j)) {
                    board.setPieceColor(i, j, Board.NONE);
                }
            }
        }
    }
    
    private boolean removeLines() {
        int lines = 0;
        for (int i = 0; i < board.getHeight(); i++) {
            boolean wholeLine = true;
            for (int j = 0; j < board.getWidth(); j++) {
                if (!board.getPieceUsed(j, i)) {
                    wholeLine = false;
                    break;
                }
            }
            if (wholeLine) {
                lines++;
                score += lines * POINTS_PER_LINE;
                for (int k = i; k > 0; k--) {
                    for (int j = 0; j < board.getWidth(); j++) {
                        board.setPieceUsed(j, k, board.getPieceUsed(j, k-1));
                        board.setPieceColor(j, k, board.getPieceColor(j, k-1));
                    }
                }
            }
        }
        combos += lines;
        return (lines > 0);
    }
    
    private boolean checkToStop() {
        int[][] cfmap = currentFigure.getMap();
        if (figY + cfmap.length > board.getHeight()) return true;
        int[] border = calculateFigureBorder(currentFigure);
        int x = adjustFigX(figX, currentFigure);
        for (int i = 0; i < cfmap[0].length && i < board.getWidth() - x; i++) {
            if (board.getPieceUsed(x + i, figY + border[i])) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean checkLeft() {
        int[][] cfmap = currentFigure.getMap();
        int[] border = calculateFigureLeftBorder(currentFigure);
        for (int i = 0; i < cfmap.length; i++) {
            if (board.getPieceUsed((figX + border[i]) - 1, adjustFigY(figY + i))) {
                return false;
            }
        }
        return true;
    }
   
    private boolean checkRight() {
        int[][] cfmap = currentFigure.getMap();
        int[] border = calculateFigureRightBorder(currentFigure);
        for (int i = 0; i < cfmap.length; i++) {
            if (board.getPieceUsed((figX + border[i]) + 1, adjustFigY(figY + i))) {
                return false;
            }
        }
        return true;
    }
    
    private int adjustFigX(int figX, Figure fig) {
        int[][] figMap = fig.getMap();
        if (figMap[0].length > board.getWidth() - figX) {
            figX = board.getWidth() - figMap[0].length;
        }
        
        return figX;
    }
    
    private int adjustFigY(int figY) {
        int y = figY;
        if (y >= board.getHeight()) {
            y = board.getHeight() - 1;
        }
        
        return y;
    }
    
    private int[] calculateFigureBorder(Figure fig) {
        int[][] figMap = fig.getMap();
        int[] border = new int[figMap[0].length];
        for (int j = 0; j < figMap[0].length; j++)  {
            for (int k = figMap.length - 1; k >= 0; k--) {
                if (figMap[k][j] != Figure.NONE) {
                    border[j] = k;
                    break;
                }
            }
        }
        
        return border;
    }
    
    private int[] calculateFigureLeftBorder(Figure fig) {
        int[][] figMap = fig.getMap();
        int[] border = new int[figMap.length];
        for (int i = 0; i < figMap.length; i++) {
            for (int j = 0; j < figMap[0].length; j++) {
                if (figMap[i][j] != Figure.NONE) {
                    border[i] = j;                
                    break;
                }
            }
        }
        
        return border;
    }
    
    private int[] calculateFigureRightBorder(Figure fig) {
        int[][] figMap = fig.getMap();
        int[] border = new int[figMap.length];
        for (int i = 0; i < figMap.length; i++) {
            for (int j = figMap[0].length - 1; j >= 0; j--) {
                if (figMap[i][j] != Figure.NONE) {
                    border[i] = j;                
                    break;
                }
            }
        }
        
        return border;
    }
    
    private boolean checkForGameEnd(int posX, int posY, Figure fig) {
        int[][] figMap = fig.getMap();
        for (int i = 0; i < figMap[0].length; i++) {
            for (int j = 0; j < figMap.length; j++) {
                if (figMap[j][i] != Figure.NONE && board.getPieceUsed(adjustFigX(posX, fig) + i, posY + j)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private int adjustFigXOnRotate(int figX, Figure fig) {
        int[][] figMap = fig.getMap();
        if (board.getWidth() - figMap[0].length < figX) {
            figX = board.getWidth() - figMap[0].length;
        }
        
        return figX;
    }
    
    private int readHighscore() {
        int highscore = 0;
        
        try {
            RecordStore record = RecordStore.openRecordStore(RECORDSTORE_NAME, true);
            if (record.getNumRecords() > 0) {
                byte[] hsBytes = record.getRecord(RECORDSTORE_INDEX);

                if (hsBytes == null) {
                    hsBytes = new byte[]{0, 0, 0, 0};
                }

                for (int i = hsBytes.length-1; i >= 0; i--) {
                    highscore <<= 8;
                    highscore += (hsBytes[i] & 0xFF);
                }
            } else {
                record.addRecord(new byte[4], 0, 4);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return highscore;
    }
    
    private void writeHighscore(int highscore) {
        try {
            byte[] hsBytes = new byte[4];
            for (int i = 0; i < 4; i++) {
                hsBytes[i] |= (byte) (highscore & 0xFF);
                highscore >>= 8;
            }

            RecordStore.openRecordStore(RECORDSTORE_NAME, true).setRecord(RECORDSTORE_INDEX, hsBytes, 0, 4);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
                
    }
    
    private int calculatePieceWidth() {
        int maxHeighSize = (getHeight() - 2) / BOARD_HEIGHT;
        int maxWidthSize = (getWidth() - 2 - pane.calculateTextWidth(TEXT_MAX_WIDTH_SAMPLE)) / BOARD_WIDTH;
        
        return (maxHeighSize > maxWidthSize ? maxWidthSize : maxHeighSize);
    }
    
    public void exit() {
        shouldExit = true;
    }
    
    public void pause() {
        paused = true;
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public void resume() {
        paused = false;
        pane.setBoardDrawingFlag();
        pane.setStatsDrawingFlag();
        synchronized(this) {
            this.notify();
        }
    }
    
    private void showSplash(int time) {
        pane.drawSplashScreen();
        flushGraphics();
        synchronized(this) {
            try {
                wait(time);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
