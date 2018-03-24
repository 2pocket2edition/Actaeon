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

package me.onebone.actaeon.task.attack;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.network.protocol.EntityEventPacket;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.task.MovingEntityTask;

/**
 * AttackTask
 * ===============
 * author: boybook
 * ===============
 */
public class AttackTask extends MovingEntityTask {

    protected Entity target;
    protected float damage;
    protected double viewAngle;

    public AttackTask(MovingEntity entity, Entity target, float damage, double viewAngle) {
        super(entity);
        this.target = target;
        this.damage = damage;
        this.viewAngle = viewAngle;
    }

    @Override
    public void onUpdate(int tick) {
        double angle = Math.atan2(this.target.z - this.entity.z, this.target.x - this.entity.x);
        double yaw = ((angle * 180) / Math.PI) - 90;
        double min = this.entity.yaw - this.viewAngle / 2;
        double max = this.entity.yaw + this.viewAngle / 2;
        boolean valid;
        if (min < 0) {
            valid = yaw > 360 + min || yaw < max;
        } else if (max > 360) {
            valid = yaw < max - 360 || yaw > min;
        } else {
            valid = yaw < max && yaw > min;
        }
        doAttack(valid);
        this.entity.updateBotTask(null);
    }

    protected void doAttack(boolean valid)  {
        if (valid) {
            if (this.target.noDamageTicks <= 0) {
                this.target.attack(new EntityDamageByEntityEvent(this.getEntity(), this.target, EntityDamageEvent.DamageCause.ENTITY_ATTACK, this.damage));
                this.target.noDamageTicks = 10;
            }
        }

        EntityEventPacket pk = new EntityEventPacket();
        pk.eid = this.entity.getId();
        pk.event = 4;
        Server.broadcastPacket(this.getEntity().getViewers().values(), pk);
    }

    @Override
    public void forceStop() {
    }
}
