/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

/**
 *
 * @author maximo
 */
public class AvgFunction extends FunctionToApply {

    @Override
    public double apply(double... values) {
        double sum = 0;
        for (double d : values) {
            sum += d;
        }
        return sum / values.length;
    }
}
