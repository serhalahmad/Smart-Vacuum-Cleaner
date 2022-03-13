package com.iea.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//  This class is used to store the paths (edges), and their weights
public class Path {
    private int cost;
    private final List<Point> path;
    private List<Integer> geneticPath;
    private double fitness;

    public Path() {
        this.geneticPath = new ArrayList<>();
        this.path = new ArrayList<>();
        this.cost = 0;
    }

    public Path(Path p) {
        this.geneticPath = new ArrayList<>(p.getGeneticPath());
        this.path = new ArrayList<>(p.path());
        this.cost = p.cost();
    }

    public void addNode(Point N) {
        path.add(new Point(N));
    }

    public int size() {
        return path.size();
    }

    public String print() {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (Point x : path) {
            sb.append(x).append("; ");
            if (i > 20) {
                i = 0;
                sb.append("<br>");
                continue;
            }
            i++;
        }
        sb.append(" <br> Cost = ").append(cost);
        return sb.toString();
    }

    public String printGenetic() {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < geneticPath.size(); x++) {
            sb.append(geneticPath.get(x)).append("; ");
            if (i > 20) {
                i = 0;
                sb.append("<br>");
                continue;
            }
            i++;
        }
        sb.append(" <br> Fitness = ").append(this.getFitness());
        return sb.toString();
    }

    public int cost() {
        return cost;
    }

    public List<Point> path() {
        return path;
    }

    public void setCost(int c) {
        cost = c;
    }

    public void addNodeFirst(Point current) {
        path.add(0, current);
    }

    public Path reverse() {
        Path inv = new Path(this);
        Collections.reverse(inv.path());
        return inv;
    }

    public double getFitness() {
        return 1.0 / (1 + this.cost);
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void addNode(int n) {
        this.geneticPath.add(n);
    }

    public void setGeneticPath(int[] p) {
        this.geneticPath = new ArrayList<>();
        for (int i = 0; i < p.length; i++) {
            this.geneticPath.add(p[i]);
        }
    }

    public List<Integer> getGeneticPath() {
        return geneticPath;
    }

    public int[] getPath() {
        int[] toReturn = new int[this.geneticPath.size()];
        for (int i = 0; i < this.geneticPath.size(); i++) {
            toReturn[i] = this.geneticPath.get(i);
        }
        return toReturn;
    }

}