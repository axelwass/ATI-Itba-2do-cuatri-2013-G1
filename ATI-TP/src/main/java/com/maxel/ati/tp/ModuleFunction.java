/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

/**
 *
 * @author maximo
 */
public class ModuleFunction extends FunctionToApply {

    @Override
    public double apply(double... values) {
        double sum = 0;
        for (double d : values) {
            sum += Math.pow(d, 2);
        }
        return Math.sqrt(sum);
    }
}
