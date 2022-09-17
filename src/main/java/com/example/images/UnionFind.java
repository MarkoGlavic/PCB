package com.example.images;

import java.util.HashSet;
import java.util.Set;

public class UnionFind {

    private int sz[];


    public static int find(int[] a, int id) {

        if (a[id] < 0) return a[id];
        if (a[id] == id) return id;
        else return find(a, a[id]);

    }


    public static void union(int[] a, int p, int q) {


        a[find(a, q)] = find(a, p); //The root of q is made reference p

    }


}
