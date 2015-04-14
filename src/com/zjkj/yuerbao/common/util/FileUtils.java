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
 * @TODO ���������ļ�
 */
public class FileUtils {
	private static YuerbaoProperties yp = null;// ����������

	private static int down_step = 5;// ��ʾstep
	private static int TIMEOUT = 50 * 1000;// ���س�ʱ
	private static int totalSize;// �ļ��ܴ�С
	private static int downloadCount = 0;// �Ѿ����غõĴ�С
	private static int updateCount = 0;// �Ѿ��ϴ����ļ���С
	private static byte buffer[] = new byte[1024];// ��ȡ�ļ��ĳ���;

	private static URL url = null;
	private static HttpURLConnection httpURLConnection = null;

	private static InputStream inputStream;
	private static OutputStream outputStream;
	private static File updateDir = null;// ����Ŀ¼
	private static File updateFile = null;// ������ļ���
	private static File sdCard = null;// �ⲿ�洢Ŀ¼

	static {

		// ��ʼ��������
		yp = YuerbaoProperties.getInstance();
		// ��ȡ�ⲿ�洢�豸�ĵ�ǰ״̬�ж��ڴ濨�Ƿ����
		sdCard = Environment.getExternalStorageDirectory();
	}

	/**
	 * @param name
	 *            apk����
	 * 
	 * @TODO ����APKĿ¼���ļ�
	 */
	public static File createApkFile(String name) {

		// ��ȡ�ⲿ�洢�豸�ĵ�ǰ״̬�ж��ڴ濨�Ƿ����
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			// ��ȡ�ⲿ�洢Ŀ¼(sdcard)
			updateDir = new File(sdCard + yp.getApkDir());
			updateFile = new File(updateDir + "/" + name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();// ����Ŀ¼
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();// �����ļ�
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
	 *            ����ͼƬ������ splash.jpg/splash.png
	 * 
	 * @return ����ͼƬ���ڵ�·�� yuerbao/splash/splash.jpg
	 */
	public static String createSplashFile() {

		// ��ȡ�ⲿ�洢�豸�ĵ�ǰ״̬�ж��ڴ濨�Ƿ����
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			updateDir = new File(sdCard + yp.getSplashDir());
			updateFile = new File(sdCard + yp.getSplashFile());

			if (!updateDir.exists()) {
				updateDir.mkdirs();// ����Ŀ¼
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();// �����ļ�
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
	 *            �������˵�·��
	 * @return ������
	 * @throws Exception
	 */
	public static InputStream getInputStream(String urlPath) throws Exception {

		url = new URL(urlPath);
		httpURLConnection = (HttpURLConnection) url.openConnection();

		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);

		// ��ȡ�����ļ���size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("����ʧ�ܣ�����������������");
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
			throw new Exception("����ʧ�ܣ�����������������");
		}

		return httpURLConnection;
	}

	/**
	 * 
	 * @param in
	 *            ������
	 * @param out
	 *            �����
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
	 *            Զ�̵�ַ
	 * @param file
	 *            ����ļ�
	 * @throws IOException
	 */
	public static void write(String urlPath, File file) throws Exception {
		inputStream = getInputStream(urlPath);// ��ȡ������
		outputStream = new FileOutputStream(file, false);// �ļ������򸲸ǵ�
		write(inputStream, outputStream);
	}

	/**
	 * 
	 * @param urlPath
	 *            Զ�̵�ַ
	 * @param filePath
	 *            ���Ŀ¼
	 * @throws IOException
	 */
	public static void write(String urlPath, String filePath) throws Exception {
		inputStream = getInputStream(urlPath);// ��ȡ������
		outputStream = new FileOutputStream(filePath, false);// �ļ������򸲸ǵ�
		write(inputStream, outputStream);
	}

	/**
	 * �ر������
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
	 * �ر������
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
	 * ������������ͼƬ
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
	 * ��ȡ����ͼƬ��Դ
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String urlPath) {
		Bitmap bitmap = null;
		try {
			InputStream is = getInputStream(urlPath);
			// �����õ�ͼƬ
			bitmap = BitmapFactory.decodeStream(is);
			// �ر�������
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

		// ��ȡ�����ļ���size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("����ʧ�ܣ�����������������");
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
