package com.example.images;

import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnionFindTest {
    @Test
    public void unionFind() {
        int[] dset = {10, 11, 11, 2, 8, 13, 2, 7, 8, 9, 11, 11, 7, 8};
        assertEquals(11, UnionFind.find(dset, 0));
        assertEquals(8, UnionFind.find(dset, 5));
        UnionFind.union(dset, 11, 8);
        assertEquals(11, UnionFind.find(dset, 5));
    }
}
