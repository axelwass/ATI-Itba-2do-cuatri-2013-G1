/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
        if (!isValid(x, y)) {
            throw new IndexOutOfBoundsException();
        }
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
        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
        for(int i=0; i<values.length; i++) {
                int val = (int) values[i];
                histogram[val]++;
        }
        return histogram;
 
    }
    public void otsuThreshold() {
 
        int[] histogram = imageHistogram();
        int total = values.length;
 
        float sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];
 
        float sumB = 0;
        int wB = 0;
        int wF = 0;
 
        float varMax = 0;
        int threshold = 0;
 
        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;
 
            if(wF == 0) break;
 
            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;
 
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
 
            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
        System.out.println("T = " + threshold);
        umbral(threshold);
 
    }
    public Channel clone(){
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

		suppressNoMaxs(this);
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
        for(int i=0;i<auxChannel.values.length;i++){
            if(auxChannel.getValue(i)>t2){
                auxChannel.setValue(i,255);
            }else if(auxChannel.getValue(i)<t1){
                auxChannel.setValue(i,0);
            }
        }
        Channel auxChannel2 = auxChannel.clone();
        for(int x=0;x<auxChannel.width;x++){
            for(int y=0;y<auxChannel.height;y++){
                double pixel = auxChannel.getValue(x, y);
                if(pixel != 0 && pixel != 255){
                    for(Double d:getValidNeighbors(x, y)){
                        if(d==255){
                            pixel =255;
                            auxChannel2.setValue(x, y, pixel);
                            break;
                        }
                    }
                    if(pixel!=255){
                        auxChannel2.setValue(x, y, 0);
                    }
                }
            }
        }
        this.values = auxChannel2.values;
    }
    
    private List<Double> getValidNeighbors(int x, int y){
        List<Double> l = new LinkedList<Double>();
        if(isValid(x-1, y)){
            l.add(getValue(x-1, y));
        }
        if(isValid(x, y-1)){
            l.add(getValue(x, y-1));
        }
        if(isValid(x+1, y)){
            l.add(getValue(x+1, y));
        }
        if(isValid(x, y+1)){
            l.add(getValue(x, y+1));
        }
        return l;
    }

    void applyCanny() {
        Channel auxChannel = clone();
        auxChannel.applyMask(Mask.newGaussianMask(11, 0.5));
        auxChannel.suppressNoMaxs();
        int umbral = (int)auxChannel.getGlobalThresholdValue();
        auxChannel.applyHistUmbralization(umbral, umbral+40);
        this.values = auxChannel.values;
    }

    void applySusan(boolean showBorders, boolean showCorners, int color) {
        Mask mask = Mask.newSusanMask();
		Channel newChannel = new Channel(this.width, this.height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double newValue = getValue(x, y);
				double s_ro = applySusanPixelMask(x, y, mask);
				if ((showBorders && isBorder(s_ro))
						|| (showCorners && isCorner(s_ro))) {
					newValue = color;
				}
				newChannel.setValue(x, y, newValue);
			}
		}
		this.values = newChannel.values;
    }
    
    private boolean isBorder(double a){
        return a>0.38&&a<0.62;
    }
    private boolean isCorner(double a){
        return a>0.63&&a<0.88;
    }
    
    private double applySusanPixelMask(int x, int y, Mask mask) {
		boolean ignoreByX = x < mask.getWidth() / 2
				|| x > this.getWidth() - mask.getWidth() / 2;
		boolean ignoreByY = y < mask.getHeight() / 2
				|| y > this.getHeight() - mask.getHeight() / 2;
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
}
