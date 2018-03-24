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

package me.onebone.actaeon.entity.animal;

import cn.nukkit.entity.passive.EntitySheep;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.hook.AnimalHook;
import me.onebone.actaeon.util.Utils;

public class Sheep extends Animal {
    public static final int NETWORK_ID = EntitySheep.NETWORK_ID;

    public Sheep(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.addHook("targetFinder", new AnimalHook(this, 500, Item.get(Item.WHEAT), 10));
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.9f; // No have information
        }
        return 1.3f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.95f * 0.9f; // No have information
        }
        return 0.95f * getHeight();
    }

    @Override
    public String getName() {
        return this.getNameTag();
    }

    @Override
    public Item[] getDrops() {
        if (isBaby()) {
            return new Item[0];
        } else {
            return new Item[]{
                    Item.get(Item.WOOL, 0, 1),
                    Item.get(this.isOnFire() ? Item.COOKED_MUTTON : Item.RAW_MUTTON, 0, Utils.rand(1, 3))
            };
        }
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        return super.entityBaseTick(tickDiff);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(8);
    }
}
