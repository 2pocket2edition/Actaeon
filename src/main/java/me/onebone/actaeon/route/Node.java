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

package me.onebone.actaeon.route;

import cn.nukkit.math.Vector3;

public class Node {
    private Node parent = null;
    public boolean closed = false;

    private Vector3 node;
    public double f = -1;
    public double g = -1;

    public Node(double x, double y, double z) {
        this.node = new Vector3(x, y, z);
    }

    public Node(Vector3 vec) {
        if (vec == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }

        this.node = new Vector3(vec.x, vec.y, vec.z);
    }

    public Vector3 getVector3() {
        return new Vector3(node.x, node.y, node.z);
    }

    public Vector3 getRawVector3() {
        return this.node;
    }

    public double getX() {
        return this.node.x;
    }

    public double getY() {
        return this.node.y;
    }

    public double getZ() {
        return this.node.z;
    }

    public void add(double x, double y, double z) {
        this.node = this.node.add(x, y, z);
    }

    public String toString() {
        return "Node (x=" + this.node.x + ", y=" + this.node.y + ", " + this.node.z + ")";
    }

    public void setParent(Node node) {
        this.parent = node;
    }

    public Node getParent() {
        return this.parent;
    }

    public boolean equals(Node node) {
        return this.getVector3().equals(node.getVector3());
    }
}
