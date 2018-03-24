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

package me.onebone.actaeon.runnable;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.AsyncTask;
import me.onebone.actaeon.path.PathFinder;

/**
 * RouteFinderSearchAsyncTask
 * ===============
 * author: boybook
 * EaseCation Network Project
 * nukkit
 * ===============
 */
public class RouteFinderSearchAsyncTask extends AsyncTask {

    public Thread thread;
    public long started;
    private PathFinder route;
    private int retryTimes = 0;
    private Level level = null;
    private Vector3 start = null;
    private Vector3 dest = null;

    /*public RouteFinderSearchAsyncTask(PathFinder route) {
        this(route, null, null, null, null);
    }*/
    private AxisAlignedBB bb = null;

    public RouteFinderSearchAsyncTask(PathFinder route, Level level, Vector3 start, Vector3 dest, AxisAlignedBB bb) {
        this.route = route;
        this.level = level;
        this.start = start.clone();
        this.dest = dest.clone();
        this.bb = bb.clone();
    }

    @Override
    public void onRun() {
        started = System.currentTimeMillis();
        TaskWatchDog.tasks.add(this);
        thread = Thread.currentThread();

        while (this.retryTimes < 100) {
            if (!this.route.isSearching()) {
                if (this.level != null) this.route.setPositions(this.level, this.start, this.dest, this.bb);
                this.route.search();
                //Server.getInstance().getLogger().notice("异步寻路线程-" + this.getTaskId() + " 开始寻路");
                return;
            } else {
                this.retryTimes++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
        }
        Server.getInstance().getLogger().warning("异步寻路线程-" + this.getTaskId() + " 超过等待限制");
        this.route.forceStop();

    }
}
