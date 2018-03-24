package me.onebone.actaeon.task.attack;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.util.Utils;

public class AttackTaskShoot extends AttackTask {
    public AttackTaskShoot(MovingEntity entity, Entity target, float damage, double viewAngle) {
        super(entity, target, damage, viewAngle);
    }

    @Override
    protected void doAttack(boolean valid) {
        if (valid)  {
            double f = 1.2;
            double yaw = this.entity.yaw + Utils.rand(-110, 111) / 10;
            double pitch = this.entity.pitch + Utils.rand(-60, 61) / 10;
            Location pos = new Location(this.entity.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.entity.y + this.entity.getHeight() - 0.18,
                    this.entity.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.entity.level);
            Entity k = Entity.createEntity("Arrow", pos, this);
            if (!(k instanceof EntityArrow)) {
                return;
            }

            EntityArrow arrow = (EntityArrow) k;
            arrow.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f,
                    Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));

            EntityShootBowEvent ev = new EntityShootBowEvent(this.entity, Item.get(Item.ARROW, 0, 1), arrow, f);
            this.entity.getServer().getPluginManager().callEvent(ev);

            EntityProjectile projectile = ev.getProjectile();
            if (ev.isCancelled()) {
                projectile.kill();
            } else {
                ProjectileLaunchEvent launch = new ProjectileLaunchEvent(projectile);
                this.entity.getServer().getPluginManager().callEvent(launch);
                if (launch.isCancelled()) {
                    projectile.kill();
                } else {
                    projectile.spawnToAll();
                    this.entity.level.addSound(this.entity, Sound.RANDOM_BOW);
                }
            }
        }
    }
}
