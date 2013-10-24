/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

/**
 *
 * @author maximo
 */
class RoThetaLine implements Comparable<RoThetaLine>{
    double ro;
	double theta;
	int ammount;

	public RoThetaLine(double ro, double theta, int ammount) {
		this.ro = ro;
		this.theta = theta;
		this.ammount = ammount;
	}

	@Override
	public boolean equals(Object obj) {
		return ro == ((RoThetaLine) obj).ro
				&& theta == ((RoThetaLine) obj).theta;
	}

	@Override
	public int hashCode() {
		return (int) (3 * ro + 5 * theta);
	}

	@Override
	public int compareTo(RoThetaLine obj) {
		return obj.ammount - ammount;
	}
}
