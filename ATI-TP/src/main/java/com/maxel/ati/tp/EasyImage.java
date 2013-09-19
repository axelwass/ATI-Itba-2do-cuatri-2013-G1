/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
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
    Channel R;
    Channel G;
    Channel B;
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
        Channel noisyChannel = new Channel(image.getWidth(), image.getHeight());
        for (int x = 0; x < noisyChannel.getWidth(); x++) {
            for (int y = 0; y < noisyChannel.getHeight(); y++) {
                double noiseLevel = NoiseGenerator.gaussian(mean, std);
                noisyChannel.setValue(x, y, noiseLevel);
            }
        }
        image.R = noisyChannel;
        image.G = noisyChannel.clone();
        image.B = noisyChannel.clone();
        image.updateFullImg();
        return image;
    }

    public static EasyImage newRayleigh(int width, int height, double eps) {
        EasyImage image = new EasyImage(width, height);
        Channel noisyChannel = new Channel(width, height);
        for (int x = 0; x < noisyChannel.getWidth(); x++) {
            for (int y = 0; y < noisyChannel.getHeight(); y++) {
                double noiseLevel = NoiseGenerator.rayleigh(eps);
                noisyChannel.setValue(x, y, noiseLevel);
            }
        }
          image.R = noisyChannel;
        image.G = noisyChannel.clone();
        image.B = noisyChannel.clone();
        image.updateFullImg();
        return image;
    }

    public static EasyImage newExponential(int width, int height, double lambda) {
        EasyImage image = new EasyImage(width, height);
        Channel noisyChannel = new Channel(image.getWidth(), image.getHeight());
		for (int x = 0; x < noisyChannel.getWidth(); x++) {
			for (int y = 0; y < noisyChannel.getHeight(); y++) {
				double noiseLevel = NoiseGenerator.exponential(lambda);
				noisyChannel.setValue(x, y, noiseLevel);
			}
		}
          image.R = noisyChannel;
        image.G = noisyChannel.clone();
        image.B = noisyChannel.clone();
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
        R = new Channel(width, height);
        G = new Channel(width, height);
        B = new Channel(width, height);
        updateChannels();
    }

    EasyImage(int width, int height) {
        this.width = width;
        this.height = height;
        fullImg = new int[width * height];
        R = new Channel(width, height);
        G = new Channel(width, height);
        B = new Channel(width, height);
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
            R.setValue(i, (rgb >> 16) & 0x000000FF);
            G.setValue(i, (rgb >> 8) & 0x000000FF);
            B.setValue(i, (rgb) & 0x000000FF);
//            channelR[i] = (rgb >> 16) & 0x000000FF;
//            channelG[i] = (rgb >> 8) & 0x000000FF;
//            channelB[i] = (rgb) & 0x000000FF;
        }
    }

    public void updateFullImg() {
        for (int i = 0; i < fullImg.length; i++) {
//            fullImg[i] = channelA[i] << 24;
            fullImg[i] = ((int) R.getValue(i) & 0x000000FF) << 16;
            fullImg[i] += ((int) G.getValue(i) & 0x000000FF) << 8;
            fullImg[i] += (int) B.getValue(i) & 0x000000FF;
        }
    }

    public void applyNegative() {
        for (int i = 0; i < width * height; i++) {
            fullImg[i] = 0x00ffffff - fullImg[i];
        }
    }

    public void dynamicRangeCompress() {
        R.dynamicRangeCompression();
        G.dynamicRangeCompression();
        B.dynamicRangeCompression();
        updateFullImg();
    }

    public void applyContrast(int p1, int p2, double y1, double y2) {
        R.contrast(p1, p2, y1, y2);
        G.contrast(p1, p2, y1, y2);
        B.contrast(p1, p2, y1, y2);
        updateFullImg();
    }

    public boolean isValid(int x, int y) {
        return (x >= 0 && y >= 0 && x < width && y < width);
    }

    public void multiply(int factor) {
        R.multiply(factor);
        G.multiply(factor);
        B.multiply(factor);
        updateFullImg();
    }

    public double getMax() {
        double maxR = R.getMax();
        double maxG = G.getMax();
        double maxB = B.getMax();
        return maxR > maxG ? (maxR > maxB ? maxR : maxB) : (maxG > maxB ? maxG : maxB);
    }

    public double getMin() {
        double minR = R.getMin();
        double minG = G.getMin();
        double minB = B.getMin();
        return minR < minG ? (minR < minB ? minR : minB) : (minG < minB ? minG : minB);
    }

    public void normalize() {
//        normalizeChannel(channelA);
        double min = getMin();
        double max = getMax();
        R.normalize(max, min);
        G.normalize(max, min);
        B.normalize(max, min);
        updateFullImg();
    }

    public void substract(EasyImage img2) {
        for (int i = 0; i < fullImg.length; i++) {
            R.substract(i, img2.R.getValue(i));
            G.substract(i, img2.G.getValue(i));
            B.substract(i, img2.B.getValue(i));
        }
        updateFullImg();
        if (!isAppropiate()) {
            normalize();
        }
    }

    public void add(EasyImage img2) {
        for (int i = 0; i < fullImg.length; i++) {
            R.add(i, img2.R.getValue(i));
            G.add(i, img2.G.getValue(i));
            B.add(i, img2.B.getValue(i));
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
        R.setValue(i, (rgb >> 16) & 0x000000FF);
        G.setValue(i, (rgb >> 8) & 0x000000FF);
        B.setValue(i, (rgb) & 0x000000FF);
    }

    public int getRGB(int x, int y) {
        int i = y * width + x;
        return fullImg[i];
    }

    public boolean isAppropiate() {
        return R.isAppropiate() && G.isAppropiate() && B.isAppropiate();
    }

    private double getGreyLevel(int x, int y) {
        int i = y * width + x;
        return getGreyLevel(i);
    }

    private double getGreyLevel(int i) {
        double red = R.getValue(i);
        double green = G.getValue(i);
        double blue = B.getValue(i);

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
        R.umbral(value);
        G.umbral(value);
        B.umbral(value);
        updateFullImg();
    }

    public void toGrey() {
        for (int i = 0; i < width * height; i++) {
            double aux = getGreyLevel(i);
            R.setValue(i, aux);
        }
        G = R.clone();
        B = R.clone();
        updateFullImg();
    }

    public void equalize() {
        R.equalize();
        G.equalize();
        B.equalize();
        updateFullImg();
    }

    public void multiplyNoise(EasyImage ei) {
        for (int i = 0; i < width * height; i++) {
            R.multiply(i, ei.R.getValue(i));
            G.multiply(i, ei.G.getValue(i));
            B.multiply(i, ei.B.getValue(i));
        }
        if (!isAppropiate()) {
            normalize();
        }
        updateFullImg();
    }
    
    public void module(EasyImage ei) {
        for (int i = 0; i < width * height; i++) {
            R.module(i, ei.R.getValue(i));
            G.module(i, ei.G.getValue(i));
            B.module(i, ei.B.getValue(i));
        }
//        if (!isAppropiate()) {
//            normalize();
//        }
        updateFullImg();
    }
     public void module(EasyImage ei1,EasyImage ei2,EasyImage ei3) {
        for (int i = 0; i < width * height; i++) {
            R.module(i, ei1.R.getValue(i),ei2.R.getValue(i),ei3.R.getValue(i));
            G.module(i, ei1.G.getValue(i),ei2.G.getValue(i),ei3.G.getValue(i));
            B.module(i, ei1.B.getValue(i),ei2.B.getValue(i),ei3.B.getValue(i));
        }
//        if (!isAppropiate()) {
//            normalize();
//        }
        updateFullImg();
    }
       public void applyFunction(FunctionToApply fn, EasyImage ei) {
            R.applyFunction(fn, ei.R);
            G.applyFunction(fn, ei.G);
            B.applyFunction(fn, ei.B);
        
//        if (!isAppropiate()) {
//            normalize();
//        }
        updateFullImg();
    }
     public void applyFunction(FunctionToApply fn,EasyImage... ei) {
        EasyImage[] imgs = ei;
        Channel[] redChnls = new Channel[imgs.length];
		Channel[] greenChnls = new Channel[imgs.length];
		Channel[] blueChnls = new Channel[imgs.length];

		for (int i = 0; i < imgs.length; i++) {
			redChnls[i] = imgs[i].R;
			greenChnls[i] = imgs[i].G;
			blueChnls[i] = imgs[i].B;
		}
        R.applyFunction(fn, redChnls);
        G.applyFunction(fn, greenChnls);
        B.applyFunction(fn, blueChnls);
//        if (!isAppropiate()) {
//            normalize();
//        }
        updateFullImg();
    }

    public void addNoise(EasyImage ei) {
        for (int i = 0; i < width * height; i++) {
            R.add(i, ei.R.getValue(i));
            G.add(i, ei.G.getValue(i));
            B.add(i, ei.B.getValue(i));
        }
        if (!isAppropiate()) {
            normalize();
        }
        updateFullImg();
    }

    public void addSaltNPepperColor(double p, double pSalt) {
        Random aux = new Random(System.currentTimeMillis());
        for (int i = 0; i < width * height; i++) {
            R.setValue(i, aux.nextDouble() < p ? (aux.nextDouble() < pSalt ? 255 : 0) : R.getValue(i));
            G.setValue(i, aux.nextDouble() < p ? (aux.nextDouble() < pSalt ? 255 : 0) : G.getValue(i));
            B.setValue(i, aux.nextDouble() < p ? (aux.nextDouble() < pSalt ? 255 : 0) : B.getValue(i));
        }
        updateFullImg();
    }

    public void addSaltNPepper(double p, double pSalt) {
        Random aux = new Random(System.currentTimeMillis());
        for (int i = 0; i < width * height; i++) {
            int bw = (aux.nextDouble() < pSalt ? 255 : 0);
            if (aux.nextDouble() < p) {
                R.setValue(i, bw);
                G.setValue(i, bw);
                B.setValue(i, bw);
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

    public void applyMask(Mask m) {
        Channel auxR = new Channel(width, height);
        Channel auxG = new Channel(width, height);
        Channel auxB = new Channel(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                auxR.setValue(x, y, R.applyMask(x, y, m));
                auxG.setValue(x, y, G.applyMask(x, y, m));
                auxB.setValue(x, y, B.applyMask(x, y, m));
            }
        }
        R.setValues(auxR.values);
        G.setValues(auxG.values);
        B.setValues(auxB.values);
        updateFullImg();
    }
    
    public void applyDirectionalMask(Mask m){
        
        EasyImage aux1 = new EasyImage(getBufferedImage());
        EasyImage aux2 = new EasyImage(getBufferedImage());
        EasyImage aux3 = new EasyImage(getBufferedImage());
        applyMask(m);
        normalize();
        m.rotate45();
        aux1.applyMask(m);
        aux1.normalize();
        m.rotate45();
        aux2.applyMask(m);
        aux2.normalize();
        m.rotate45();
        aux3.applyMask(m);
        aux3.normalize();
        applyFunction(new ModuleFunction(),aux1,aux2,aux3);
        updateFullImg();
    }

    public void applyMedianMask(int height, int width) {
        int i;
        double[] newChannelR = new double[this.height * this.width];
        double[] newChannelG = new double[this.height * this.width];
        double[] newChannelB = new double[this.height * this.width];
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                i = y * getWidth() + x;
                newChannelR[i] = R.applyMedianMask(x, y, height, width);
                newChannelG[i] = G.applyMedianMask(x, y, height, width);
                newChannelB[i] = B.applyMedianMask(x, y, height, width);
            }
        }
        R.setValues(newChannelR);
        G.setValues(newChannelG);
        B.setValues(newChannelB);
        updateFullImg();
    }
    
    public void applyLocalVarianceEval(double variance){
        R.localVarianceEvaluation(variance);
        G.localVarianceEvaluation(variance);
        B.localVarianceEvaluation(variance);
        updateFullImg();
    }
    
    public void applyZeroCrossing(double umbral) {
		this.R.zeroCross(umbral);
		this.G.zeroCross(umbral);
		this.B.zeroCross(umbral);
                updateFullImg();
	}
    public void globalThreshold() {
		this.R.globalThreshold();
		this.G.globalThreshold();
		this.B.globalThreshold();
                updateFullImg();
	}
    
    public void otsuThreshold() {
		this.R.otsuThreshold();
		this.G.otsuThreshold();
		this.B.otsuThreshold();
                updateFullImg();
	}
    public void applyAnisotropicDiffusion(int iterations, BorderDetector bd) {
		this.R.applyAnisotropicDiffusion(iterations, bd);
		this.G.applyAnisotropicDiffusion(iterations, bd);
		this.B.applyAnisotropicDiffusion(iterations, bd);
                updateFullImg();
	}

       public void applyIsotropicDiffusion(int iterations, BorderDetector bd) {
		this.R.applyIsotropicDiffusion(iterations, bd);
		this.G.applyIsotropicDiffusion(iterations, bd);
		this.B.applyIsotropicDiffusion(iterations, bd);
                updateFullImg();
	}


}
