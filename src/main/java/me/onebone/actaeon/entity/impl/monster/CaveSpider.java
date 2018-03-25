package me.onebone.actaeon.entity.impl.monster;

import cn.nukkit.entity.mob.EntityCaveSpider;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class CaveSpider extends Spider {
    public static final int NETWORK_ID = EntityCaveSpider.NETWORK_ID;

    public CaveSpider(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getWidth() {
        return 0.7f;
    }

    @Override
    public float getLength() {
        return 0.7f;
    }

    @Override
    public float getHeight() {
        return 0.5f;
    }

    @Override
    public float getEyeHeight() {
        return 0.4f;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
}
