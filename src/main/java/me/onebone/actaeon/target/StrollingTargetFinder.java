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

import cn.nukkit.math.Vector3;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.route.AdvancedRouteFinder;

import java.util.Random;

/**
 * StrollingTargetFinder
 * ===============
 * author: boybook
 * EaseCation Network Project
 * codefuncore
 * ===============
 */
public class StrollingTargetFinder extends TargetFinder {

    public int needFind = new Random().nextInt(3);
    public int needFindResetMax = 8;
    public int needFindResetUntil = 0;
    private double radius;
    public int findHighest = -1;  //<0为关闭寻找最高，0为至虚空，>0为向下方块格数限制

    public StrollingTargetFinder(MovingEntity entity) {
        this(entity, 5);
    }

    public StrollingTargetFinder(MovingEntity entity, double radius) {
        this(entity, radius, 1000 * (new Random().nextInt(2) + 2));
    }

    public StrollingTargetFinder(MovingEntity entity, double radius, long inter) {
        super(entity, inter);
        this.radius = radius;
    }

    @Override
    protected void find() {
        Random random = new Random();
        if (this.needFind > 0) {
            Vector3 base = this.entity.getRealTarget() != null ? this.entity.getTarget() : this.getEntity().getPosition();
            //for (int i = 0; i < 5; i++) {
            double r = random.nextDouble() * 360;
            double x = this.radius * Math.cos(Math.toRadians(r));
            double z = this.radius * Math.sin(Math.toRadians(r));
            double y = base.getY();
            if (this.findHighest >= 0 && this.getEntity().getRoute() instanceof AdvancedRouteFinder) {
                Vector3 highest = ((AdvancedRouteFinder) this.getEntity().getRoute()).getHighestUnder(x, y + 2, z, this.findHighest == 0 ? (int) y + 2 : this.findHighest);
                if (highest == null)
                    return;  //不可走, 重新尝试选点
                y = highest.getY() + 1;
            }
            this.entity.setTarget(new Vector3(base.getX() + x, y, base.getZ() + z), this.getEntity().getName());
            //break;
            //}
            this.needFind--;
            if (random.nextInt(10) < 2) {
                this.entity.setSprinting(true);
            } else {
                this.entity.setSprinting(false);
            }
        } else {
            this.entity.getRoute().forceStop();
            this.entity.setTarget(null, this.getEntity().getName());
        }
        if (this.needFind <= this.needFindResetUntil) this.needFind = this.needFindResetMax;
    }

}
