package me.onebone.actaeon.hook;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDirt;
import cn.nukkit.block.BlockGrass;
import cn.nukkit.block.BlockTallGrass;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.network.protocol.EntityEventPacket;
import me.onebone.actaeon.entity.animal.Animal;

/**
 * Created by CreeperFace on 15.7.2017.
 */
public class EatGrassHook extends MovingEntityHook {

    private int timer = 40;
    private boolean executing = false;

    public EatGrassHook(Animal entity){
        super(entity);
    }

    @Override
    public void onUpdate(int tick) {
        if(!executing) {
            if (this.entity.level.rand.nextInt(((Animal) this.entity).isBaby() ? 50 : 1000) != 0) {
                return;
            } else {
                Block block = this.entity.getLevelBlock();

                if (block instanceof BlockTallGrass || block.down() instanceof BlockGrass) {
                    this.executing = true;

                    EntityEventPacket pk = new EntityEventPacket();
                    pk.eid = this.getEntity().getId();
                    pk.event = EntityEventPacket.EAT_GRASS_ANIMATION;
                    Server.broadcastPacket(this.getEntity().getViewers().values(), pk);
                }
            }
        } else {
            this.timer = Math.max(0, this.timer - 1);

            if(this.timer == 4) {
                Block block = this.entity.getLevelBlock();

                if(block instanceof BlockTallGrass) {
                    if (this.entity.level.getGameRules().getBoolean("mobGriefing")) {
                        this.entity.level.useBreakOn(block);
                    }

                    //TODO: grow bonus
                } else {
                    block = block.down();

                    if(block.getId() == Block.GRASS) {
                        if (this.entity.level.getGameRules().getBoolean("mobGriefing")) {
                            this.entity.level.addParticle(new DestroyBlockParticle(block, block));
                            this.entity.level.setBlock(block, new BlockDirt(), true, false);
                        }

                        //TODO: grow bonus
                    }
                }
            }
        }
    }
}
