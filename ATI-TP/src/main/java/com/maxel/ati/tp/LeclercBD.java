/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

/**
 *
 * @author maximo
 */
public class LeclercBD extends BorderDetector{
    double sigma;

    public LeclercBD(double sigma) {
        this.sigma = sigma;
    }
    
    
    @Override
    public double apply(double x) {
        return Math.exp(-Math.pow(Math.abs(x), 2) / Math.pow(sigma, 2));
    }
    
}
