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

package me.onebone.actaeon;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.entity.animal.Chicken;
import me.onebone.actaeon.entity.animal.Cow;
import me.onebone.actaeon.entity.animal.Pig;
import me.onebone.actaeon.entity.animal.Sheep;
import me.onebone.actaeon.entity.animal.Wolf;
import me.onebone.actaeon.entity.monster.Zombie;
import me.onebone.actaeon.runnable.TaskWatchDog;

public class Actaeon extends PluginBase {

    private static Actaeon instance;

    public static Actaeon getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        if (instance == null) instance = this;
    }

    public void onEnable() {
        this.saveDefaultConfig();

        new TaskWatchDog().start();

        this.registerEntity("Sheep", Sheep.class);
        this.registerEntity("Cow", Cow.class);
        this.registerEntity("Chicken", Chicken.class);
        this.registerEntity("Pig", Pig.class);
        this.registerEntity("Zombie", Zombie.class);
        this.registerEntity("Wolf", Wolf.class);

        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, Zombie.NETWORK_ID));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 10));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 11));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 12));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 13));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 14));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 18));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 22));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 23));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 24));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 25));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 28));
    }

    private void registerEntity(String name, Class<? extends MovingEntity> clazz) {
        Entity.registerEntity(name, clazz, true);
    }
}
