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

import cn.nukkit.InterruptibleThread;
import cn.nukkit.Server;
import cn.nukkit.utils.MainLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author CreeperFace
 */
public class TaskWatchDog extends Thread implements InterruptibleThread {

    public static List<RouteFinderSearchAsyncTask> tasks = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void run() {
        Server server = Server.getInstance();

        while (server.isRunning()) {
            while (!tasks.isEmpty()) {
                try {
                    RouteFinderSearchAsyncTask task = tasks.get(0);
                    long time = System.currentTimeMillis();

                    if (time - task.started > 1000 && task.thread != null) {
                        task.thread.interrupt();
                        tasks.remove(0);
                    } else if (task.isFinished()) {
                        tasks.remove(0);
                    }
                } catch (Throwable t) {
                    MainLogger.getLogger().error("", t);
                }
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }
}
