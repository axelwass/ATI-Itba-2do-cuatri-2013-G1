/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

/**
 *
 * @author maximo
 */


public class Mask {
    public double[][] values;
    int height;
    int width;
    
    public static Mask newMeanFilter(int width, int height){
     Mask m = new Mask(width, height);
     for(int x=-width/2;x<width/2 + width%2;x++){
         for(int y=-height/2;y<height/2 + height%2;y++){
             m.setValue(x, y, 1.0/(double)(width*height));
         }
     }
     return m;
    }
    
     public static Mask newHighAmpFilter(int width, int height){
     Mask m = new Mask(width, height);
     for(int x=-width/2;x<width/2+ width%2;x++){
         for(int y=-height/2;y<height/2 + height%2;y++){
             if(x==0 && y==0){
                 m.setValue(x, y,(width*height-1.0)/(double)(width*height));
             }else{
                m.setValue(x, y,-1.0/(double)(width*height));
             }
         }
     }
     return m;
    }
    
    public Mask(int width, int height) {
        this.height = height;
        this.width = width;
        values = new double[width][height];
    }
    public Mask(int size) {
        this.height = size;
        this.width = size;
        values = new double[size][size];
    }
    
    public double getValue(int x, int y){
        x = x + width/2;
        y = y + height/2;
        return values[x][y];
    }
    
    public boolean isValid(int x, int y){
        return (x+width/2 >= 0)&&(x<width/2+ width%2)&&(y+height/2 >= 0)&&(y<height/2 + height%2);
    }
    
    public void setValue(int x, int y, double value){
        if(isValid(x, y)){
            x = x + width/2;
            y = y + height/2;
            values[x][y] = value;
        }
    }
    
}
