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


import cn.nukkit.Server;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.task.ChickenEggTask;
import me.onebone.actaeon.util.Utils;

public class ChickenEggHook extends MovingEntityHook {
    private int nextEggTick;

    public ChickenEggHook(MovingEntity animal) {
        super(animal);
        nextEggTick = Server.getInstance().getTick() + Utils.rand(6000, 12000);
    }

    @Override
    public void onUpdate(int tick) {
        if (tick >= nextEggTick) {
            nextEggTick = tick + Utils.rand(6000, 12000);
            this.entity.updateBotTask(new ChickenEggTask(entity));
        }
    }
}
