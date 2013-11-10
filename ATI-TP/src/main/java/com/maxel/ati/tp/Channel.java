/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.jfree.data.Range;

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
        return (x >= 0 && y >= 0 && x < width && y < height);
    }

    public void setValue(int x, int y, double v) {
        if (isValid(x, y)) {
            values[x + y * width] = v;
        }
    }

    public double getValue(int x, int y) {
        if (!isValid(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        return values[x + y * width];
    }

    public void setValue(int i, double v) {
        if (i>=0 && i<values.length) {
            values[i] = v;
        }
    }

    public double getValue(int i) {
         if (i<0 || i>=values.length) {
            throw new IndexOutOfBoundsException();
        }
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

    public void module(int i, double v) {
        values[i] = Math.hypot(values[i], v);
    }

    public void module(int i, double v, double w, double x) {
        values[i] = Math.sqrt(values[i] * values[i] + v * v + w * w + x * x);
    }

    public void applyFunction(FunctionToApply fn, Channel... channels) {
        double[] result = new double[width * height];

        // Iterates through all the pixels of the channel
        // In every loop takes the i pixel from all the channels and apply the
        // synth function
        for (int i = 0; i < values.length; i++) {
            double[] colors = new double[channels.length + 1];
            colors[0] = this.values[i];
            for (int j = 1; j <= channels.length; j++) {
                colors[j] = channels[j - 1].values[i];
            }
            result[i] = fn.apply(colors);
        }
        this.values = result;
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

    public double applyMask(int x1, int y1, Mask mask) {
//        double aux = 0;
//        for (int x = -m.width / 2; x < m.width / 2 + m.width % 2; x++) {
//            for (int y = -m.height / 2; y < m.height / 2 + m.height % 2; y++) {
//                if (isValid(x + x1, y + y1)) {
//                    aux += m.getValue(x, y) * values[(x1 + x) + (y1 + y) * width];
//                } else if (isValid(x1 + x, y1 - y)) {
//                    aux += m.getValue(x, y) * values[(x1 + x) + (y1 - y) * width];
//                } else if (isValid(x1 - x, y1 + y)) {
//                    aux += m.getValue(x, y) * values[(x1 - x) + (y1 + y) * width];
//                } else if (isValid(x1 - x, y1 - y)) {
//                    aux += m.getValue(x, y) * values[(x1 - x) + (y1 - y) * width];
//                }
//            }
//        }
//        return aux;
        double newColor = 0;
        for (int i = -mask.getWidth() / 2; i <= mask.getWidth() / 2; i++) {
            for (int j = -mask.getHeight() / 2; j <= mask.getHeight() / 2; j++) {
                if (this.isValid(x1 + i, y1 + j)) {
                    double oldColor = 0;
                    try {
                        oldColor = this.getValue(x1 + i, y1 + j);
                        newColor += oldColor * mask.getValue(i, j);
                    } catch (IndexOutOfBoundsException e) {
                        newColor += oldColor * mask.getValue(i, j);
                    }
                }
            }
        }
        return newColor;
    }

    public void applyMask(Mask m) {
        Channel aux = new Channel(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                aux.setValue(x, y, this.applyMask(x, y, m));
            }
        }
        this.setValues(aux.values);
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

    public void localVarianceEvaluation(double variance) {
        double[] newChannel = new double[values.length];
        for (int i = 1; i < this.values.length; i++) {
            double n1 = values[i - 1];
            double n2 = values[i];
            if (n1 > 0 && n2 < 0 && Math.abs(n1 - n2) > variance) {
                newChannel[i] = 255;
            } else if (n1 < 0 && n2 > 0 && Math.abs(-n1 + n2) > variance) {
                newChannel[i] = 255;
            } else {
                newChannel[i] = 0;
            }
        }
        this.values = newChannel;
    }

    public void zeroCross(double threshold) {

        double[] hChannel = new double[values.length];
        double[] vChannel = new double[values.length];

        double last;
        for (int y = 0; y < this.getHeight(); y++) {
            double past = 0;
            for (int x = 0; x < this.getWidth(); x++) {


                double current = 0;

                if (x + 1 < width) {
                    current = this.getValue(x + 1, y);
                    last = past;
                    past = this.getValue(x, y);

                    if (past == 0 && x > 0) {
                        past = last;
                    }

                    if (((current < 0 && past > 0) || (current > 0 && past < 0))
                            && Math.abs(current - past) > threshold) {
                        hChannel[y * this.getWidth() + x] = 255;
                    }
                }
            }
        }
        for (int x = 0; x < this.getWidth(); x++) {
            double past = 0;
            for (int y = 0; y < this.getHeight(); y++) {

                double current = 0;
                if (y + 1 < height) {
                    current = this.getValue(x, y + 1);
                    last = past;
                    past = this.getValue(x, y);

                    if (past == 0 && x > 0) {
                        past = last;
                    }

                    if (((current < 0 && past > 0) || (current > 0 && past < 0))
                            && Math.abs(current - past) > threshold) {
                        vChannel[y * this.getWidth() + x] = 255;
                    }
                }
            }
        }
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                vChannel[y * this.getWidth() + x] += hChannel[y
                        * this.getWidth() + x];
                if (vChannel[y * this.getWidth() + x] > 255) {
                    vChannel[y * this.getWidth() + x] = 255;
                }
            }
        }

        this.values = vChannel;
    }

    public void globalThreshold() {
        double globalThreshold = getGlobalThresholdValue();
        umbral(globalThreshold);
    }

    private double getGlobalThresholdValue() {
        double deltaT = 10;
        // Step 1
        double currentT = 50;
        double previousT = 0;

        // Step 5
        int i = 0;
        do {
            previousT = currentT;
            currentT = getAdjustedThreshold(currentT);
            i++;
        } while (Math.abs((currentT - previousT)) >= deltaT);
        System.out.println("Iteraciones: " + i);
        System.out.println("T: " + currentT);
        return currentT;
    }

    /**
     * Calculates de new T.
     *
     * @param previousThreshold
     * @return new threshold
     */
    private double getAdjustedThreshold(double previousThreshold) {
        double amountOfHigher = 0;
        double amountOfLower = 0;

        double sumOfHigher = 0;
        double sumOfLower = 0;

        // Step 3
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double aPixel = this.getValue(x, y);
                if (aPixel >= previousThreshold) {
                    amountOfHigher += 1;
                    sumOfHigher += aPixel;
                }
                if (aPixel < previousThreshold) {
                    amountOfLower += 1;
                    sumOfLower += aPixel;
                }
            }
        }
        double n1 = amountOfHigher;
        double n2 = amountOfLower;
        double m1 = (1 / n1) * sumOfHigher;
        double m2 = (1 / n2) * sumOfLower;

        // Step 4
        return 0.5 * (m1 + m2);
    }

    public void applyAnisotropicDiffusion(int iterations, BorderDetector bd) {
        Channel auxiliarChannel = new Channel(values.clone(), width, height);

        for (int t = 0; t < iterations; t++) {
            auxiliarChannel = applyAnisotropicDiffusion(auxiliarChannel, bd);
        }
        this.values = auxiliarChannel.values;
    }

    public void applyIsotropicDiffusion(int iterations, BorderDetector bd) {
        Channel auxiliarChannel = new Channel(values.clone(), width, height);

        for (int t = 0; t < iterations; t++) {
            auxiliarChannel = applyIsotropicDiffusion(auxiliarChannel, bd);
        }
        this.values = auxiliarChannel.values;
    }

    private Channel applyAnisotropicDiffusion(Channel oldChannel,
            BorderDetector bd) {
        Channel modifiedChannel = new Channel(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double oldValueIJ = oldChannel.getValue(i, j);

                double DnIij = i > 0 ? oldChannel.getValue(i - 1, j)
                        - oldValueIJ : 0;
                double DsIij = i < width - 1 ? oldChannel.getValue(i + 1, j)
                        - oldValueIJ : 0;
                double DeIij = j < height - 1 ? oldChannel.getValue(i, j + 1)
                        - oldValueIJ : 0;
                double DoIij = j > 0 ? oldChannel.getValue(i, j - 1)
                        - oldValueIJ : 0;

                double Cnij = bd.apply(DnIij);
                double Csij = bd.apply(DsIij);
                double Ceij = bd.apply(DeIij);
                double Coij = bd.apply(DoIij);

                double DnIijCnij = DnIij * Cnij;
                double DsIijCsij = DsIij * Csij;
                double DeIijCeij = DeIij * Ceij;
                double DoIijCoij = DoIij * Coij;

                double lambda = 0.25;
                double newValueIJ = oldValueIJ + lambda
                        * (DnIijCnij + DsIijCsij + DeIijCeij + DoIijCoij);
                modifiedChannel.setValue(i, j, newValueIJ);
            }
        }

        return modifiedChannel;
    }

    private Channel applyIsotropicDiffusion(Channel oldChannel,
            BorderDetector bd) {
        Channel modifiedChannel = new Channel(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double oldValueIJ = oldChannel.getValue(i, j);

                double DnIij = i > 0 ? oldChannel.getValue(i - 1, j)
                        - oldValueIJ : 0;
                double DsIij = i < width - 1 ? oldChannel.getValue(i + 1, j)
                        - oldValueIJ : 0;
                double DeIij = j < height - 1 ? oldChannel.getValue(i, j + 1)
                        - oldValueIJ : 0;
                double DoIij = j > 0 ? oldChannel.getValue(i, j - 1)
                        - oldValueIJ : 0;

                double Cnij = 1;
                double Csij = 1;
                double Ceij = 1;
                double Coij = 1;

                double DnIijCnij = DnIij * Cnij;
                double DsIijCsij = DsIij * Csij;
                double DeIijCeij = DeIij * Ceij;
                double DoIijCoij = DoIij * Coij;

                double lambda = 0.25;
                double newValueIJ = oldValueIJ + lambda
                        * (DnIijCnij + DsIijCsij + DeIijCeij + DoIijCoij);
                modifiedChannel.setValue(i, j, newValueIJ);
            }
        }

        return modifiedChannel;
    }

    // Return histogram of grayscale image
    public int[] imageHistogram() {

        int[] histogram = new int[256];
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }
        for (int i = 0; i < values.length; i++) {
            int val = (int) values[i];
            histogram[val]++;
        }
        return histogram;

    }

    public void otsuThreshold() {

        int[] histogram = imageHistogram();
        int total = values.length;

        float sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += i * histogram[i];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) {
                continue;
            }
            wF = total - wB;

            if (wF == 0) {
                break;
            }

            sumB += i * histogram[i];
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
        System.out.println("T = " + threshold);
        umbral(threshold);

    }

    @Override
	public Channel clone() {
        return new Channel(values.clone(), width, height);
    }

    public void suppressNoMaxs() {
        Mask mdx = Mask.newDxSobel();
        Mask mdy = Mask.newDySobel();
        Channel G1 = this.clone();
        G1.applyMask(mdx);
        Channel G2 = this.clone();
        G2.applyMask(mdy);

        // obtenemos ángulo del gradiente
        Channel direction = new Channel(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double pxG1 = G1.getValue(x, y);
                double pxG2 = G2.getValue(x, y);
                double anAngle = 0;
                if (pxG2 != 0) {
                    anAngle = Math.atan(pxG1 / pxG2);
                }
                anAngle *= (180 / Math.PI);
                direction.setValue(x, y, anAngle);
            }
        }

        G1.applyFunction(new ModuleFunction(), G2);
        this.values = G1.values;

        suppressNoMaxs(direction);
    }

    private void suppressNoMaxs(Channel directionChannel) {
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                double pixel = getValue(x, y);
                if (pixel == 0) {
                    continue;
                }

                double direction = directionChannel.getValue(x, y);
                double neighbor1 = 0;
                double neighbor2 = 0;
                if (direction >= -90 && direction < -45) {
                    neighbor1 = getValue(x, y - 1);
                    neighbor2 = getValue(x, y + 1);
                } else if (direction >= -45 && direction < 0) {
                    neighbor1 = getValue(x + 1, y - 1);
                    neighbor2 = getValue(x - 1, y + 1);
                } else if (direction >= 0 && direction < 45) {
                    neighbor1 = getValue(x + 1, y);
                    neighbor2 = getValue(x - 1, y);
                } else if (direction >= 45 && direction <= 90) {
                    neighbor1 = getValue(x + 1, y + 1);
                    neighbor2 = getValue(x - 1, y - 1);
                }

                // Si los vecinos superan se borran
                if (neighbor1 > pixel || neighbor2 > pixel) {
                    setValue(x, y, 0);
                }
            }
        }
    }

    void applyHistUmbralization(int t1, int t2) {
        Channel auxChannel = clone();
        for (int i = 0; i < auxChannel.values.length; i++) {
            if (auxChannel.getValue(i) > t2) {
                auxChannel.setValue(i, 255);
            } else if (auxChannel.getValue(i) < t1) {
                auxChannel.setValue(i, 0);
            }
        }
        Channel auxChannel2 = auxChannel.clone();
        for (int x = 0; x < auxChannel.width; x++) {
            for (int y = 0; y < auxChannel.height; y++) {
                double pixel = auxChannel.getValue(x, y);
                if (pixel != 0 && pixel != 255) {
                    for (Double d : getValidNeighbors(x, y)) {
                        if (d == 255) {
                            pixel = 255;
                            auxChannel2.setValue(x, y, pixel);
                            break;
                        }
                    }
                    if (pixel != 255) {
                        auxChannel2.setValue(x, y, 0);
                    }
                }
            }
        }
        this.values = auxChannel2.values;
    }

    private List<Double> getValidNeighbors(int x, int y) {
        List<Double> l = new LinkedList<Double>();
        if (isValid(x - 1, y)) {
            l.add(getValue(x - 1, y));
        }
        if (isValid(x, y - 1)) {
            l.add(getValue(x, y - 1));
        }
        if (isValid(x + 1, y)) {
            l.add(getValue(x + 1, y));
        }
        if (isValid(x, y + 1)) {
            l.add(getValue(x, y + 1));
        }
        return l;
    }

    void applyCanny() {
        Channel auxChannel = clone();
        auxChannel.applyMask(Mask.newGaussianMask(11, 0.5));
        auxChannel.suppressNoMaxs();
        int umbral = (int) auxChannel.getGlobalThresholdValue();
        auxChannel.applyHistUmbralization(umbral, umbral + 40);
        this.values = auxChannel.values;
    }

    Channel applySusan(boolean showBorders, boolean showCorners, int color) {
        Mask mask = Mask.newSusanMask();
        Channel newChannel = new Channel(this.width, this.height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double newValue = getValue(x, y);
                double s_ro = applySusanPixelMask(x, y, mask);
                if ((showBorders && isBorder(s_ro))
                        || (showCorners && isCorner(s_ro))) {
                    newValue = color;
                    newChannel.setValue(x, y, newValue);
                }
                
            }
        }
        return newChannel;
    }

    private boolean isBorder(double a) {
        return a > 0.40 && a < 0.6;
    }

    private boolean isCorner(double a) {
        return a > 0.6 && a < 0.85;
    }

    private double applySusanPixelMask(int x, int y, Mask mask) {
        boolean ignoreByX = x <= mask.getWidth() / 2
                || x >= this.getWidth() - mask.getWidth() / 2;
        boolean ignoreByY = y <= mask.getHeight() / 2
                || y >= this.getHeight() - mask.getHeight() / 2;
        if (ignoreByX || ignoreByY) {
            return 1;
        }

        final int threshold = 27;
        int nRo = 0;
        double ro = this.getValue(x, y);
        for (int i = -mask.getWidth() / 2; i <= mask.getWidth() / 2; i++) {
            for (int j = -mask.getHeight() / 2; j <= mask.getHeight() / 2; j++) {
                if (this.isValid(x + i, y + j) && mask.getValue(i, j) == 1) {
                    double eachPixel = this.getValue(x + i, y + j);
                    //sumo todos los pixeles q cumplen con el umbral
                    if (Math.abs(ro - eachPixel) < threshold) {
                        nRo += 1;
                    }
                }
            }
        }

        final double N = 37.0;
        double sRo = 1 - nRo / N;
        return sRo;
    }

    void houghLinesTransform(double eps, int color) {
        double white = 255;
        double D = Math.max(width, height);
        Range roRange = new Range(-Math.sqrt(2) * D, Math.sqrt(2) * D);
        Range thetaRange = new Range(-90, 90);
        
        int roSize = (int) (Math.abs(roRange.getLength()));
        int thetaSize = (int) (Math.abs(thetaRange.getLength()));
        thetaSize /= 15;
        int[][] A = new int[roSize][thetaSize];

        // Step 3
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double pixel = getValue(x, y);
                if (pixel == white) {
                    // Iterates theta (j) from 1 to m
                    for (int theta = 0; theta < thetaSize; theta++) {
                        double thetaValue = thetaRange.getLowerBound() + theta*15;
                        double thetaTerm = x
                                * Math.cos(thetaValue * Math.PI / 180) - y
                                * Math.sin(thetaValue * Math.PI / 180);

                        // Iterates ro (i) from 1 to n
                        for (int ro = 0; ro < roSize; ro++) {
                            double roValue = roRange.getLowerBound() + ro;
                            double total = roValue - thetaTerm;
                            // If verifies the normal equation of the line, add
                            // 1 to the acumulator
                            // Step 4
                            if (Math.abs(total) < eps) {
                                // The maximum values from this vector, gives
                                // the most voted positions.
                                A[ro][theta] += 1;
                            }
                        }
                    }
                }
            }
        }

        // Step 5
        Set<RoThetaLine> set;
        set = new HashSet<RoThetaLine>();
        for (int ro = 0; ro < roSize; ro++) {
            for (int theta = 0; theta < thetaSize; theta++) {
                RoThetaLine roTheta = new RoThetaLine(ro, theta*15,
                        A[ro][theta]);
                set.add(roTheta);

            }
        }

        // Generates a descending sorted list.
        List<RoThetaLine> list = new ArrayList<RoThetaLine>(
                set);
        Collections.sort(list);

        Channel newChannel = new Channel(width, height);
        // Gets the max vote number
        int maxVotes = list.get(0).ammount;
        if (maxVotes > 1) {
            for (RoThetaLine b : list) {
                System.out.println("ro: " + b.ro + " thetha: " + b.theta + " ammount: " + b.ammount);
                // Only for those with max votes
                if (b.ammount < maxVotes/6 - 1) {
                    break;
                }

                double roValue = roRange.getLowerBound() + b.ro;
                double thetaValue = thetaRange.getLowerBound() + b.theta;

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        double thetaTerm = x
                                * Math.cos(thetaValue * Math.PI / 180) - y
                                * Math.sin(thetaValue * Math.PI / 180);
                        double total = roValue - thetaTerm;
                        // Step 6
                        if (Math.abs(total) < eps && isValid(x, y)) {
                            newChannel.setValue(x, y, color);
                        }
                    }
                }

            }
        }
        this.values = newChannel.values;
    }

    void houghCirclesTransform(double eps, int color) {
        double whiteColor = 255;
        Range aRange = new Range(0, width);
        Range bRange = new Range(0, height);

        // TODO: Verificar si no es el min aca
        double maxRad = Math.max(width, height);
        Range rRange = new Range(25, 35);

        int aSize = (int) (Math.abs(aRange.getLength())) - (int)rRange.getLowerBound();
        int bSize = (int) (Math.abs(bRange.getLength())) - (int)rRange.getLowerBound();
        int rSize = (int) (Math.abs(rRange.getLength()));
        int[][][] A = new int[aSize][bSize][rSize];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double pixel = getValue(x, y);
                if (pixel == whiteColor) {
                    for (int a = 0 + (int)rRange.getLowerBound(); a < aSize; a++) {
                        double aValue = aRange.getLowerBound() + a;
                        double aTerm = Math.pow(x - aValue, 2);
                        for (int b = 0 + (int)rRange.getLowerBound(); b < bSize; b++) {
                            double bValue = bRange.getLowerBound() + b;
                            double bTerm = Math.pow(y - bValue, 2);
                            for (int r = 0; r < rSize; r++) {
                                double rValue = rRange.getLowerBound() + r;
                                double rTerm = Math.pow(rValue, 2);
                                double total = rTerm - aTerm - bTerm;
                                if (Math.abs(total) < eps) {
                                    A[a][b][r] += 1;
                                }
                            }
                        }
                    }
                }
            }
        }

        Set<Circle> circles = new HashSet<Circle>();
        for (int a = 0; a < aSize; a++) {
            for (int b = 0; b < bSize; b++) {
                for (int r = 0; r < rSize; r++) {
                    if (A[a][b][r] > 0) {
                        Circle circle = new Circle(a, b,
                                r, A[a][b][r]);
                        circles.add(circle);
                    }
                }
            }
        }

        if (circles.isEmpty()) {
            return;
        }

        List<Circle> circleList = new ArrayList<Circle>(
                circles);
        Collections.sort(circleList);

        Channel newChannel = new Channel(width, height);
        int maxHits = circleList.get(0).votes;
        int maxAns = 20;
        if (maxHits > 2) {
            for (Circle b : circleList) {
                if (b.votes < maxHits*0.4 || maxAns--==0) {
                    break;
                }
                double aValue = aRange.getLowerBound() + b.a;
                double bValue = bRange.getLowerBound() + b.b;
                double rValue = rRange.getLowerBound() + b.r;
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        double aTerm = Math.pow(x - aValue, 2);
                        double bTerm = Math.pow(y - bValue, 2);
                        double rTerm = Math.pow(rValue, 2);
                        double total = rTerm - aTerm - bTerm;
                        if (Math.abs(total) < 10 * eps && isValid(x, y)) {
                            newChannel.setValue(x, y, color);
                        }
                    }
                }

            }
        }

        this.values = newChannel.values;

    }

         private class Corner {
                int x, y;
                double measure;

                public Corner(int x, int y, double measure) {
                        this.x = x;
                        this.y = y;
                        this.measure = measure;
                }
        }
        public List<java.awt.Point> applyHarrisCornerDetector(int maskSize, double sigma, double umbral,
                        double k) {
                double[][] Lx2 = new double[width][height];
                double[][] Ly2 = new double[width][height];
                double[][] Lxy = new double[width][height];
                
                List<Corner> corners = new ArrayList<Corner>();

                // precompute derivatives
                computeDerivatives(maskSize, sigma, Lx2, Ly2, Lxy);

                // Harris measure map
                double[][] harrismap = computeHarrisMap(k, Lx2, Ly2, Lxy);
                
                // for each pixel in the hmap, keep the local maxima
                for (int y = 1; y < this.height - 1; y++) {
                        for (int x = 1; x < this.width - 1; x++) {
                                double h = harrismap[x][y];
                                if (h < umbral)
                                        continue;
                                if (!isSpatialMaxima(harrismap, x, y))
                                        continue;
                                // add the corner to the list
                                corners.add(new Corner(x, y, h));
                        }
                }

                // remove corners to close to each other (keep the highest measure)
                Iterator<Corner> iter = corners.iterator();
                while (iter.hasNext()) {
                        Corner p = iter.next();
                        for (Corner n : corners) {
                                if (n == p)
                                        continue;
                                int dist = (int) Math.sqrt((p.x - n.x) * (p.x - n.x)
                                                + (p.y - n.y) * (p.y - n.y));
                                if (dist > 3)
                                        continue;
                                if (n.measure < p.measure)
                                        continue;
                                iter.remove();
                                break;
                        }
                }


                List<Point> points = new ArrayList<Point>();
                for (Corner corner : corners) {
                        points.add(new Point(corner.x, corner.y));
                }
                return points;

        }

        private double gaussian(double x, double y, double sigma2) {
                double t = (x * x + y * y) / (2 * sigma2);
                double u = 1.0 / (2 * Math.PI * sigma2);
                double e = u * Math.exp(-t);
                return e;
        }
        //gradiente 3x3
        private double[] sobel(int x, int y) {
                int v00 = 0, v01 = 0, v02 = 0, v10 = 0, v12 = 0, v20 = 0, v21 = 0, v22 = 0;

                int x0 = x - 1, x1 = x, x2 = x + 1;
                int y0 = y - 1, y1 = y, y2 = y + 1;
                if (x0 < 0)
                        x0 = 0;
                if (y0 < 0)
                        y0 = 0;
                if (x2 >= width)
                        x2 = width - 1;
                if (y2 >= height)
                        y2 = height - 1;

                v00 = (int) getForcedPixel(x0, y0);
                v10 = (int) getForcedPixel(x1, y0);
                v20 = (int) getForcedPixel(x2, y0);
                v01 = (int) getForcedPixel(x0, y1);
                v21 = (int) getForcedPixel(x2, y1);
                v02 = (int) getForcedPixel(x0, y2);
                v12 = (int) getForcedPixel(x1, y2);
                v22 = (int) getForcedPixel(x2, y2);

                double sx = (v20 + 2 * v21 + v22) - (v00 + 2 * v01 + v02);
                double sy = (v02 + 2 * v12 + v22) - (v00 + 2 * v10 + v20);
                return new double[] { sx / 4, sy / 4 };
        }

        private void computeDerivatives(int radius, double sigma, double[][] Lx2,
                        double[][] Ly2, double[][] Lxy) {

                // gradient values: Gx,Gy
                double[][][] grad = new double[width][height][];
                for (int y = 0; y < this.height; y++)
                        for (int x = 0; x < this.width; x++)
                                grad[x][y] = sobel(x, y);

                // precompute the coefficients of the gaussian filter
                double[][] filter = new double[2 * radius + 1][2 * radius + 1];
                double filtersum = 0;
                for (int j = -radius; j <= radius; j++) {
                        for (int i = -radius; i <= radius; i++) {
                                double g = gaussian(i, j, sigma);
                                filter[i + radius][j + radius] = g;
                                filtersum += g;
                        }
                }

                // Convolve gradient with gaussian filter:
                //
                // Ix2 = (F) * (Gx^2)
                // Iy2 = (F) * (Gy^2)
                // Ixy = (F) * (Gx.Gy)
                //
                for (int y = 0; y < this.height; y++) {
                        for (int x = 0; x < this.width; x++) {

                                for (int dy = -radius; dy <= radius; dy++) {
                                        for (int dx = -radius; dx <= radius; dx++) {
                                                int xk = x + dx;
                                                int yk = y + dy;
                                                if (xk < 0 || xk >= this.width)
                                                        continue;
                                                if (yk < 0 || yk >= this.height)
                                                        continue;

                                                // filter weight
                                                double f = filter[dx + radius][dy + radius];

                                                // convolution
                                                Lx2[x][y] += f * grad[xk][yk][0] * grad[xk][yk][0];
                                                Ly2[x][y] += f * grad[xk][yk][1] * grad[xk][yk][1];
                                                Lxy[x][y] += f * grad[xk][yk][0] * grad[xk][yk][1];
                                        }
                                }
                                Lx2[x][y] /= filtersum;
                                Ly2[x][y] /= filtersum;
                                Lxy[x][y] /= filtersum;
                        }
                }
        }

        private double harrisMeasure(int x, int y, double k, double[][] Lx2,
                        double[][] Ly2, double[][] Lxy) {
                // matrix elements (normalized)
                double m00 = Lx2[x][y];
                double m01 = Lxy[x][y];
                double m10 = Lxy[x][y];
                double m11 = Ly2[x][y];

                // Harris corner measure = det(M)-lambda.trace(M)^2

                return m00 * m11 - m01 * m10 - k * (m00 + m11) * (m00 + m11);
        }

        private boolean isSpatialMaxima(double[][] hmap, int x, int y) {
                int n = 8;
                int[] dx = new int[] { -1, 0, 1, 1, 1, 0, -1, -1 };
                int[] dy = new int[] { -1, -1, -1, 0, 1, 1, 1, 0 };
                double w = hmap[x][y];
                for (int i = 0; i < n; i++) {
                        double wk = hmap[x + dx[i]][y + dy[i]];
                        if (wk >= w)
                                return false;
                }
                return true;
        }

        private double[][] computeHarrisMap(double k, double[][] Lx2,
                        double[][] Ly2, double[][] Lxy) {

                // Harris measure map
                double[][] harrismap = new double[width][height];
                double max = 0;

                // for each pixel in the image
                for (int y = 0; y < this.height; y++) {
                        for (int x = 0; x < this.width; x++) {
                                // compute ans store the harris measure
                                harrismap[x][y] = harrisMeasure(x, y, k, Lx2, Ly2, Lxy);
                                if (harrismap[x][y] > max)
                                        max = harrismap[x][y];
                        }
                }

                // rescale measures in 0-100
                for (int y = 0; y < this.height; y++) {
                        for (int x = 0; x < this.width; x++) {
                                double h = harrismap[x][y];
                                if (h < 0)
                                        h = 0;
                                else
                                        h = 100 * Math.log(1 + h) / Math.log(1 + max);
                                harrismap[x][y] = h;
                        }
                }

                return harrismap;
        }




        public double getForcedPixel(int x, int y) {
                if (!isValid(x, y)) {
                        return 0;
                }

                return values[y * this.getWidth() + x];
        }

}
