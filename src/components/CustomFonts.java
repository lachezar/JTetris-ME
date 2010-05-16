/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package components;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author lucho
 */
public class CustomFonts {
    
    private Graphics g;
    private int color;
    private Image map;
    private int charWidth;
    private int charHeight;
    
    byte[] symbols;
    
    public CustomFonts(Graphics g, Image map, int charWidth, int charHeight) {
        this.g = g;
        this.map = Image.createImage(map);
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        
        symbols = new byte[128];
        for (int i = 0; i < symbols.length; i++) {
            symbols[i] = -1;
        }
        symbols[65]=0;
        symbols[66]=1;
        symbols[67]=2;
        symbols[68]=3;
        symbols[69]=4;
        symbols[70]=5;
        symbols[71]=6;
        symbols[72]=7;
        symbols[73]=8;
        symbols[74]=9;
        symbols[75]=10;
        symbols[76]=11;
        symbols[77]=12;
        symbols[78]=13;
        symbols[79]=14;
        symbols[80]=15;
        symbols[81]=16;
        symbols[82]=17;
        symbols[83]=18;
        symbols[84]=19;
        symbols[85]=20;
        symbols[86]=21;
        symbols[87]=22;
        symbols[88]=23;
        symbols[89]=24;
        symbols[90]=25;
        symbols[48]=26;
        symbols[49]=27;
        symbols[50]=28;
        symbols[51]=29;
        symbols[52]=30;
        symbols[53]=31;
        symbols[54]=32;
        symbols[55]=33;
        symbols[56]=34;
        symbols[57]=35;
        symbols[46]=36;
        symbols[44]=37;
        symbols[33]=38;
        symbols[63]=39;
        symbols[40]=40;
        symbols[41]=41;
        symbols[42]=42;
        symbols[38]=43;
        symbols[35]=44;
        symbols[64]=45;
        symbols[60]=46;
        symbols[62]=47;

    }
    
    public void setFontColor(int color) {
        this.color = color;
    }
    
    public void drawString(String str, int posX, int posY) {
        str = str.toUpperCase();
        int oldColor = g.getColor();
        g.setColor(color);
        g.fillRect(posX, posY, str.length()*charWidth, charHeight);
        
        for (int i = 0; i < str.length(); i++) {
            int n = symbols[(byte)str.charAt(i)];
            if (n != -1) {
                g.drawRegion(map, charWidth*(n%(map.getWidth()/charWidth)), charHeight*(n/(map.getWidth()/charWidth)), charWidth, charHeight, 0, posX, posY, 0);
            }
            posX += charWidth;
        }
        
        g.setColor(oldColor);
    }
    
    public int getWidthOfText(String str) {
        return str.length()*charWidth;
    }

}
