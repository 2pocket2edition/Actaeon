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

/**
 * @author CreeperFace
 */
public class FollowItemAI extends MovingEntityHook {

    private Item item;
    private Player holder = null;
    private double range;

    public FollowItemAI(MovingEntity entity, Item item, double range) {
        super(entity);
        this.item = item;
        this.range = range * range;
    }

    @Override
    public void onUpdate(int tick) {
        if (tick % 10 == 0) {
            if (holder != null) {
                if (holder.closed) {
                    holder = null;
                    return;
                }

                Item item = holder.getInventory().getItemInHand();
                if (item.getId() != this.item.getId() || item.getDamage() != this.item.getDamage()) {
                    this.holder = null;
                    this.getEntity().setTarget(null, this.getEntity().getName());
                } else {
                    return;
                }
            }

            Player near = null;
            double last = this.range;

            for (Player player : this.getEntity().getLevel().getPlayers().values()) {
                double distance = player.distanceSquared(getEntity());

                if (distance <= last) {
                    Item item = player.getInventory().getItemInHand();

                    if (item.getId() == this.item.getId() && item.getDamage() == this.item.getDamage()) {
                        near = player;
                        last = distance;
                    }
                }
            }

            if (near != null) {
                this.holder = near;
                this.getEntity().getRoute().forceStop();
                //this.getEntity().get

                this.getEntity().setTarget(near, "itemfollow", true);
            }
        }
    }
}
