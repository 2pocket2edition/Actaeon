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

package me.onebone.actaeon.target;

import me.onebone.actaeon.entity.MovingEntity;

public abstract class TargetFinder {
    protected MovingEntity entity = null;
    protected long nextFind = 0;
    protected long interval;

    public TargetFinder(MovingEntity entity, long interval) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null");
        this.entity = entity;
        this.interval = interval;
    }

    public MovingEntity getEntity() {
        return this.entity;
    }

    public void onUpdate() {
        if (System.currentTimeMillis() >= nextFind) {
            this.find();
            this.nextFind = System.currentTimeMillis() + this.interval;
        }
    }

    protected abstract void find();

}
