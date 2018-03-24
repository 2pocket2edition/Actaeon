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

package me.onebone.actaeon.hook;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.target.AreaHandItemTargetAI;

public class AnimalHook extends WanderHook {
    private int radius;
    private long interval;
    private Item item;

    public AnimalHook(MovingEntity animal, long interval, Item item, int radius) {
        super(animal);
        this.radius = radius;
        this.interval = interval;
        this.item = item;
        this.radius = radius;
    }

    @Override
    public void onUpdate(int tick) {
        if (tick % 20 == 0) {
            Player near = null;
            double nearest = this.radius * this.radius;

            for (Player player : this.getEntity().getLevel().getPlayers().values()) {
                if (this.getEntity().distanceSquared(player) < nearest) {
                    near = player;
                    nearest = this.getEntity().distance(player);
                }
            }

            if (near != null && entity.getTargetFinder() == null/* && near.getInventory().getItemInHand().equals(this.item, true, false)*/) {
                entity.setTargetFinder(new AreaHandItemTargetAI(this.entity, interval, item, radius));
            } else {
                entity.setTargetFinder(null);
            }
        }

        if (entity.getTargetFinder() == null) {
            super.onUpdate(tick);
        }
    }
}
