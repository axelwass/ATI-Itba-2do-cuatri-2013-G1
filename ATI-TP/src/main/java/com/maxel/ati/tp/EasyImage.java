/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

/**
 *
 * @author maximo
 */
public class EasyImage {

    BufferedImage img;
    int[] channelR;
    int[] channelG;
    int[] channelB;
//    int[] channelA;
    int[] fullImg;
    int width;
    int height;
    ColorModel cm;

    public static EasyImage newSquare(int width, int height) {
        EasyImage image = new EasyImage(width, height);
        Color blackColor = Color.BLACK;
        Color whiteColor = Color.WHITE;

        // Generates a black square with a smaller white square inside
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Analyzes if the point is in the black or white square
                boolean fitsInSquareByWidth = (x > width / 4 && x < 3 * (width / 4));
                boolean fitsInSquareByHeight = (y > height / 4 && y < 3 * (height / 4));
                boolean fitsInSquare = (fitsInSquareByWidth && fitsInSquareByHeight);
                Color colorToApply = fitsInSquare ? whiteColor : blackColor;
                image.setRGB(x, y, colorToApply.getRGB());
            }
        }
        image.updateFullImg();
        return image;
    }

    public static EasyImage newGauss(int width, int height, double mean, double std) {
        EasyImage image = new EasyImage(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int aux = ((int) NoiseGenerator.gaussian(mean, std) & 0x000000FF) << 16;
                aux += ((int) NoiseGenerator.gaussian(mean, std) & 0x000000FF) << 8;
                aux += (int) NoiseGenerator.gaussian(mean, std) & 0x000000FF;
                image.setRGB(x, y, aux);
            }
        }
        image.updateFullImg();
        return image;
    }

    public static EasyImage newRayleigh(int width, int height, double eps) {
        EasyImage image = new EasyImage(width, height);


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int aux = ((int) NoiseGenerator.rayleigh(eps) & 0x000000FF) << 16;
                aux += ((int) NoiseGenerator.rayleigh(eps) & 0x000000FF) << 8;
                aux += (int) NoiseGenerator.rayleigh(eps) & 0x000000FF;
                image.setRGB(x, y, aux);
            }
        }
        image.updateFullImg();
        return image;
    }

    public static EasyImage newExponential(int width, int height, double lambda) {
        EasyImage image = new EasyImage(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
               // System.out.println((int)NoiseGenerator.exponential(lambda));
                int aux = ((int) NoiseGenerator.exponential(lambda) & 0x000000FF) << 16;
                aux += ((int) NoiseGenerator.exponential(lambda) & 0x000000FF) << 8;
                aux += (int) NoiseGenerator.exponential(lambda) & 0x000000FF;
                image.setRGB(x, y, aux);
            }
        }
        image.updateFullImg();
        return image;
    }

    public static EasyImage newUniform(int width, int height, double from, double to) {
        EasyImage image = new EasyImage(width, height);


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int aux = ((int) NoiseGenerator.uniform(from, to) & 0x000000FF) << 16;
                aux += ((int) NoiseGenerator.uniform(from, to) & 0x000000FF) << 8;
                aux += (int) NoiseGenerator.uniform(from, to) & 0x000000FF;
                image.setRGB(x, y, aux);
            }
        }
        image.updateFullImg();
        return image;
    }

    public static EasyImage newCircle(int width, int height) {
        EasyImage image = new EasyImage(width, height);

        Color blackColor = Color.BLACK;
        Color whiteColor = Color.WHITE;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double aTerm = Math.pow(x - width / 2, 2);
                double bTerm = Math.pow(y - height / 2, 2);
                double rTerm = Math.pow(40, 2);
                boolean fitsInCircle = (aTerm + bTerm) <= rTerm;
                Color colorToApply = fitsInCircle ? whiteColor : blackColor;
                image.setRGB(x, y, colorToApply.getRGB());
            }
        }
        image.updateFullImg();
        return image;
    }

    public static EasyImage newColorDegradee(int width, int height, int color1, int color2) {
        return newDegradee(width, height, color1, color2, true);
    }

    private static EasyImage newDegradee(int width, int height, int color1, int color2, boolean isColor) {
        EasyImage degrade = new EasyImage(width, height);

//		if (isColor) {
//			degrade = new ColorImage(height, width, Image.ImageFormat.BMP,
//					Image.ImageType.RGB);
//		} else {
//			degrade = new ColorImage(height, width, Image.ImageFormat.BMP,
//					Image.ImageType.GRAYSCALE);
//		}

        Color c1 = new Color(color1);
        Color cAux = new Color(color1);
        Color c2 = new Color(color2);

        float redFactor = (float) (c2.getRed() - c1.getRed()) / height;
        float greenFactor = (float) (c2.getGreen() - c1.getGreen()) / height;
        float blueFactor = (float) (c2.getBlue() - c1.getBlue()) / height;

        float red = 0;
        float green = 0;
        float blue = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                degrade.setRGB(x, y, c1.getRGB());
            }
            red = red + redFactor;
            green = green + greenFactor;
            blue = blue + blueFactor;

            c1 = new Color(cAux.getRGB() + (int) ((int) red * 0x010000)
                    + (int) ((int) green * 0x000100)
                    + (int) ((int) blue * 0x000001));
        }

        return degrade;
    }

    public EasyImage(BufferedImage img) {
        this.img = img;
        cm = img.getColorModel();
        this.width = img.getWidth();
        this.height = img.getHeight();
        fullImg = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        channelR = new int[fullImg.length];
        channelG = new int[fullImg.length];
        channelB = new int[fullImg.length];
//        channelA = new int[fullImg.length];
        updateChannels();
    }

    EasyImage(int width, int height) {
        this.width = width;
        this.height = height;
        fullImg = new int[width * height];
        channelR = new int[fullImg.length];
        channelG = new int[fullImg.length];
        channelB = new int[fullImg.length];
//        channelA = new int[fullImg.length];

    }

    public EasyImage getSubImage(int startX, int startY, int endX, int endY) {
        EasyImage newImg = new EasyImage(endX - startX, endY - startY);
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                newImg.setRGB(x - startX, y - startY, getRGB(x, y));
            }
        }
        return newImg;
    }

    public BufferedImage getBufferedImage() {

        if (img == null) {
            img = new BufferedImage(width, height, 1);
        }
        updateImg();
        return img;
    }

    public void updateImg() {
        img.setRGB(0, 0, width, height, fullImg, 0, width);
    }

    private void updateChannels() {
        for (int i = 0; i < fullImg.length; i++) {
            int rgb = fullImg[i];
//            channelA[i] = (rgb >> 24) & 0x000000FF;
            channelR[i] = (rgb >> 16) & 0x000000FF;
            channelG[i] = (rgb >> 8) & 0x000000FF;
            channelB[i] = (rgb) & 0x000000FF;
        }
    }

    private void updateFullImg() {
        for (int i = 0; i < fullImg.length; i++) {
//            fullImg[i] = channelA[i] << 24;
            fullImg[i] = (channelR[i] & 0x000000FF) << 16;
            fullImg[i] += (channelG[i] & 0x000000FF) << 8;
            fullImg[i] += channelB[i] & 0x000000FF;
        }
    }

    public void applyNegative() {
        for (int i = 0; i < width * height; i++) {
            fullImg[i] = 0x00ffffff - fullImg[i];
        }
        //                    for (int x = 0; x < width; x++) {
//                        for (int y = 0; y < height; y++) {
//                            int aux = img.getRGB(x, y);
//                            aux = 0x0fff - aux;
//                            img.setRGB(x, y, aux);
//                        }
//                    }
    }

    private void normalizeChannel(int[] channel) {
        Integer max = channel[0];
        Integer min = channel[0];
//        for (int x : channelA) {
//            max = x > max ? x : max;
//            min = x < min ? x : min;
//        }
        for (int x : channelR) {
            max = x > max ? x : max;
            min = x < min ? x : min;
        }
        for (int x : channelG) {
            max = x > max ? x : max;
            min = x < min ? x : min;
        }
        for (int x : channelB) {
            max = x > max ? x : max;
            min = x < min ? x : min;
        }
        max = max - min;
        for (int i = 0; i < channel.length; i++) {
            channel[i] = ((channel[i] - min) * 255) / max;
        }
    }

    public void dynamicRangeCompress() {
        dynamicRangeCompression(channelR);
        dynamicRangeCompression(channelG);
        dynamicRangeCompression(channelB);
        updateFullImg();
    }

    private void contrastChannel(int r1, int r2, double y1, double y2, int[] channel) {
        for (int i = 0; i < width * height; i++) {
            double r = channel[i];
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
            double f = m * r + b;
            channel[i] = (int) f;
        }

    }

    public void applyContrast(int p1, int p2, double y1, double y2) {
        contrastChannel(p1, p2, y1, y2, channelR);
        contrastChannel(p1, p2, y1, y2, channelG);
        contrastChannel(p1, p2, y1, y2, channelB);
        updateFullImg();
    }

    private void dynamicRangeCompression(int[] channel) {
        int max = 0;
        for (int x : channel) {
            max = max > x ? max : x;
        }
        int L = 255;
        double c = (L - 1) / Math.log(1 + max);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                channel[x + y * width] = (int) (c * Math.log(1 + channel[x + y * width]));
            }
        }
    }

    public boolean isValid(int x, int y) {
        return (x >= 0 && y >= 0 && x < width && y < width);
    }

    public void multiply(int factor) {
        for (int i = 0; i < channelR.length; i++) {
            channelR[i] *= factor;
            channelG[i] *= factor;
            channelB[i] *= factor;
        }
        updateFullImg();
    }

    public void normalize() {
//        normalizeChannel(channelA);
        normalizeChannel(channelR);
        normalizeChannel(channelG);
        normalizeChannel(channelB);
        updateFullImg();
    }

    public void substract(EasyImage img2) {
        for (int i = 0; i < fullImg.length; i++) {
            channelR[i] -= img2.channelR[i];
            channelG[i] -= img2.channelG[i];
            channelB[i] -= img2.channelB[i];
        }
        updateFullImg();
        if (!isAppropiate()) {
            normalize();
        }
    }

    public void add(EasyImage img2) {
        for (int i = 0; i < fullImg.length; i++) {
            channelR[i] += img2.channelR[i];
            channelG[i] += img2.channelG[i];
            channelB[i] += img2.channelB[i];
        }
        updateFullImg();
        if (!isAppropiate()) {
            normalize();
        }
    }

    public void setRGB(int x, int y, int rgb) {
        int i = y * width + x;
        fullImg[i] = rgb;
//        channelA[i] = (rgb >> 24) & 0x000000FF;
        channelR[i] = (rgb >> 16) & 0x000000FF;
        channelG[i] = (rgb >> 8) & 0x000000FF;
        channelB[i] = (rgb) & 0x000000FF;

    }

    public int getRGB(int x, int y) {
        int i = y * width + x;
        return fullImg[i];

    }

    public boolean isAppropiate() {
        return isAppropiate(channelR) && isAppropiate(channelG) && isAppropiate(channelB);
    }

    private boolean isAppropiate(int[] channel) {
        for (int i = 0; i < channel.length; i++) {
            if (channel[i] < 0 || channel[i] > 255) {
                return false;
            }
        }
        return true;
    }

    private double getGreyLevel(int x, int y) {
        int i = y * width + x;
        double red = channelR[i];
        double green = channelG[i];
        double blue = channelB[i];

        return (red + green + blue) / 3.0;
    }

    public double[] getHistogramPixels() {
        double[] result = new double[height * width];

        for (int i = 0; i < result.length; i++) {
            result[i] = getGreyLevel(
                    (int) Math.floor(i / height), i % width);
        }

        return result;
    }

    public static BufferedImage generateHistogram(EasyImage image) {

        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);

        double[] histData = image.getHistogramPixels();

        dataset.addSeries("Histogram", histData, histData.length);

        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean tooltips = false;
        boolean urls = false;

        JFreeChart chart = ChartFactory.createHistogram("Histograma",
                "Nivel de gris", "Apariciones", dataset, orientation, show,
                tooltips, urls);
        return chart.createBufferedImage(400, 200);
    }

    public void umbral(int value) {
        int black = 0;
        int white = 255;
        for (int i = 0; i < width * height; i++) {
            channelR[i] = channelR[i] > value ? white : black;
            channelG[i] = channelG[i] > value ? white : black;
            channelB[i] = channelB[i] > value ? white : black;
        }
        updateFullImg();
    }

    public void toGrey() {
        for (int i = 0; i < width * height; i++) {
            channelR[i] = (channelR[i] + channelG[i] + channelB[i]) / 3;
        }
        channelG = channelR;
        channelB = channelR;
        updateFullImg();
    }

    public void equalize() {
        equalizeChannel(channelR);
        equalizeChannel(channelG);
        equalizeChannel(channelB);
        updateFullImg();
    }

    private void equalizeChannel(int[] channel) {
        int[] ocurrences = getOccurrences(channel);
        double[] newChannel = new double[channel.length];
        double s_min = 255;
        double s_max = 0;

        for (int i = 0; i < newChannel.length; i++) {
            int grayLevel = channel[i];

            double outGrayLevel = 0;
            for (int k = 0; k < grayLevel; k++) {
                outGrayLevel += ocurrences[k];
            }

            newChannel[i] = outGrayLevel / channel.length;
            s_min = Math.min(s_min, newChannel[i]);
            s_max = Math.max(s_max, newChannel[i]);

        }
        for (int i = 0; i < newChannel.length; i++) {
            double aux = 255 * (newChannel[i] - s_min) / (s_max - s_min);
            channel[i] = (int) Math.ceil(aux);
        }
    }

    public int[] getOccurrences(int[] channel) {
        int[] occu = new int[256];
        for (int i = 0; i < channel.length; i++) {
            int c = channel[i];
            //check for ouverflow
            c = c > 255 ? 255 : (c < 0 ? 0 : c);
            occu[c]++;
        }

        return occu;
    }

    public void multiplyNoise(EasyImage ei) {
        Random aux = new Random(System.currentTimeMillis());
        for (int i = 0; i < width * height; i++) {
            channelR[i] = (int) (((double) channelR[i]) * (1.0 + (aux.nextBoolean() ? 1 : -1) * ((double) ei.channelR[i]) / 255.0));
            channelG[i] = (int) (((double) channelG[i]) * (1.0 + (aux.nextBoolean() ? 1 : -1) * ((double) ei.channelG[i]) / 255.0));
            channelB[i] = (int) (((double) channelB[i]) * (1.0 + (aux.nextBoolean() ? 1 : -1) * ((double) ei.channelB[i]) / 255.0));
        }
        if (!isAppropiate()) {
            normalize();
        }
        updateFullImg();
    }

    public void addNoise(EasyImage ei) {
        Random aux = new Random(System.currentTimeMillis());
        for (int i = 0; i < width * height; i++) {
            channelR[i] = (int) (((double) channelR[i]) + (aux.nextBoolean() ? 1 : 1) * ((double) ei.channelR[i]));
            channelG[i] = (int) (((double) channelG[i]) + (aux.nextBoolean() ? 1 : 1) * ((double) ei.channelG[i]));
            channelB[i] = (int) (((double) channelB[i]) + (aux.nextBoolean() ? 1 : 1) * ((double) ei.channelB[i]));
        }
        if (!isAppropiate()) {
            normalize();
        }
        updateFullImg();
    }

    public void addSaltNPepperColor(double p, double pSalt) {
        Random aux = new Random(System.currentTimeMillis());
        for (int i = 0; i < width * height; i++) {
            channelR[i] = aux.nextDouble() < p ? (aux.nextDouble() < pSalt ? 255 : 0) : channelR[i];
            channelG[i] = aux.nextDouble() < p ? (aux.nextDouble() < pSalt ? 255 : 0) : channelG[i];
            channelB[i] = aux.nextDouble() < p ? (aux.nextDouble() < pSalt ? 255 : 0) : channelB[i];
        }
        updateFullImg();
    }

    public void addSaltNPepper(double p, double pSalt) {
        Random aux = new Random(System.currentTimeMillis());
        for (int i = 0; i < width * height; i++) {
            int bw = (aux.nextDouble() < pSalt ? 255 : 0);
            if (aux.nextDouble() < p) {
                channelR[i] = bw;
                channelG[i] = bw;
                channelB[i] = bw;
            }
        }
        updateFullImg();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int applyMaskToPixelInChannel(int x1, int y1, Mask m, int[] channel) {
        double aux = 0;
        for (int x = -m.width / 2; x < m.width / 2 + m.width % 2; x++) {
            for (int y = -m.height / 2; y < m.height / 2 + m.height % 2; y++) {
                if (isValid(x + x1, y + y1)) {
                    aux += m.getValue(x, y) * channel[(x1 + x) + (y1 + y) * width];
                } else if (isValid(x1 + x, y1 - y)) {
                    aux += m.getValue(x, y) * channel[(x1 + x) + (y1 - y) * width];
                } else if (isValid(x1 - x, y1 + y)) {
                    aux += m.getValue(x, y) * channel[(x1 - x) + (y1 + y) * width];
                } else if (isValid(x1 - x, y1 - y)) {
                    aux += m.getValue(x, y) * channel[(x1 - x) + (y1 - y) * width];
                }
            }
        }
        return (int) aux;
    }

    public void applyMask(Mask m) {
        int i;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                i = y * width + x;
                channelR[i] = applyMaskToPixelInChannel(x, y, m, channelR);
                channelG[i] = applyMaskToPixelInChannel(x, y, m, channelG);
                channelB[i] = applyMaskToPixelInChannel(x, y, m, channelB);
            }
        }
        if (!isAppropiate()) {
            normalize();
        }
        updateFullImg();
    }

    public void applyMedianMask(int height, int width) {
        int i;
        int[] newChannelR = new int[this.height * this.width];
        int[] newChannelG = new int[this.height * this.width];
        int[] newChannelB = new int[this.height * this.width];
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                i = y * getWidth() + x;
                newChannelR[i] = applyMedianMaskToChannelPixel(x, y, height, width, channelR);
                newChannelG[i] = applyMedianMaskToChannelPixel(x, y, height, width, channelG);
                newChannelB[i] = applyMedianMaskToChannelPixel(x, y, height, width, channelB);
            }
        }
        channelR = newChannelR;
        channelG = newChannelG;
        channelB = newChannelB;
        updateFullImg();
    }

    public int applyMedianMaskToChannelPixel(int x1, int y1, int height, int width, int[] channel) {
        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int x = -width / 2; x < width / 2 + width % 2; x++) {
            for (int y = -height / 2; y < height / 2 + height % 2; y++) {
                if (isValid(x + x1, y + y1)) {
                    list.add(channel[(x1 + x) + (y1 + y) * getWidth()]);
                } else if (isValid(x1 - x, y1 + y)) {
                    list.add(channel[(x1 - x) + (y1 + y) * getWidth()]);
                } else if (isValid(x1 + x, y1 - y)) {
                    list.add(channel[(x1 + x) + (y1 - y) * getWidth()]);
                } else if (isValid(x1 - x, y1 - y)) {
                    list.add(channel[(x1 - x) + (y1 - y) * getWidth()]);
                }
            }
        }
        Collections.sort(list);
        return list.get(height * width / 2 + height * width % 2);
    }
}
