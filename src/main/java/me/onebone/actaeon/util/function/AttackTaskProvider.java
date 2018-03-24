package me.onebone.actaeon.util.function;

import cn.nukkit.entity.Entity;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.task.attack.AttackTask;

/**
 * Provides an instance of {@link AttackTask} using the given arguments
 *
 * @author DaPorkchop_
 */
public interface AttackTaskProvider {
    AttackTask provide(MovingEntity entity, Entity target, float damage, double viewAngle);
}
