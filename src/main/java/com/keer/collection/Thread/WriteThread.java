package com.keer.collection.Thread;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class WriteThread extends Thread{

    private String sourceFile;
    private String targetFile;
    private String threadName;

    public WriteThread(String sourceFile,String targetFile,String ThreadName){
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
        this.threadName = ThreadName;
    }

    @Override
    public void run(){
        RandomAccessFile raf = null;
        FileChannel fc = null;
        FileLock fl = null;
        FileInputStream in = null;
        try {
            raf = new RandomAccessFile(targetFile, "rw");
            fc = raf.getChannel();
            while(true){
                try {
                    fl = fc.tryLock();
                    System.out.println(fl.isShared());
                    System.out.println(this.threadName+" : get the lock");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                } catch (Exception e) {
                    System.out.println(this.threadName+" is block");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

            }
            in = new FileInputStream(sourceFile);
            byte[] b = new byte[1024];
            int len = 0;
            ByteBuffer bb = ByteBuffer.allocate(1024);
            while((len=in.read(b))!=-1){
                bb.clear();
                bb.put(b, 0, len);
                bb.flip();
                fc.write(bb);

            }
            System.out.println(this.threadName+" : write success");
            fl.release();
            System.out.println(this.threadName+" : release lock");
            raf.close();
            fc.close();
            in.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            System.out.println(this.threadName+" : write failed");
        }finally{
            if(fl!=null&&fl.isValid()){
                try {
                    fl.release();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
