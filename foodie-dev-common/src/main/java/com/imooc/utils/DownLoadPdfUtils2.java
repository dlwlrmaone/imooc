package com.imooc.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadPdfUtils2 {

	public static void downLoadByUrl(String urlStr, String fileName,
			String savePath) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//设置连接时间
		conn.setConnectTimeout(3 * 1000);
		//设置请求属性
		conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		InputStream inputStream = conn.getInputStream();
		byte[] getData = readInputStream(inputStream);
		// �ļ�����λ��
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
		System.out.println("info:" + url + " download success");
 
	}

	public static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	public static void main(String[] args) {
		
		String policyno = "370004842398088" ;
		int count = 212 ;
		
//		int i=30;
		String[] ii = {"212"};
		String floder = "/Users/dlwlrmaone/Desktop/pdf/";
		try {
			for (int j = 0; j < ii.length; j++) {

				downLoadByUrl("http://10.135.1.59:9600/capolicy/downloadpdf?PolicyCode="+ policyno +"&PDFTempType=3&InsuredNum="
						+ ii[j] , policyno+"_" + ii[j] + ".pdf", floder);
			}
		} catch (Exception e) {
			System.out.println("���س����쳣��" + e);
		}
//		for (int i = 42; i <=count ; i++) {
//			try {
//				downLoadByUrl("http://10.135.1.59:9600/capolicy/downloadpdf?PolicyCode="+ policyno +"&PDFTempType=3&InsuredNum="
//						+ i , policyno+"_" + i + ".pdf", floder);
//			} catch (Exception e) {
//				System.out.println("报错啦：" + e);
//			}
//
//		}
	}
}
