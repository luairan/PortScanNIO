package com.test.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static com.test.nio.Main.THREAD_NUM;

public class SelectorHander extends Thread {
    private Selector selector;

    private AtomicInteger count;

    public SelectorHander(Selector selector, AtomicInteger count) {
        this.selector = selector;
        this.count = count;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (count.get() == THREAD_NUM) {
                    System.out.println("finish port scan");
                    break;
                }
                testScanInfo();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void testScanInfo() {
        SocketChannel channel = null;
        try {
            //boolean isOver = false;
            //while (!isOver) {
            int readyChannels = selector.select(1000);
            //if(readyChannels == 0) continue;

            System.out.println(readyChannels);
            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                ite.remove();
                if (key.isConnectable()) {
                    channel = (SocketChannel)key.channel();
                    if (channel.isConnectionPending()) {
                        try {
                            if (channel.finishConnect()) {
                                System.out.println(channel.getRemoteAddress().toString() + " ok");
                            } else {
                                key.cancel();
                                System.out.println(channel.getRemoteAddress().toString() + " failed");
                            }

                        } catch (Throwable e) {
                            System.out.println(channel.toString() + " failed" + Thread.currentThread().getName());
                            //e.printStackTrace();
                        }

                    }
                }
            }
            //}
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static class ScanInfo {
        public String ip;
        public int port;

        public boolean endFlag;

        public ScanInfo(String ip, int port, boolean endFlag) {
            this.ip = ip;
            this.port = port;
            this.endFlag = endFlag;
        }
    }
}