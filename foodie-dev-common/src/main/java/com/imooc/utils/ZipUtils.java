package com.imooc.utils;

import java.io.*;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    
    public static String fileToZip(String sourceFilePath,String zipFilePath,String fileName){
        File sourceFile = new File(sourceFilePath);
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream;
        ZipOutputStream zipOutputStream = null;
        //判断源文件是否存在
        if (!sourceFile.exists()){
            System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");
        }
        File zipFile = new File(zipFilePath + "/" + fileName +".zip");
        //判断压缩后文件是否会重复
        if(zipFile.exists()){
            System.out.println(zipFilePath + "目录下已存在名字为" + fileName +".zip" +"的文件");
        }
        //每次生成压缩包之前先删除原有的压缩包
        zipFile.delete();
        //获取源文件夹下的所有文件
        File[] sourceFiles = sourceFile.listFiles();
        if(sourceFiles == null || sourceFiles.length == 0){
            System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
        }
        //先删除之前的压缩包文件
        for (int i = 0; i <=sourceFiles.length; i++) {
            if(sourceFiles[i].getName().indexOf(".zip")!=-1){
                File zipFile2 = new File(sourceFiles[i].getPath());
                zipFile2.delete();
                break;
            }
        }
        //重新获取文件夹所以文件
        sourceFiles = sourceFile.listFiles();
        try {
            fileOutputStream = new FileOutputStream(zipFile);
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
            byte[] bufs = new byte[1024*10];
            for(int i=0; i<sourceFiles.length; i++){
                //创建ZIP实体，并添加进压缩包
                ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                zipOutputStream.putNextEntry(zipEntry);
                //读取待压缩的文件并写进压缩包里
                fileInputStream = new FileInputStream(sourceFiles[i]);
                bufferedInputStream = new BufferedInputStream(fileInputStream, 1024*10);
                int read = 0;
                while((read=bufferedInputStream.read(bufs, 0, 1024*10)) != -1){
                    zipOutputStream.write(bufs,0,read);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(null != bufferedInputStream) bufferedInputStream.close();
                if(null != zipOutputStream) zipOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName + ".zip";
    }

    public static void main(String[] args) {
        String sourceFilePath = "/Users/dlwlrmaone/Desktop/pdf/";
        String zipFilePath = "/Users/dlwlrmaone/Desktop/zip/";
        String fileName = DateUtil.dateToString(new Date(),"yyyy-MM-dd-HHmmss");
        String fn = ZipUtils.fileToZip(sourceFilePath, zipFilePath, fileName);
        System.out.println(fn);
    }
}
