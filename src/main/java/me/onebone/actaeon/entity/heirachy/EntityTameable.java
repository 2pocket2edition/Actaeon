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

package me.onebone.actaeon.entity.heirachy;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityOwnable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.entity.heirachy.type.Animal;

/**
 * @author CreeperFace
 */
public abstract class EntityTameable extends Animal implements EntityOwnable {

    protected String owner;
    protected Player ownerInstance;

    public EntityTameable(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        //TODO: load owner from NBT
    }

    public boolean isTamed() {
        return getDataFlag(DATA_FLAGS, DATA_FLAG_TAMED);
    }

    public void setTamed(boolean tamed) {
        setDataFlag(DATA_FLAGS, DATA_FLAG_TAMED, tamed);
    }

    public boolean isSitting() {
        return getDataFlag(DATA_FLAGS, DATA_FLAG_SITTING);
    }

    public void setSitting(boolean sitting) {
        setDataFlag(DATA_FLAGS, DATA_FLAG_SITTING, sitting);
    }

    @Override
    public Player getOwner() {
        if (ownerInstance == null || ownerInstance.closed) {
            ownerInstance = this.getServer().getPlayerExact(this.owner);
        }

        return ownerInstance;
    }

    public void setOwner(Player p) {
        this.ownerInstance = p;
        this.owner = p.getName();
    }

    @Override
    public String getOwnerName() {
        return owner;
    }

    @Override
    public void setOwnerName(String owner) {
        this.owner = owner;
    }

    public boolean isOwner(Player player) {
        return owner.equalsIgnoreCase(player.getName());
    }

    public boolean hasOwner() {
        return owner != null;
    }
}
