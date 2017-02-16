/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Raef M
 */
public class BlockingQueue {

    private final Queue<File> q = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();

    public void put(File[] fs) throws Exception {
        lock.lock();
        try {
            q.addAll(Arrays.asList(fs));
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public void put(File fs) throws Exception {
        lock.lock();
        try {
            q.add(fs);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    

    public File take() throws InterruptedException {
        lock.lock();
        try {
            while (q.isEmpty()) {
                notEmpty.await();
            }

            File item = q.remove();
            return item;
        } finally {
            lock.unlock();
        }
    }

}
