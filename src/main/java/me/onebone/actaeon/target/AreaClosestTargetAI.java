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

import cn.nukkit.entity.Entity;
import cn.nukkit.math.SimpleAxisAlignedBB;
import me.onebone.actaeon.entity.heirachy.MovingEntity;

/**
 * Created by CreeperFace on 19.7.2017.
 */
public class AreaClosestTargetAI extends WanderTargetAI {

    private int radius;

    public AreaClosestTargetAI(MovingEntity entity, long interval, int radius) {
        super(entity, interval);
        this.radius = radius;
    }

    @Override
    protected Entity scan() {
        Entity near = null;
        double nearest = Double.MAX_VALUE;

        for (Entity e : this.getEntity().getLevel().getCollidingEntities(new SimpleAxisAlignedBB(entity.x - radius, entity.y - radius, entity.z - radius, entity.x + radius, entity.y + radius, entity.z + radius))) {
            if (this.getEntity().distanceSquared(e) < nearest) {
                near = e;
                nearest = this.getEntity().distance(e);
            }
        }
        return near;
    }
}
