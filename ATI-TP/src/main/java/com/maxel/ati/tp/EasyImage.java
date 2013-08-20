/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author maximo
 */
public class EasyImage {

    BufferedImage img;
    int[] channelR;
    int[] channelG;
    int[] channelB;
    int[] channelA;
    int[] fullImg;

    public EasyImage(BufferedImage img) {
        this.img = img;

        fullImg = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        channelR = new int[fullImg.length];
        channelG = new int[fullImg.length];
        channelB = new int[fullImg.length];
        channelA = new int[fullImg.length];
        for (int i = 0; i < fullImg.length; i++) {
            int rgb = fullImg[i];
            channelA[i] = (rgb >> 24) & 0x000000FF;
            channelR[i] = (rgb >> 16) & 0x000000FF;
            channelG[i] = (rgb >> 8) & 0x000000FF;
            channelB[i] = (rgb) & 0x000000FF;
        }
    }

    public BufferedImage getImage() {

        return img;
    }

    public void updateImg() {
        img.setRGB(0, 0, img.getWidth(), img.getHeight(), fullImg, 0, img.getWidth());
    }

    public void applyNegative() {
        for (int i = 0; i < img.getWidth()*img.getHeight(); i++) {
            fullImg[i] = 0x0fff - fullImg[i];
        }
        //                    for (int x = 0; x < img.getWidth(); x++) {
//                        for (int y = 0; y < img.getHeight(); y++) {
//                            int aux = img.getRGB(x, y);
//                            aux = 0x0fff - aux;
//                            img.setRGB(x, y, aux);
//                        }
//                    }
    }
}
