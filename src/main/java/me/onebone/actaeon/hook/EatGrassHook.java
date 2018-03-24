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
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDirt;
import cn.nukkit.block.BlockGrass;
import cn.nukkit.block.BlockTallGrass;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.network.protocol.EntityEventPacket;
import me.onebone.actaeon.entity.animal.Animal;
import me.onebone.actaeon.util.Utils;

/**
 * Created by CreeperFace on 15.7.2017.
 */
public class EatGrassHook extends MovingEntityHook {

    private int timer = 40;

    public EatGrassHook(Animal entity) {
        super(entity);
    }

    @Override
    public void onUpdate(int tick) {
        if (!this.executing) {
            if (Utils.rand(0, ((Animal) this.entity).isBaby() ? 50 : 1000) != 0) {
                return;
            } else {
                Block block = this.entity.getLevelBlock();

                if (block instanceof BlockTallGrass || block.down() instanceof BlockGrass) {
                    this.timer = 40;

                    EntityEventPacket pk = new EntityEventPacket();
                    pk.eid = this.getEntity().getId();
                    pk.event = EntityEventPacket.EAT_GRASS_ANIMATION;
                    Server.broadcastPacket(this.getEntity().getViewers().values(), pk);
                    this.executing = true;
                    this.entity.routeLeading = false;
                    this.entity.getRoute().forceStop();
                    this.entity.getRoute().arrived();
                }
            }

            return;
        }

        this.timer = Math.max(0, this.timer - 1);

        if (this.timer == 4) {
            Block block = this.entity.getLevelBlock();

            if (block instanceof BlockTallGrass) {
                //if (this.entity.level.getGameRules().getBoolean("mobGriefing")) {
                this.entity.level.useBreakOn(block);
                //}

                //TODO: grow bonus
            } else {
                block = block.down();

                if (block.getId() == Block.GRASS) {
                    //if (this.entity.level.getGameRules().getBoolean("mobGriefing")) {
                    this.entity.level.addParticle(new DestroyBlockParticle(block, block));
                    this.entity.level.setBlock(block, new BlockDirt(), true, false);
                    //}

                    //TODO: grow bonus
                }
            }
        }

        if (timer == 0) {
            this.executing = false;
            this.entity.routeLeading = true;
        }
    }
}
