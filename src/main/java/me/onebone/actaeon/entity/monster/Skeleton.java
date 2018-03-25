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

package me.onebone.actaeon.entity.monster;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.entity.mob.EntitySkeleton;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBow;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import me.onebone.actaeon.hook.AttackHook;
import me.onebone.actaeon.hook.WanderHook;
import me.onebone.actaeon.target.AreaPlayerTargetAI;
import me.onebone.actaeon.task.attack.AttackTaskShoot;
import me.onebone.actaeon.util.Utils;

/**
 * @author DaPorkchop_
 */
public class Skeleton extends Monster implements EntityAgeable {
    public static final int NETWORK_ID = EntitySkeleton.NETWORK_ID;
    private static final ItemBow BOW = new ItemBow();

    public Skeleton(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.setTargetAI(new AreaPlayerTargetAI(this, 500, 32));
        this.addHook("attack", new AttackHook(this, this.getAttackDistance(), this.getDamage(), 5000, 10, 180, AttackTaskShoot::new));
        this.addHook("wander", new WanderHook(this));
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getLength() {
        return 0.6f;
    }

    @Override
    protected float getGravity() {
        return 0.05f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.8f;
        }
        return 1.8f;
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
        return new Item[]{
                Item.get(Item.BONE, 0, Utils.rand(0, 3)),
                Item.get(Item.ARROW, 0, Utils.rand(0, 3)),
                Item.get(Item.BOW, 0, Math.max(0, Utils.rand(-10, 1)))
        };
    }

    public double getAttackDistance() {
        return 16;
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getId();
        pk.item = BOW;
        pk.hotbarSlot = 9;
        player.dataPacket(pk);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setMaxHealth(20);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public int getXp() {
        return Utils.rand(1, 4) + (isBaby() ? 7 : 0);
    }
}
