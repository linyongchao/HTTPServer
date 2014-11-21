package http.handler;

import http.util.FileUtil;
import http.util.PropertiesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.ggf.drmaa.DrmaaException;
import org.ggf.drmaa.JobTemplate;
import org.ggf.drmaa.Session;
import org.ggf.drmaa.SessionFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * @Description:获取运行时耗费的资源
 * @author lin
 * @date 2014-11-21 下午1:00:33
 */
public class ResourcesHandler implements HttpHandler {
	// Http请求处理类
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String id = null; // 响应信息
		InputStream in = httpExchange.getRequestBody(); // 获得输入流
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		while ((id = reader.readLine()) != null) {
			System.err.println("Resource id = " + id);
			String f = PropertiesUtil.sh_path + "select" + id + ".sh";
			FileUtil.createFile(f);
			FileUtil.appendWrite(f, "qacct -j " + id + "\n");
			Runtime.getRuntime().exec("chmod 755 " + f);
			SessionFactory factory = SessionFactory.getFactory();
			Session session = factory.getSession();
			try {
				session.init("");
				JobTemplate jt = session.createJobTemplate();
				jt.setRemoteCommand(f);
				id = session.runJob(jt);
				session.deleteJobTemplate(jt);
			} catch (DrmaaException e) {
				System.out.println("RunError: " + e.getMessage());
			} finally {
				try {
					session.exit();
				} catch (DrmaaException e) {
					e.printStackTrace();
				}
			}
		}
		httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0); // 设置响应头属性及响应信息的长度
		OutputStream out = httpExchange.getResponseBody(); // 获得输出流
		out.write(0);
		out.flush();
		httpExchange.close();
	}
}