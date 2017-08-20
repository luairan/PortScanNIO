package com.test.nio;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import com.test.nio.Client.ScanInfo;

/**
 * Created by luairan on 2017/6/30.
 *
 * @author luairan
 * @date 2017/06/30
 */
public class Main {
    public static final int THREAD_NUM = 400;

    //private final static ExecutorService executorService = Executors.newFixedThreadPool(20);

    private final static BlockingQueue<ScanInfo> scanInfoBlockingQueue = new LinkedBlockingDeque<ScanInfo>(200);

    public static void main(String[] args) throws IOException {

        AtomicInteger count = new AtomicInteger(0);
        new Thread(new ScanInfoInput(scanInfoBlockingQueue)).start();
        Selector selector = Selector.open();
        new Thread(new SelectorHander(selector, count)).start();
        for (int i = 0; i < THREAD_NUM; i++) {
            new Client(scanInfoBlockingQueue, selector, count).start();
        }

    }
}
