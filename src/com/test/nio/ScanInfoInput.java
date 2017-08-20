package com.test.nio;

import java.util.concurrent.BlockingQueue;

import com.test.nio.Client.ScanInfo;

import static com.test.nio.Main.THREAD_NUM;

/**
 * Created by luairan on 2017/6/30.
 *
 * @author luairan
 * @date 2017/06/30
 */
public class ScanInfoInput implements Runnable {
    private final BlockingQueue<ScanInfo> scanInfoBlockingQueue;

    public ScanInfoInput(BlockingQueue<ScanInfo> scanInfoBlockingQueue) {
        this.scanInfoBlockingQueue = scanInfoBlockingQueue;
    }

    @Override
    public void run() {
        try {

            for (int i = 1; i < 65535; i++) {
                scanInfoBlockingQueue.put(new ScanInfo("www.baidu.com", i, false));
            }

            for (int i = 0; i < THREAD_NUM; i++) {
                scanInfoBlockingQueue.put(new ScanInfo("127.0.0.1", i, true));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
