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

import cn.nukkit.Player;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.entity.passive.EntityCow;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.hook.AnimalGrowHook;
import me.onebone.actaeon.target.AreaHandItemTargetAI;
import me.onebone.actaeon.util.Utils;

public class Cow extends Animal implements EntityAgeable {
    public static final int NETWORK_ID = EntityCow.NETWORK_ID;
    private boolean isBaby = false;

    public Cow(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.setTargetAI(new AreaHandItemTargetAI(this, 500, Item.get(Item.WHEAT), 10));
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.65f;
        }
        return 1.3f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.65f;
        }
        return 1.2f;
    }

    @Override
    public boolean isBaby() {
        return isBaby;
    }

    @Override
    public Item[] getDrops() {
        if (isBaby()) {
            return new Item[0];
        } else {
            return new Item[]{
                    Item.get(this.isOnFire() ? Item.STEAK : Item.RAW_BEEF, 0, Utils.rand(1, 4)),
                    Item.get(Item.LEATHER, 0, Utils.rand(0, 3))
            };
        }
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        return super.entityBaseTick(tickDiff);
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        if (item.getId() == Item.BUCKET) {
            player.getInventory().addItem(Item.get(335, 0, 1));
            return true;
        }
        return false;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setMaxHealth(10);
        isBaby = false;
        //TODO: this is called every time the entity is loaded, meaning that adults can suddenly become babys again
        //isBaby = Utils.rand(1, 10) == 1;
        setBaby(isBaby);
        if (isBaby) {
            this.addHook("grow", new AnimalGrowHook(this, Utils.rand(20 * 60 * 10, 20 * 60 * 20)));
        }
    }
}
