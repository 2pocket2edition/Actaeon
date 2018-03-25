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
import cn.nukkit.Server;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import me.onebone.actaeon.hook.MovingEntityHook;
import me.onebone.actaeon.path.pathfinder.PathFinderAStar;
import me.onebone.actaeon.path.Node;
import me.onebone.actaeon.path.PathFinder;
import me.onebone.actaeon.runnable.RouteFinderSearchAsyncTask;
import me.onebone.actaeon.target.TargetAI;
import me.onebone.actaeon.task.MovingEntityTask;
import me.onebone.actaeon.util.Utils;

import java.util.Hashtable;
import java.util.Map;

abstract public class MovingEntity extends EntityCreature {
    public boolean routeLeading = true;
    private boolean isKnockback = false;
    private PathFinder route = null;
    private TargetAI targetAI = null;
    private Vector3 target = null;
    private Entity hate = null;
    private String targetSetter = "";
    private Map<String, MovingEntityHook> hooks;
    private MovingEntityTask task = null;
    private boolean lookAtFront = true;

    public MovingEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        this.route = new PathFinderAStar(this);
        //this.route = new PathFinderDirect(this);
        this.setImmobile(false);
    }

    public void setBaby(boolean isBaby) {
        this.setDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY, isBaby);
    }

    public Map<String, MovingEntityHook> getHooks() {
        return hooks == null ? hooks = new Hashtable<>() : hooks;
    }

    public void addHook(String key, MovingEntityHook hook) {
        this.getHooks().put(key, hook);
    }

    public void removeHook(String key) {
        this.getHooks().remove(key);
    }

    @Override
    protected float getGravity() {
        return 0.08f;
        //return 0.092f;
    }

    public Entity getHate() {
        return hate;
    }

    public void setHate(Entity hate) {
        this.hate = hate;
    }

    public void jump() {
        if (this.onGround) {
            this.motionY = Math.sqrt(2 * getJumpHeight() * getGravity());
        }
    }

    @Override
    public boolean onUpdate(int currentTick) {
        super.onUpdate(currentTick);
        return true;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (this.closed) {
            return false;
        }

        this.getHooks().values().forEach(hook -> {
            if (hook.shouldExecute())   {
                hook.onUpdate(this.server.getTick());
            }
        });
        if (this.task != null) this.task.onUpdate(Server.getInstance().getTick());

        boolean hasUpdate = super.entityBaseTick(tickDiff);

        if (this.isKnockback) {                   // knockback 이 true 인 경우는 맞은 직후

        } else if (this.routeLeading && this.onGround) {
            this.motionX = this.motionZ = 0;
        }

        this.motionX *= (1 - this.getDrag());
        this.motionZ *= (1 - this.getDrag());

        if (this.targetAI != null) this.targetAI.onUpdate();

        if (this.route.isSearching() && System.currentTimeMillis() - this.route.stopRouteFindUntil > 1000) {
            this.route.forceStop();
        }

        if (this.routeLeading && this.onGround && this.hasSetTarget() && !this.route.isSearching() && System.currentTimeMillis() >= this.route.stopRouteFindUntil && (this.route.getDestination() == null || this.route.getDestination().distance(this.getTarget()) > 2)) { // 대상이 이동함
            Server.getInstance().getScheduler().scheduleAsyncTask(new RouteFinderSearchAsyncTask(this.route, this.level, this, this.getTarget(), this.boundingBox));

			/*if(this.route.isSearching()) this.route.reSearch();
            else this.route.search();*/

            hasUpdate = true;
        }

        if (!this.isImmobile()) {
            if (this.routeLeading && !this.isKnockback && !this.route.isSearching() && this.route.isSuccess() && this.route.hasRoute()) { // entity has route to go
                hasUpdate = true;

                Node node = this.route.get();
                if (node != null) {
                    //level.addParticle(new cn.nukkit.level.particle.RedstoneParticle(node.getVector3(), 2));
                    Vector3 vec = node.clone();
                    double diffX = Math.pow(vec.x - this.x, 2);
                    double diffZ = Math.pow(vec.z - this.z, 2);

                    if (diffX + diffZ == 0) {
                        if (!this.route.next()) {
                            this.route.arrived();
                            //Server.getInstance().getLogger().warning(vec.toString());
                        }
                    } else {
                        int negX = vec.x - this.x < 0 ? -1 : 1;
                        int negZ = vec.z - this.z < 0 ? -1 : 1;

                        this.motionX = Math.min(Math.abs(vec.x - this.x), diffX / (diffX + diffZ) * this.getMovementSpeed()) * negX;
                        this.motionZ = Math.min(Math.abs(vec.z - this.z), diffZ / (diffX + diffZ) * this.getMovementSpeed()) * negZ;
                        if (this.lookAtFront) {
                            double angle = Math.atan2(vec.z - this.z, vec.x - this.x);
                            this.setRotation((angle * 180) / Math.PI - 90, 0);
                        }
                    }
                }
            }

            for (Entity entity : this.getLevel().getCollidingEntities(this.boundingBox)) {
                if (this.canCollide() && this.canCollideWith(entity)) {
                    Vector3 motion = this.subtract(entity);
                    this.motionX += motion.x / 2;
                    this.motionZ += motion.z / 2;
                }
            }

            if ((this.motionX != 0 || this.motionZ != 0) && this.isCollidedHorizontally) {
                this.jump();
            }
            this.move(this.motionX, this.motionY, this.motionZ);

            this.checkGround();
            if (!this.onGround) {
                this.motionY -= this.getGravity();
                //Server.getInstance().getLogger().warning(this.getId() + ": 不在地面, 掉落 motionY=" + this.motionY);
                hasUpdate = true;
            } else {
                this.isKnockback = false;
            }
        }


        return hasUpdate;
    }

    public double getRange() {
        return 100.0;
    }

    public void setTarget(Vector3 vec, String identifier) {
        this.setTarget(vec, identifier, false);
    }

    public void setTarget(Vector3 vec, String identifier, boolean forceSearch) {
        if (identifier == null) return;

        if (forceSearch || !this.hasSetTarget() || identifier.equals(this.targetSetter)) {
            this.target = vec;

            this.targetSetter = identifier;
        }

        if (this.hasSetTarget() && (forceSearch || !this.route.hasRoute())) {
            this.route.forceStop();
            Server.getInstance().getScheduler().scheduleAsyncTask(new RouteFinderSearchAsyncTask(this.route, this.level, this, this.getTarget(), this.boundingBox.clone()));
			/*if(this.route.isSearching()) this.route.reSearch();
			else this.route.search();*/
        }
    }

    public boolean isCurrentIdentifier(String identifier)   {
        return identifier != null && identifier.equals(this.targetSetter);
    }

    public boolean hasReachedTarget() {
        return this.route.hasArrived();
    }

    public Vector3 getRealTarget() {
        return this.target;
    }

    public Vector3 getTarget() {
        return new Vector3(this.target.x, this.target.y, this.target.z);
    }

    /**
     * Returns whether the entity has following target
     * Entity will try to move to position where target exists
     */
    public boolean hasFollowingTarget() {
        return this.route.getDestination() != null && this.target != null && this.distanceSquared(this.target) < this.getRange();
    }

    /**
     * Returns whether the entity has set its target
     * The entity may not follow the target if there is following target and set target is different
     * If following distance of target is too far to follow or cannot reach, set target will be the next following target
     */
    public boolean hasSetTarget() {
        return this.target != null && this.distanceSquared(this.target) < this.getRange();
    }

    @Override
    protected void checkGroundState(double movX, double movY, double movZ, double dx, double dy, double dz) {
        this.isCollidedVertically = movY != dy;
        this.isCollidedHorizontally = (movX != dx || movZ != dz);
        this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);

        // this.onGround = (movY != dy && movY < 0);
        // onGround 는 onUpdate 에서 확인
    }

    private void checkGround() {
        AxisAlignedBB[] list = this.level.getCollisionCubes(this, this.level.getTickRate() > 1 ? this.boundingBox.getOffsetBoundingBox(0, -1, 0) : this.boundingBox.addCoord(0, -1, 0), false);

        double maxY = 0;
        for (AxisAlignedBB bb : list) {
            if (bb.getMaxY() > maxY) {
                maxY = bb.getMaxY();
            }
        }

        this.onGround = (maxY == this.boundingBox.getMinY());
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        UpdateAttributesPacket pk0 = new UpdateAttributesPacket();
        pk0.entityId = this.getId();
        pk0.entries = new Attribute[]{
                Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(this.getMaxHealth()).setValue(this.getHealth()),
        };
        this.getLevel().addChunkPacket(this.chunk.getX(), this.chunk.getZ(), pk0);
    }

    @Override
    public void setMaxHealth(int maxHealth) {
        super.setMaxHealth(maxHealth);
        if (this.getHealth() > maxHealth) this.health = maxHealth;
        UpdateAttributesPacket pk0 = new UpdateAttributesPacket();
        pk0.entityId = this.getId();
        pk0.entries = new Attribute[]{
                Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(this.getMaxHealth()).setValue(this.getHealth()),
        };
        this.getLevel().addChunkPacket(this.chunk.getX(), this.chunk.getZ(), pk0);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI);
    }

    @Override
    public void knockBack(Entity attacker, double damage, double x, double z, double base) {
        this.isKnockback = true;

        super.knockBack(attacker, damage, x, z, base / 2);
    }

    public PathFinder getRoute() {
        return route;
    }

    public void setRoute(PathFinder route) {
        this.route = route;
    }

    public TargetAI getTargetAI() {
        return targetAI;
    }

    public void setTargetAI(TargetAI targetAI) {
        this.targetAI = targetAI;
    }

    public void updateBotTask(MovingEntityTask task) {
        if (this.task != null) this.task.forceStop();
        this.task = task;
        if (task != null) this.task.onUpdate(Server.getInstance().getTick());
    }

    public MovingEntityTask getTask() {
        return task;
    }

    public boolean isLookAtFront() {
        return lookAtFront;
    }

    public void setLookAtFront(boolean lookAtFront) {
        this.lookAtFront = lookAtFront;
    }

    public double getJumpHeight() {
        return 1.25;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        this.getHooks().values().forEach(hook -> hook.onDamage(source));
        return super.attack(source);
    }

    @Override
    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = this.getNetworkId();
        pk.entityUniqueId = this.getId();
        pk.entityRuntimeId = this.getId();
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.speedX = (float) this.motionX;
        pk.speedY = (float) this.motionY;
        pk.speedZ = (float) this.motionZ;
        pk.metadata = this.dataProperties;
        player.dataPacket(pk);

        UpdateAttributesPacket pk0 = new UpdateAttributesPacket();
        pk0.entityId = this.getId();
        pk0.entries = new Attribute[]{
                Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(this.getMaxHealth()).setValue(this.getHealth()),
        };
        player.dataPacket(pk0);

        super.spawnTo(player);
    }

    @Override
    public void kill() {
        super.kill();

        this.level.dropExpOrb(this, getXp(), new Vector3(
                Utils.rand(-16, 17) / 32,
                Utils.rand(-16, 17) / 32,
                Utils.rand(-16, 17) / 32
        ));
    }

    public abstract int getXp();
}

