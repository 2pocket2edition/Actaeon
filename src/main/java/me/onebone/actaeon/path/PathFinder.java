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

package me.onebone.actaeon.path;

import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3;
import me.onebone.actaeon.entity.heirachy.MovingEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class PathFinder {
    public long stopRouteFindUntil = System.currentTimeMillis();
    public volatile Thread thread;
    protected Vector3 destination = null, start = null;
    protected List<Node> nodes = new ArrayList<>();
    protected Level level = null;
    protected AxisAlignedBB aabb = null;

    protected MovingEntity entity = null;

    protected boolean forceStop = false;
    private int current = 0;
    private boolean arrived = false;

    public PathFinder(MovingEntity entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null");

        this.entity = entity;
    }

    public MovingEntity getEntity() {
        return this.entity;
    }

    public void setPositions(Level level, Vector3 start, Vector3 dest, AxisAlignedBB bb) {
        this.setLevel(level);
        this.setStart(start);
        this.setDestination(dest);
        this.setBoundingBox(bb);
    }

    public Vector3 getStart() {
        if (start == null) return null;

        return new Vector3(start.x, start.y, start.z);
    }

    public void setStart(Vector3 start) {
        if (start == null) throw new IllegalArgumentException("Cannot set start as null");

        this.start = new Vector3(start.x, start.y, start.z);
    }

    public Vector3 getDestination() {
        if (destination == null) return null;

        return new Vector3(destination.x, destination.y, destination.z);
    }

    public void setDestination(Vector3 destination) {
        if (destination == null) {
            this.destination = null;
            return;
        }

        this.destination = new Vector3(destination.x, destination.y, destination.z);
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        if (level == null) throw new IllegalArgumentException("Level cannot be null");

        this.level = level;
    }

    public AxisAlignedBB getBoundingBox() {
        if (this.aabb == null) return new SimpleAxisAlignedBB(0, 0, 0, 0, 0, 0);

        return this.aabb.clone();
    }

    public void setBoundingBox(AxisAlignedBB bb) {
        if (bb == null) {
            this.aabb = new SimpleAxisAlignedBB(0, 0, 0, 0, 0, 0);
        }

        this.aabb = bb;
    }

    protected void resetNodes() {
        this.nodes.clear();
        this.arrived = false;
        this.current = 0;
    }

    public void forceStop() {
        this.forceStop = true;
        /*if(this.thread != null) {
            this.thread.interrupt();
            //this.thread.stop();
            this.thread = null;
        }*/

        if (!this.isSearching()) {
            this.forceStop = false;
            this.resetNodes();
        }
    }

    protected void addNode(Node node) {
        this.nodes.add(node);
    }

    /**
     * @return true if it has next node to go
     */
    public boolean hasNext() {
        if (nodes.size() == 0) throw new IllegalStateException("There is no path found");

        return !this.arrived && nodes.size() > this.current + 1;
    }

    /**
     * Move to next node
     *
     * @return true if succeed
     */
    public boolean next() {
        if (nodes.size() == 0) throw new IllegalStateException("There is no path found");

        if (this.hasNext()) {
            this.current++;
            return true;
        }
        return false;
    }

    /**
     * Returns if the entity has reached the node
     *
     * @return true if reached
     */
    public boolean hasReachedNode(Vector3 vec) {
        Vector3 cur = this.get();

		/*return NukkitMath.floorDouble(vec.x) ==  NukkitMath.floorDouble(cur.x)
                && NukkitMath.floorDouble(vec.y) == NukkitMath.floorDouble(cur.y)
				&& NukkitMath.floorDouble(vec.z) == NukkitMath.floorDouble(cur.z);*/
        return vec.x == cur.x
                //&& vec.y == cur.y
                && vec.z == cur.z;
    }

    /**
     * Gets node of current
     *
     * @return current node
     */
    public Node get() {
        if (nodes.size() == 0) throw new IllegalStateException("There is no path found.");

        if (this.arrived) return null;
        return nodes.get(current);
    }

    public void arrived() {
        this.current = 0;
        this.arrived = true;
    }

    public boolean hasRoute() {
        return this.nodes.size() > 0;
    }

    public boolean hasArrived() {
        return this.arrived;
    }

    /**
     * Search for route
     *
     * @return true if finding path is done. It also returns true even if there is no route.
     */
    public abstract boolean search();

    /**
     * Re-search route to destination
     *
     * @return true if finding path is done.
     */
    public abstract boolean reSearch();

    /**
     * @return true if searching is not end
     */
    public abstract boolean isSearching();

    /**
     * @return true if finding route was success
     */
    public abstract boolean isSuccess();
}
