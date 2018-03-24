/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2018 onebone and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package me.onebone.actaeon.path;

import cn.nukkit.math.MathHelper;
import cn.nukkit.math.Vector3;

public class Node extends Vector3 {
    private static final int NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;
    public boolean closed = false;
    public double f = -1;
    public double g = -1;
    private Node parent = null;

    public Node(double x, double y, double z) {
        super(x, y, z);
    }

    public Node(Vector3 vec) {
        super(vec.x, vec.y, vec.z);
    }

    public static long toLong(long x, long y, long z) {
        return (x & X_MASK) << X_SHIFT | (y & Y_MASK) << Y_SHIFT | (z & Z_MASK);
    }

    public String toString() {
        return "Node (x=" + this.x + ", y=" + this.y + ", " + this.z + ")";
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node node) {
        this.parent = node;
    }

    public long toLong() {
        return toLong((long) this.x, (long) this.y, (long) this.z);
    }

    public boolean equals(Node node) {
        if (node == null) {
            return false;
        }

        return this.x == node.x && this.y == node.y && this.z == node.z;
    }
}
