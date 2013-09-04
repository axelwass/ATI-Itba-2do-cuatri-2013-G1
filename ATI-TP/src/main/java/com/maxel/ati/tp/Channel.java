/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author maximo
 */
class Channel {

    double[] values;
    int width;
    int height;

    public Channel(double[] values, int width, int height) {
        this.values = values;
        this.width = width;
        this.height = height;
    }

    Channel(int width, int height) {
        this.width = width;
        this.height = height;
        values = new double[width * height];
    }
    public boolean isValid(int x, int y) {
        return (x >= 0 && y >= 0 && x < width && y < width);
    }
    public void setValue(int x, int y, double v) {
        values[x + y * width] = v;
    }

    public double getValue(int x, int y) {
        return values[x + y * width];
    }

    public void setValue(int i, double v) {
        values[i] = v;
    }

    public double getValue(int i) {
        return values[i];
    }

    public void normalize(double max, double min) {
        max = max - min;
        for (int i = 0; i < values.length; i++) {
            values[i] = ((values[i] - min) * 255) / max;
        }
    }

    public double getMax() {
        double max = values[0];
        for (int i = 0; i < values.length; i++) {
            max = max > values[i] ? max : values[i];
        }
        return max;
    }

    public double getMin() {
        double min = values[0];
        for (int i = 0; i < values.length; i++) {
            min = min < values[i] ? min : values[i];
        }
        return min;
    }

    public void dynamicRangeCompression() {
        double max = getMax();
        double L = 255;
        double c = (L - 1) / Math.log(1 + max);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                values[x + y * width] = c * Math.log(1 + values[x + y * width]);
            }
        }
    }

    public void contrast(int r1, int r2, double y1, double y2) {
        for (int i = 0; i < width * height; i++) {
            double r = values[i];
            double m = 0;
            double b = 0;
            if (r < r1) {
                m = y1 / r1;
                b = 0;
            } else if (r > r2) {
                m = (255 - y2) / (255 - r2);
                b = y2 - m * r2;
            } else {
                m = (y2 - y1) / (r2 - r1);
                b = y1 - m * r1;
            }
            values[i] = m * r + b;
        }

    }

    public void multiply(double v) {
        for (int i = 0; i < values.length; i++) {
            values[i] *= v;
        }
    }

    public void multiply(int i, double v) {
        values[i] *= v;

    }

    public void substract(int i, double value) {
        values[i] -= value;
    }

    public void add(int i, double value) {
        values[i] += value;
    }

    public boolean isAppropiate() {
        for (int i = 0; i < values.length; i++) {
            if (values[i] < 0 || values[i] > 255) {
                return false;
            }
        }
        return true;
    }

    public void umbral(double value) {
        int black = 0;
        int white = 255;
        for (int i = 0; i < width * height; i++) {
            values[i] = values[i] > value ? white : black;
        }
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public void equalize() {
        int[] ocurrences = getOccurrences();
        double[] newChannel = new double[values.length];
        double s_min = 255;
        double s_max = 0;

        for (int i = 0; i < newChannel.length; i++) {
            double grayLevel = values[i];

            double outGrayLevel = 0;
            for (int k = 0; k < grayLevel; k++) {
                outGrayLevel += ocurrences[k];
            }

            newChannel[i] = outGrayLevel / values.length;
            s_min = Math.min(s_min, newChannel[i]);
            s_max = Math.max(s_max, newChannel[i]);

        }
        for (int i = 0; i < newChannel.length; i++) {
            double aux = 255 * (newChannel[i] - s_min) / (s_max - s_min);
            values[i] = Math.ceil(aux);
        }
    }

    private int[] getOccurrences() {
        int[] occu = new int[256];
        for (int i = 0; i < values.length; i++) {
            int c = (int) values[i];
            //check for overflow
            c = c > 255 ? 255 : (c < 0 ? 0 : c);
            occu[c]++;
        }

        return occu;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    
    
    public double applyMask(int x1, int y1, Mask m) {
        double aux = 0;
        for (int x = -m.width / 2; x < m.width / 2 + m.width % 2; x++) {
            for (int y = -m.height / 2; y < m.height / 2 + m.height % 2; y++) {
                if (isValid(x + x1, y + y1)) {
                    aux += m.getValue(x, y) * values[(x1 + x) + (y1 + y) * width];
                } else if (isValid(x1 + x, y1 - y)) {
                    aux += m.getValue(x, y) * values[(x1 + x) + (y1 - y) * width];
                } else if (isValid(x1 - x, y1 + y)) {
                    aux += m.getValue(x, y) * values[(x1 - x) + (y1 + y) * width];
                } else if (isValid(x1 - x, y1 - y)) {
                    aux += m.getValue(x, y) * values[(x1 - x) + (y1 - y) * width];
                }
            }
        }
        return aux;
    }
    
    public double applyMedianMask(int x1, int y1, int height, int width) {
        ArrayList<Double> list = new ArrayList<Double>();

        for (int x = -width / 2; x < width / 2 + width % 2; x++) {
            for (int y = -height / 2; y < height / 2 + height % 2; y++) {
                if (isValid(x + x1, y + y1)) {
                    list.add(values[(x1 + x) + (y1 + y) * getWidth()]);
                } else if (isValid(x1 - x, y1 + y)) {
                    list.add(values[(x1 - x) + (y1 + y) * getWidth()]);
                } else if (isValid(x1 + x, y1 - y)) {
                    list.add(values[(x1 + x) + (y1 - y) * getWidth()]);
                } else if (isValid(x1 - x, y1 - y)) {
                    list.add(values[(x1 - x) + (y1 - y) * getWidth()]);
                }
            }
        }
        Collections.sort(list);
        return list.get(height * width / 2 + height * width % 2);
    }
}
