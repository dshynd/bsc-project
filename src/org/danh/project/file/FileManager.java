package org.danh.project.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.danh.project.image.ImageManager;
import org.danh.project.user.usertypes.IUser;

public class FileManager {
	private static FileManager instance = null;
	private static final transient String KEYSTRING = "79c14f6ad97b80224f261f9cb9cf791a";
	static final String IUserDATA_FILENAME = "IUsers.ser";
	public static final String IMAGE_FILENAME = "img.png";
	public static final String PREPENDPATH = "temp" + File.separator;
	public static final String APPENDPATH = File.separator + "img" + File.separator;
	String root = "";
	Map<Long, Lock> locks = Collections.synchronizedMap(new HashMap<Long, Lock>());

	private FileManager(String root) {
		if (root.endsWith(File.separator)) {
			this.root = root;
		} else {
			this.root = (root + File.separator);
		}
	}

	public static FileManager getInstance() throws NotInitialisedException {
		if (instance == null) {
			throw new NotInitialisedException();
		}
		return instance;
	}

	public static void init(String root) {
		if (instance == null) {
			instance = new FileManager(root);
		}
	}

	public String writeImage(long canvasId, BufferedImage image) throws IOException {
		Lock lock = getLock(canvasId);
		lock.lock();
		try {
			String path = buildCanvasPath(canvasId);
			File folder = new File(this.root + path);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File file = new File(folder, "img.png");
			ImageIO.write(image, "png", file);
			return path + file.getName();
		} finally {
			lock.unlock();
		}
	}

	public void readImage(long canvasId, HttpServletResponse resp) throws IOException {
		String path = getFilePath(canvasId);
		File file = new File(path);
		ImageManager manager = ImageManager.getInstance();
		if (manager.isSetup(canvasId)) {
			Lock lock = getLock(canvasId);
			lock.lock();
			try {
				String contentType = URLConnection.guessContentTypeFromName(path);
				resp.setContentType(contentType);
				FileInputStream fis = null;
				ServletOutputStream sos = null;
				try {
					fis = new FileInputStream(file);
					sos = resp.getOutputStream();
					byte[] arrayOfByte = new byte[50000];
					int i = 0;
					while ((i = fis.read(arrayOfByte)) >= 0) {
						sos.write(arrayOfByte, 0, i);
					}
				} finally {
					if (fis != null) {
						fis.close();
					}
					if (sos != null) {
						sos.close();
					}
				}
			} finally {
				lock.unlock();
			}
		} else {
			resp.setStatus(404);
		}
	}

	public BufferedImage getBufferedImage(long canvasId) throws InvalidPathException {
		BufferedImage image = new BufferedImage(1, 1, 2);
		File file = new File(getFilePath(canvasId));
		if (!file.exists()) {
			throw new InvalidPathException();
		}
		Lock lock = getLock(canvasId);
		lock.lock();
		try {
			image = ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return image;
	}

	public boolean fileExists(long canvasId) {
		return new File(getFilePath(canvasId)).exists();
	}

	private String getFilePath(long canvasId) {
		return this.root + buildCanvasPath(canvasId) + "img.png";
	}

	private String buildCanvasPath(long canvasId) {
		return PREPENDPATH + canvasId + APPENDPATH;
	}

	public String getImagePath(long canvasId) {
		return buildCanvasPath(canvasId) + "img.png";
	}

	public long getCanvasId(String name) throws InvalidPathException {
		int startIndex = name.indexOf(PREPENDPATH) + PREPENDPATH.length();
		int endIndex = name.indexOf(APPENDPATH);
		if ((startIndex != -1) && (endIndex != -1)) {
			return Long.parseLong(name.substring(startIndex, endIndex));
		}
		throw new InvalidPathException();
	}

	Lock getLock(long canvasId) {
		if (!this.locks.containsKey(Long.valueOf(canvasId))) {
			this.locks.put(Long.valueOf(canvasId), new ReentrantLock(true));
		}
		return this.locks.get(Long.valueOf(canvasId));
	}

	public void serialiseUsers(Map<String, IUser> map) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(1, getSecretKey());
			SealedObject sealedObject = new SealedObject(new HashMap<String, IUser>(map), cipher);
			FileOutputStream fos = new FileOutputStream(this.root + "IUsers.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(sealedObject);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, IUser> deserialiseUsers() {
		File file = new File(this.root + "IUsers.ser");
		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				SealedObject sealedObject = (SealedObject) ois.readObject(); 
				ois.close();
				return (Map<String, IUser>) sealedObject.getObject(getSecretKey());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new HashMap<String, IUser>();
	}

	private SecretKey getSecretKey() {
		byte[] key = new BigInteger(KEYSTRING, 16).toByteArray();
		return new SecretKeySpec(key, "AES");
	}
}
