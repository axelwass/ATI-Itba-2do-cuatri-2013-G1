/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

/**
 *
 * @author maximo
 */
class Circle implements Comparable<Circle> {

    double a;
    double b;
    double r;
    int votes;

    public Circle(double a, double b, double r, int votes) {
        this.a = a;
        this.b = b;
        this.r = r;
        this.votes = votes;
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        Circle aux = (Circle) obj;
        return a == aux.a && b == aux.b && r == aux.r;
    }

    @Override
    public int hashCode() {
        return (int) (3 * a + 5 * b + 7 * r);
    }

    @Override
    public int compareTo(Circle obj) {
        return obj.votes - votes;
    }
}
