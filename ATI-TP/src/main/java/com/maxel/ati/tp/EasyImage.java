/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
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

    public void dynamicRangeCompress(){
        dynamicRangeCompression(channelR);
        dynamicRangeCompression(channelG);
        dynamicRangeCompression(channelB);
        updateFullImg();
    }
    
    private void dynamicRangeCompression(int[] channel) {
		int max = 0;
                for(int x: channel){
                    max = max>x?max:x;
                }
                int L = 255;
		double c = (L - 1) / Math.log(1 + max);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				channel[x + y*width] =(int) (c * Math.log(1 + channel[x + y*width]));
			}
		}
	}
    
    public void multiply(int factor){
        for(int i=0;i<channelR.length;i++){
            channelR[i]*=factor;
            channelG[i]*=factor;
            channelB[i]*=factor;
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
            fullImg[i] -= img2.fullImg[i];
        }
        if (!isAppropiate()) {
            normalize();
        }
    }

    public void add(EasyImage img2) {
        for (int i = 0; i < fullImg.length; i++) {
            fullImg[i] += img2.fullImg[i];
        }
        updateChannels();
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
}
