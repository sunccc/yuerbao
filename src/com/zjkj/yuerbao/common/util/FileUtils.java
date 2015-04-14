package com.zjkj.yuerbao.common.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import com.zjkj.yuerbao.R;

/**
 * 
 * @author Gxy
 *
 * @TODO 创建下载文件
 */
public class FileUtils {
	private static YuerbaoProperties yp = null;// 属性名件类

	private static int down_step = 5;// 提示step
	private static int TIMEOUT = 50 * 1000;// 下载超时
	private static int totalSize;// 文件总大小
	private static int downloadCount = 0;// 已经下载好的大小
	private static int updateCount = 0;// 已经上传的文件大小
	private static byte buffer[] = new byte[1024];// 读取文件的长度;

	private static URL url = null;
	private static HttpURLConnection httpURLConnection = null;

	private static InputStream inputStream;
	private static OutputStream outputStream;
	private static File updateDir = null;// 保存目录
	private static File updateFile = null;// 保存的文件名
	private static File sdCard = null;// 外部存储目录

	static {

		// 初始化属性类
		yp = YuerbaoProperties.getInstance();
		// 获取外部存储设备的当前状态判断内存卡是否存在
		sdCard = Environment.getExternalStorageDirectory();
	}

	/**
	 * @param name
	 *            apk名称
	 * 
	 * @TODO 创建APK目录与文件
	 */
	public static File createApkFile(String name) {

		// 获取外部存储设备的当前状态判断内存卡是否存在
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			// 获取外部存储目录(sdcard)
			updateDir = new File(sdCard + yp.getApkDir());
			updateFile = new File(updateDir + "/" + name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();// 创建目录
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();// 创建文件
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return updateFile;
	}

	/**
	 * 
	 * @param name
	 *            启动图片的名称 splash.jpg/splash.png
	 * 
	 * @return 启动图片所在的路径 yuerbao/splash/splash.jpg
	 */
	public static String createSplashFile() {

		// 获取外部存储设备的当前状态判断内存卡是否存在
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			updateDir = new File(sdCard + yp.getSplashDir());
			updateFile = new File(sdCard + yp.getSplashFile());

			if (!updateDir.exists()) {
				updateDir.mkdirs();// 创建目录
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();// 创建文件
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return updateFile.toString();
	}

	/**
	 * 
	 * @param urlPath
	 *            服务器端的路径
	 * @return 输入流
	 * @throws Exception
	 */
	public static InputStream getInputStream(String urlPath) throws Exception {

		url = new URL(urlPath);
		httpURLConnection = (HttpURLConnection) url.openConnection();

		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);

		// 获取下载文件的size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("连接失败，请检查您的网络设置");
		}
		return httpURLConnection.getInputStream();
	}

	public static HttpURLConnection getHttpURLConnection(String urlPath)
			throws Exception {

		url = new URL(urlPath);
		httpURLConnection = (HttpURLConnection) url.openConnection();

		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);

		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("连接失败，请检查您的网络设置");
		}

		return httpURLConnection;
	}

	/**
	 * 
	 * @param in
	 *            输入流
	 * @param out
	 *            输出流
	 * @throws IOException
	 */
	public static void write(InputStream in, OutputStream out)
			throws IOException {

		int readsize = 0;
		long downSize = 0;

		while ((readsize = in.read(buffer)) != -1) {
			downSize += readsize;
			out.write(buffer, 0, readsize);
		}
		closeOutputStream();
	}

	/**
	 * 
	 * @param urlPath
	 *            远程地址
	 * @param file
	 *            输出文件
	 * @throws IOException
	 */
	public static void write(String urlPath, File file) throws Exception {
		inputStream = getInputStream(urlPath);// 获取输入流
		outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
		write(inputStream, outputStream);
	}

	/**
	 * 
	 * @param urlPath
	 *            远程地址
	 * @param filePath
	 *            输出目录
	 * @throws IOException
	 */
	public static void write(String urlPath, String filePath) throws Exception {
		inputStream = getInputStream(urlPath);// 获取输入流
		outputStream = new FileOutputStream(filePath, false);// 文件存在则覆盖掉
		write(inputStream, outputStream);
	}

	/**
	 * 关闭输出流
	 * 
	 * @throws IOException
	 */
	public static void closeOutputStream() throws IOException {
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		outputStream.flush();
		inputStream.close();
		outputStream.close();
	}

	/**
	 * 关闭输出流
	 * 
	 * @throws IOException
	 */
	public static void closeOutputStream(InputStream inputStream,
			OutputStream outputStream) throws IOException {
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		outputStream.flush();
		inputStream.close();
		outputStream.close();
	}

	/**
	 * 下载启动背景图片
	 */
	public static void downloadSplashFile() throws Exception {

		write(yp.getLoadSplashFileUrl(), createSplashFile());
	}

	public static byte[] inputStreamToByte(String urlPath) {
		try {
			InputStream is = getInputStream(urlPath);
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static JSONObject inputStreamToJson(String urlPath) {

		JSONObject obj = null;
		try {
			obj = new JSONObject(inputStreamToString(urlPath));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	public static String inputStreamToString(String urlPath) {
		try {
			InputStream is = getInputStream(urlPath);
			OutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			String str = bytestream.toString();
			bytestream.close();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取网落图片资源
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String urlPath) {
		Bitmap bitmap = null;
		try {
			InputStream is = getInputStream(urlPath);
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;

	}

	public static InputStream getInputStreamByPost(String urlPath)
			throws Exception {

		url = new URL(urlPath);
		httpURLConnection = (HttpURLConnection) url.openConnection();

		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);

		// 获取下载文件的size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("连接失败，请检查您的网络设置");
		}
		return httpURLConnection.getInputStream();
	}

	public static String inputStreamToStringByPost(String urlPath) {
		try {
			InputStream is = getInputStreamByPost(urlPath);
			OutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			String str = bytestream.toString();
			bytestream.close();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
