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

    public static Mask newMeanFilter(int width, int height) {
        Mask m = new Mask(width, height);
        for (int x = -width / 2; x < width / 2 + width % 2; x++) {
            for (int y = -height / 2; y < height / 2 + height % 2; y++) {
                m.setValue(x, y, 1.0 / (double) (width * height));
            }
        }
        return m;
    }

    public static Mask newHighAmpFilter(int width, int height) {
        Mask m = new Mask(width, height);
        for (int x = -width / 2; x < width / 2 + width % 2; x++) {
            for (int y = -height / 2; y < height / 2 + height % 2; y++) {
                if (x == 0 && y == 0) {
                    m.setValue(x, y, (width * height - 1.0) / (double) (width * height));
                } else {
                    m.setValue(x, y, -1.0 / (double) (width * height));
                }
            }
        }
        return m;
    }

    public static Mask newGaussianMask(int size, double sigma) {
        if (size % 2 == 0) {
            size++;
        }
        Mask mask = new Mask(size);
        double total = 0;
        for (int i = -mask.getWidth() / 2; i <= mask.getWidth() / 2; i++) {
            for (int j = -mask.getHeight() / 2; j <= mask.getHeight() / 2; j++) {
                double gaussianFunction = (1 / (2 * Math.PI * Math
                        .pow(sigma, 2)))
                        * Math.exp(-((Math.pow(i, 2) + Math.pow(j, 2))
                        / (Math.pow(sigma, 2))));
                total += gaussianFunction;
                mask.setValue(i, j, gaussianFunction);
            }
        }
        for (int i = -mask.getWidth() / 2; i <= mask.getWidth() / 2; i++) {
            for (int j = -mask.getHeight() / 2; j <= mask.getHeight() / 2; j++) {
                double oldPixel = mask.getValue(i, j);
                mask.setValue(i, j, oldPixel / total);
            }
        }
        return mask;
    }

    public static Mask newDxRoberts() {
        Mask m = new Mask(2);
        m.setValue(-1, -1, 1);
        m.setValue(-1, 0, 0);
        m.setValue(0, -1, 0);
        m.setValue(0, 0, -1);
        return m;
    }

    public static Mask newDyRoberts() {
        Mask m = new Mask(2);
        m.setValue(-1, -1, 0);
        m.setValue(-1, 0, -1);
        m.setValue(0, -1, 1);
        m.setValue(0, 0, 0);
        return m;
    }

    public static Mask newDxPrewitt(int size) {
        Mask m = new Mask(size);
        for (int i = -size / 2; i < size / 2 + size % 2; i++) {
            m.setValue(-size / 2, i, -1);
            m.setValue(size / 2, i, 1);
        }
        System.out.println("dx");
        System.out.println(m);
        return m;
    }

    public static Mask newDyPrewitt(int size) {
        Mask m = new Mask(size);
        for (int i = -size / 2; i < size / 2 + size % 2; i++) {
            m.setValue(i, -size / 2, -1);
            m.setValue(i, size / 2, 1);
        }
        System.out.println("dy");
        System.out.println(m);
        return m;
    }

    public static Mask newDxSobel() {
        Mask m = new Mask(3);
        m.setValue(-1, -1, -1);
        m.setValue(0, -1, -2);
        m.setValue(1, -1, -1);
        m.setValue(-1, 1, 1);
        m.setValue(0, 1, 2);
        m.setValue(1, 1, 1);
        return m;
    }

    public static Mask newDySobel() {
        Mask m = new Mask(3);
        m.setValue(-1, -1, -1);
        m.setValue(-1, 0, -2);
        m.setValue(-1, 1, -1);
        m.setValue(1, -1, 1);
        m.setValue(1, 0, 2);
        m.setValue(1, 1, 1);
        return m;
    }

    public static Mask newDxSevenA() {
        Mask m = new Mask(3);
        m.setValue(-1, -1, 1);
        m.setValue(0, -1, 1);
        m.setValue(1, -1, 1);
        m.setValue(-1, 0, 1);
        m.setValue(0, 0, -2);
        m.setValue(1, 0, 1);
        m.setValue(-1, 1, -1);
        m.setValue(0, 1, -1);
        m.setValue(1, 1, -1);
        return m;
    }

    public static Mask newDySevenA() {
        Mask m = newDxSevenA();
        m.rotate90();
        return m;
    }

    public static Mask newDxKirsh() {
        Mask m = new Mask(3);
        m.setValue(-1, -1, 5);
        m.setValue(0, -1, 5);
        m.setValue(1, -1, 5);
        m.setValue(-1, 0, -3);
        m.setValue(0, 0, 0);
        m.setValue(1, 0, -3);
        m.setValue(-1, 1, -3);
        m.setValue(0, 1, -3);
        m.setValue(1, 1, -3);
        return m;
    }

    public static Mask newDyKirsh() {
        Mask m = newDxSevenA();
        m.rotate90();
        return m;
    }

    public static Mask newDxSevenC() {
        Mask m = new Mask(3);
        m.setValue(-1, -1, 1);
        m.setValue(0, -1, 1);
        m.setValue(1, -1, 1);
        m.setValue(-1, 0, 0);
        m.setValue(0, 0, 0);
        m.setValue(1, 0, 0);
        m.setValue(-1, 1, -1);
        m.setValue(0, 1, -1);
        m.setValue(1, 1, -1);
        return m;
    }

    public static Mask newDySevenC() {
        Mask m = newDxSevenA();
        m.rotate90();
        return m;
    }

    public static Mask newDxSevenD() {
        Mask m = new Mask(3);
        m.setValue(-1, -1, 1);
        m.setValue(0, -1, 2);
        m.setValue(1, -1, 1);
        m.setValue(-1, 0, 0);
        m.setValue(0, 0, 0);
        m.setValue(1, 0, 0);
        m.setValue(-1, 1, -1);
        m.setValue(0, 1, -2);
        m.setValue(1, 1, -1);
        return m;
    }

    public static Mask newDySevenD() {
        Mask m = newDxSevenA();
        m.rotate90();
        return m;
    }

    public static Mask newLaplace() {
        Mask m = new Mask(3);
        m.setValue(-1, -1, 0);
        m.setValue(0, -1, -1);
        m.setValue(1, -1, 0);
        m.setValue(-1, 0, -1);
        m.setValue(0, 0, 4);
        m.setValue(1, 0, -1);
        m.setValue(-1, 1, 0);
        m.setValue(0, 1, -1);
        m.setValue(1, 1, 0);
        return m;
    }

    public static Mask newLaplaceGaussianMask(int radius, double sigma) {

        if (radius % 2 == 0) {
            radius++;
        }
        final int sideLength = 2 * radius + 1;

        final double[][] mask = new double[sideLength][sideLength];

        final double sigmaSquared = Math.pow(sigma, 2);

        final double sigmaCubed = Math.pow(sigma, 3);



        for (int i = -radius; i <= radius; i++) {

            for (int j = -radius; j <= radius; j++) {



                final double iSquared = Math.pow(i, 2);

                final double jSquared = Math.pow(j, 2);



                final double factor = Math.pow(Math.sqrt(2 * Math.PI) * sigmaCubed, -1);

                final double exp = -(iSquared + jSquared) / (2 * sigmaSquared);

                final double term = 2 - (iSquared + jSquared) / sigmaSquared;

                final double pixelValue = -1 * factor * term * Math.pow(Math.E, exp);

                mask[i + radius][j + radius] = pixelValue;

            }

        }
        Mask m = new Mask(radius);
        m.values = mask;

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getValue(int x, int y) {
        x = x + width / 2;
        y = y + height / 2;
        return values[x][y];
    }

    public boolean isValid(int x, int y) {
        return (x + width / 2 >= 0) && (x < width / 2 + width % 2) && (y + height / 2 >= 0) && (y < height / 2 + height % 2);
    }

    public void setValue(int x, int y, double value) {
        if (isValid(x, y)) {
            x = x + width / 2;
            y = y + height / 2;
            values[x][y] = value;
        } else {
            System.err.println("x: " + x + "y: " + y);
        }
    }

    public boolean isSquared() {
        return width == height;
    }

    public void rotate45() {

        if (isSquared() && width == 3) {
            Mask m = new Mask(width, height);
            m.setValue(-1, -1, getValue(-1, 0));
            m.setValue(-1, 0, getValue(-1, 1));
            m.setValue(-1, 1, getValue(0, 1));

            m.setValue(0, -1, getValue(-1, -1));
            m.setValue(0, 0, getValue(0, 0));
            m.setValue(0, 1, getValue(1, 1));

            m.setValue(1, -1, getValue(0, -1));
            m.setValue(1, 0, getValue(1, -1));
            m.setValue(1, 1, getValue(1, 0));
            values = m.values;
        }
        /*
         result.setPixel(-1, -1, mask.getValue(-1, 0));
         result.setPixel(-1, 0, mask.getValue(-1, 1));
         result.setPixel(-1, 1, mask.getValue(0, 1));

         result.setPixel(0, -1, mask.getValue(-1, -1));
         result.setPixel(0, 0, mask.getValue(0, 0));
         result.setPixel(0, 1, mask.getValue(1, 1));

         result.setPixel(1, -1, mask.getValue(0, -1));
         result.setPixel(1, 0, mask.getValue(1, -1));
         result.setPixel(1, 1, mask.getValue(1, 0));*/
    }

    public void rotate90() {
        rotate45();
        rotate45();
    }

    public void rotate135() {
        rotate90();
        rotate45();
    }

    @Override
    public String toString() {
        String a = "";
        for (int i = 0; i < width * height; i++) {
            if (i % width == 0) {
                a += "\n";
            }
            a += values[i % width][i / width];
        }
        return a; //To change body of generated methods, choose Tools | Templates.
    }
}
