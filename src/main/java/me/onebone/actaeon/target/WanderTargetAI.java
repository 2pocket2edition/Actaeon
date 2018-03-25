package me.onebone.actaeon.target;

import cn.nukkit.entity.Entity;
import me.onebone.actaeon.entity.heirachy.MovingEntity;
import me.onebone.actaeon.util.Utils;

/**
 * Prevents code duplication with targetters that wander when there's no target
 *
 * @author DaPorkchop_
 */
public abstract class WanderTargetAI extends TargetAI {
    private Entity near;

    public WanderTargetAI(MovingEntity entity, long interval) {
        super(entity, interval);
    }

    @Override
    protected void find() {
        near = scan();

        if (near == null) {
            if (this.entity.isCurrentIdentifier("randomPos")) {
                if (Utils.rand(0, 6) == 0)
                    this.entity.setTarget(Utils.randomVector(this.entity, 10, 0, 10).add(0, 7), "randomPos",
                            !this.entity.isCurrentIdentifier("randomPos"));
            } else {
                this.entity.setTarget(Utils.randomVector(this.entity, 10, 0, 10).add(0, 7), "randomPos",
                        true);
            }
        } else {
            this.getEntity().setTarget(near, this.getEntity().getName(),
                    this.entity.isCurrentIdentifier("randomPos"));
            this.getEntity().setHate(near);
        }
    }

    protected abstract Entity scan();
}
