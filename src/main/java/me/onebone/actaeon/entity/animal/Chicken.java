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

import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.entity.passive.EntityChicken;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.entity.attribute.IFallable;
import me.onebone.actaeon.hook.AnimalGrowHook;
import me.onebone.actaeon.hook.ChickenEggHook;
import me.onebone.actaeon.target.AreaHandItemTargetAI;
import me.onebone.actaeon.util.Utils;

public class Chicken extends Animal implements EntityAgeable, IFallable {
    public static final int NETWORK_ID = EntityChicken.NETWORK_ID;
    private boolean isBaby = false;

    public Chicken(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.setTargetAI(new AreaHandItemTargetAI(this, 500, Item.get(Item.WHEAT_SEEDS), 10));
        this.addHook("egg", new ChickenEggHook(this));
    }

    @Override
    public float getWidth() {
        return 0.4f;
    }

    @Override
    protected float getGravity() {
        return 0.05f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.51f;
        }
        return 0.7f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.51f;
        }
        return 0.7f;
    }

    @Override
    public Item[] getDrops() {
        if (isBaby()) {
            return new Item[0];
        } else {
            return new Item[]{
                    Item.get(this.isOnFire() ? Item.COOKED_CHICKEN : Item.RAW_CHICKEN, 0, 1),
                    Item.get(Item.FEATHER, 0, Utils.rand(0, 3))
            };
        }
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        return super.entityBaseTick(tickDiff);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setMaxHealth(4);
        isBaby = (Utils.rand(1, 11) == 1);
        setBaby(isBaby);
        if (isBaby) {
            this.addHook("grow", new AnimalGrowHook(this, Utils.rand(20 * 60 * 10, 20 * 60 * 20)));
        }
    }

    @Override
    public boolean isBaby() {
        return isBaby;
    }

    @Override
    public boolean isBreedingItem(Item item) {
        int id = item.getId();

        return id == Item.WHEAT_SEEDS || id == Item.MELON_SEEDS || id == Item.BEETROOT_SEED || id == Item.PUMPKIN_SEEDS;
    }
}
