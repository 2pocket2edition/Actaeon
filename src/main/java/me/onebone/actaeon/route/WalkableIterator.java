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

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

import java.util.Iterator;

public class WalkableIterator implements Iterator<Block> {
    private final Level level;
    private final int maxDistance;
    private final double width;

    private boolean end = false;

    private Block currentBlockObject;

    private double currentDistance;
    private Vector3 startPosition;
    private Vector3 currentPosition = null;
    private Vector3 direction = null;

    private AdvancedRouteFinder advancedRouteFinder;

    public WalkableIterator(AdvancedRouteFinder advancedRouteFinder, Level level, Vector3 start, Vector3 direction, double width, int maxDistance) {
        this.advancedRouteFinder = advancedRouteFinder;
        this.level = level;
        this.width = width;
        this.maxDistance = maxDistance == 0 ? 120 : maxDistance;
        this.currentDistance = 0;
        this.currentPosition = start.clone();
        this.startPosition = start.clone();
        this.direction = direction.normalize();
    }

    public Vector3 getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public Block next() {
        return this.currentBlockObject;
    }

    @Override
    public boolean hasNext() {
        this.scan();
        return this.currentBlockObject != null;
    }

    private void scan() {
        if (this.maxDistance != 0 && this.currentDistance > this.maxDistance) {
            this.end = true;
            return;
        }
        if (this.end) return;

        do {
            //if (this.currentDistance > 2 && new Random().nextInt(100) < 35) this.level.addParticle(new FlameParticle(this.currentPosition));
            Block block = this.level.getBlock(this.currentPosition);
            Vector3 next = this.currentPosition.add(this.direction);
            double walkable = this.advancedRouteFinder.isWalkableAt(block);
            if (walkable != 0 || (block.getBoundingBox() != null && block.getBoundingBox().calculateIntercept(this.currentPosition, next) != null)) {
                this.currentBlockObject = block;
                this.end = true;
            }
            this.currentPosition = next;
            this.currentDistance = this.currentPosition.distance(this.startPosition);
            if (this.maxDistance > 0 && this.currentDistance > this.maxDistance) this.end = true;
        } while (!this.end);
    }

}
