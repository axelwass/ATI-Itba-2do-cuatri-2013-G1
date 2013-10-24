package com.maxel.ati.tp;


import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by maximo
 */
public class TitaFunction {

    private int[][] values;
    private int height;
    private int width;

    public TitaFunction(List<Point> selection, int height, int width) {
        values = new int[height][width];
        this.height = height;
        this.width = width;

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                values[x][y] = 3;
            }
        }

        for (Point c : selection) {
            if (!outOfBounds(c)) {
                values[c.y][c.x] = -3;
            }
        }
        List<Point> in = this.getIn();
        for (Point c : in) {
            for (Point n : N4(c)) {
                if (!outOfBounds(n)) {
                    if (values[n.y][n.x] == 3) {
                        values[n.y][n.x] = -1;
                    }
                }
            }
        }
        List<Point> lIn = this.getlIn();
        for (Point c : lIn) {
            for (Point n : N4(c)) {
                if (!outOfBounds(n)) {
                }
                if (values[n.y][n.x] == 3) {
                    values[n.y][n.x] = 1;
                }
            }
        }
    }

    public static List<Point> N4(Point p) {
        List<Point> resp = new ArrayList<Point>();
        resp.add(new Point(p.x, p.y - 1));
        resp.add(new Point(p.x - 1, p.y));
        resp.add(new Point(p.x + 1, p.y));
        resp.add(new Point(p.x, p.y + 1));
        return resp;
    }

    public boolean isOut(Point c) {
        if (outOfBounds(c))
            return false;
        return values[c.y][c.x] == 3;
    }

    private boolean outOfBounds(Point c) {
        return c.y <= 0 || c.y >= this.height || c.x <= 0 || c.x >= this.width;
    }

    public boolean islOut(Point c) {
        if (outOfBounds(c))
            return false;
        return values[c.y][c.x] == 1;
    }

    public boolean isIn(Point c) {
        if (outOfBounds(c))
            return false;
        return values[c.y][c.x] == -3;
    }

    public boolean islIn(Point c) {
        if (outOfBounds(c))
            return false;
        return values[c.y][c.x] == -1;
    }

    public List<Point> getlOut() {
        List<Point> ret = new ArrayList<Point>();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++)
                if (values[x][y] == 1) {
                    ret.add(new Point(y, x));
                }
        }
        return ret;
    }

    public void setlOut(Point c) {
        if (outOfBounds(c))
            return;
        values[c.y][c.x] = 1;
    }

    public List<Point> getlIn() {
        List<Point> ret = new ArrayList<Point>();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++)
                if (values[x][y] == -1) {
                    ret.add(new Point(y, x));
                }
        }
        return ret;
    }

    public void setlIn(Point c) {
        if (outOfBounds(c))
            return;
        values[c.y][c.x] = -1;
    }

    public List<Point> getIn() {
        List<Point> ret = new ArrayList<Point>();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++)
                if (values[x][y] == -3) {
                    ret.add(new Point(y, x));
                }
        }
        return ret;
    }

    public void setIn(Point c) {
        if (outOfBounds(c))
            return;
        values[c.y][c.x] = -3;
    }

    public List<Point> getInnerIn() {
        int maxX = 0, maxY = 0, minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        List<Point> result = new ArrayList<Point>();

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (values[x][y] == -3) {
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                    if (x < minX) {
                        minX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    result.add(new Point(y,x));
                }
            }
        }

        final int midX = (maxX + minX) / 2, midY = (maxY + minY) / 2;

        Collections.sort(result, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                if ((dist(p1.y, p1.x, midX,  midY) - dist(p2.y, p2.x, midX, midY)) > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        int k = 30;
        int min = (result.size() < k) ? result.size() : k;

        result = result.subList(0, min - 1);



        return result;
    }

    private int dist(int x1, int y1, int x2, int y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    public List<Point> getOut() {
        List<Point> ret = new ArrayList<Point>();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++)
                if (values[x][y] == 3) {
                    ret.add(new Point(y, x));
                }
        }
        return ret;
    }

    public void setOut(Point c) {
        if (outOfBounds(c))
            return;
        values[c.y][c.x] = 3;
    }

    @Override
    public String toString() {
        String resp = "";
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                resp = resp.concat(String.valueOf(values[x][y]) + "\t");
            }
            resp = resp.concat("\t|\n");
        }
        return resp;
    }


}