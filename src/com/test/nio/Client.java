package com.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Client extends Thread {
    private final BlockingQueue<ScanInfo> scanInfoBlockingQueue;
    //private final Queue<CheckedInfo> handlingQuene = new LinkedList<CheckedInfo>();
    private Selector selector;
    //private int maxSize = 2;
    private AtomicInteger off;

    public Client(BlockingQueue<ScanInfo> scanInfoBlockingQueue, Selector selector,
                  AtomicInteger off) throws IOException {
        this.scanInfoBlockingQueue = scanInfoBlockingQueue;

        this.selector = selector;

        this.off = off;
    }

    //public static void main(String[] args) {
    //    //种多个线程发起Socket客户端连接请求
    //        //Client c = new Client();
    //        //c.init();
    //        new Thread(new Client()).start();
    //
    //}
    //
    @Override
    public void run() {
        while (true) {
            try {
                //while (handlingQuene.size() <= maxSize) {
                ScanInfo scanInfo = scanInfoBlockingQueue.take();
                if (scanInfo.endFlag) {
                    System.out.println("Client end !" + Thread.currentThread().getName());
                    off.getAndIncrement();
                    break;
                }
                add(scanInfo);
                //testScanInfo();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void add(ScanInfo scanInfo) {
        SocketChannel channel = null;
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            //请求连接
            channel.connect(new InetSocketAddress(scanInfo.ip, scanInfo.port));
            channel.register(selector, SelectionKey.OP_CONNECT);
            //handlingQuene.add(new CheckedInfo(channel,scanInfo));

        } catch (IOException e) {
            e.printStackTrace();
        }
        //testScanInfo(scanInfo);
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