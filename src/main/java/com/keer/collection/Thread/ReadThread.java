package com.keer.collection.Thread;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class ReadThread extends Thread{

    private String sourceFile;
    private String threadName;

    public ReadThread(String sourceFile,String threadName){
        this.sourceFile = sourceFile;
        this.threadName = threadName;
    }

    @Override
    public void run(){
        RandomAccessFile raf = null;
        FileChannel fc = null;
        FileLock fl = null;
        try {
            raf = new RandomAccessFile(sourceFile, "rw");
            fc = raf.getChannel();
            while(true){
                try {
                    fl = fc.tryLock(0,Long.MAX_VALUE,true);
                    System.out.println(fl.isShared());
                    System.out.println(this.threadName+" : get the lock");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    break;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    System.out.println(this.threadName+" is block");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

            }
            StringBuffer sb = new StringBuffer();
            sb.append("Read from "+this.threadName+"：");
            ByteBuffer bb = ByteBuffer.allocate(1024);
            while((fc.read(bb))!=-1){
                //sb.append(new String(bb.array()));
                //System.out.println(new String(bb.array()));
                bb.clear();
            }
            System.out.println(this.threadName+" : read success!");
            fl.release();
            System.out.println(this.threadName+" : release lock");
            raf.close();
            fc.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        new WriteThread("./test.json", "./test.json", "write_thread-1").start();
//        new WriteThread("./test.json", "./test.json", "write_thread-2").start();

        new ReadThread("./test.json", "read_thread-1").start();
        new ReadThread("./test.json", "read_thread-2").start();

    }
}

