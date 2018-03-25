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

import cn.nukkit.entity.Entity;
import me.onebone.actaeon.entity.heirachy.MovingEntity;
import me.onebone.actaeon.task.attack.AttackTask;
import me.onebone.actaeon.util.Utils;
import me.onebone.actaeon.util.function.AttackTaskProvider;

/**
 * AttackHook
 * ===============
 * author: boybook
 * ===============
 */
public class AttackHook extends MovingEntityHook {
    private AttackTaskProvider provider;
    private volatile long nextAttack = 0;
    private double attackDistance;
    private long coolDown;
    private int effectual;
    private double viewAngle;
    private float damage;

    public AttackHook(MovingEntity bot, double attackDistance, float damage, long coolDown, int effectual, double viewAngle) {
        super(bot);
        this.provider = AttackTask::new;
        this.attackDistance = attackDistance;
        this.damage = damage;
        this.coolDown = coolDown;
        this.effectual = effectual;
        this.viewAngle = viewAngle;
    }

    public AttackHook(MovingEntity bot, double attackDistance, float damage, long coolDown, int effectual, double viewAngle, AttackTaskProvider provider) {
        this(bot, attackDistance, damage, coolDown, effectual, viewAngle);
        this.provider = provider;
    }

    @Override
    public boolean shouldExecute() {
        Entity hate = this.entity.getHate();
        return hate != null && this.entity.distance(hate) <= this.attackDistance;
    }

    @Override
    public void onUpdate(int tick) {
        if (System.currentTimeMillis() > this.nextAttack) {
            if (this.entity.getTask() == null && this.entity.getHate() != null) {
                Entity hate = this.entity.getHate();
                if (this.entity.distance(hate) <= this.attackDistance) {
                    if (Utils.rand(0, 10) < this.effectual) {
                        this.entity.updateBotTask(provider.provide(this.entity, hate, this.damage, this.viewAngle));
                    }
                    this.nextAttack = System.currentTimeMillis() + coolDown;
                }
            }
        }
    }
}
