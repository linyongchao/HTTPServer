package http.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	/**
	 * sh文件的保存路径
	 */
	public static String sh_path;
	/**
	 * 结果目录的路径
	 */
	public static String result_path;
	static {
		getPath();
	}

	/**
	 * 获取文件存放路径
	 * 
	 * @return
	 */
	private static void getPath() {
		Properties pro = new Properties();
		InputStream is = PropertiesUtil.class.getClassLoader()
				.getResourceAsStream("path.properties");
		try {
			pro.load(is);
		} catch (IOException e) {
			System.err.println("获取路径配置文件信息失败" + e);
		}
		sh_path = pro.getProperty("sh_path");
		result_path = pro.getProperty("result_path");
	}
}