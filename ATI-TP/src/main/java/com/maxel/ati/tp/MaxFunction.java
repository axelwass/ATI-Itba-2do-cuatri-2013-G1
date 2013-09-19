/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

/**
 *
 * @author maximo
 */
public class MaxFunction extends FunctionToApply {

    @Override
    public double apply(double... values) {
        double sum = values[0];
        for (double d : values) {
            sum = Math.max(sum, d);
        }
        return sum;
    }
}
