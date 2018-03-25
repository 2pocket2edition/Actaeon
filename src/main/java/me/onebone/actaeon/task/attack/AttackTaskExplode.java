package me.onebone.actaeon.task.attack;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import me.onebone.actaeon.entity.heirachy.MovingEntity;

public class AttackTaskExplode extends AttackTask {
    private final int fuse;
    private int counter = 0;

    public AttackTaskExplode(MovingEntity entity, Entity target, float damage, double viewAngle, int fuse) {
        super(entity, target, damage, viewAngle);
        this.fuse = fuse;
    }

    @Override
    protected boolean doAttack(boolean valid) {
        if (valid && ++counter == fuse)  {
            ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this.entity, damage);
            this.entity.getServer().getPluginManager().callEvent(ev);

            if (!ev.isCancelled()) {
                Explosion explosion = new Explosion(this.entity, (float) ev.getForce(), this.entity);
                if (ev.isBlockBreaking()) {
                    explosion.explodeA();
                }
                explosion.explodeB();
                //this.entity.exploded = true;
            }
            this.entity.close();
            return true;
        }
        return false;
    }
}
