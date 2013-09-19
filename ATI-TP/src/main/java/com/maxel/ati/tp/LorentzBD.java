/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

/**
 *
 * @author maximo
 */
public class LorentzBD extends BorderDetector{
    double sigma;

    public LorentzBD(double sigma) {
        this.sigma = sigma;
    }
    
    
    @Override
    public double apply(double x) {
       return 1/((Math.pow(Math.abs(x), 2) / Math.pow(sigma, 2)) + 1);
    }
    
}
