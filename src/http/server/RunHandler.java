package http.server;

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

public class RunHandler implements HttpHandler {
	// Http请求处理类
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String id = null; // 响应信息
		InputStream in = httpExchange.getRequestBody(); // 获得输入流
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String temp = null;
		while ((temp = reader.readLine()) != null) {
			SessionFactory factory = SessionFactory.getFactory();
			Session session = factory.getSession();
			try {
				session.init("");
				JobTemplate jt = session.createJobTemplate();
				jt.setRemoteCommand(temp);
				id = session.runJob(jt);
				session.deleteJobTemplate(jt);
				session.exit();
			} catch (DrmaaException e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
		httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
				id.getBytes().length); // 设置响应头属性及响应信息的长度
		OutputStream out = httpExchange.getResponseBody(); // 获得输出流
		out.write(id.getBytes());
		out.flush();
		httpExchange.close();
	}
}