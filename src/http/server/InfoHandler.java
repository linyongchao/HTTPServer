package http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.ggf.drmaa.DrmaaException;
import org.ggf.drmaa.Session;
import org.ggf.drmaa.SessionFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class InfoHandler implements HttpHandler {
	// Http请求处理类
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String id = null; // 响应信息
		String info = null;
		InputStream in = httpExchange.getRequestBody(); // 获得输入流
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		while ((id = reader.readLine()) != null) {
			SessionFactory factory = SessionFactory.getFactory();
			Session session = factory.getSession();
			try {
				session.init("");
				int status = session.getJobProgramStatus(id);
				switch (status) {
				case Session.UNDETERMINED:
					info = "Job status cannot be determined\n";
					break;
				case Session.QUEUED_ACTIVE:
					info = "Job is queued and active\n";
					break;
				case Session.SYSTEM_ON_HOLD:
					info = "Job is queued and in system hold\n";
					break;
				case Session.USER_ON_HOLD:
					info = "Job is queued and in user hold\n";
					break;
				case Session.USER_SYSTEM_ON_HOLD:
					info = "Job is queued and in user and system hold\n";
					break;
				case Session.RUNNING:
					info = "Job is running\n";
					break;
				case Session.SYSTEM_SUSPENDED:
					info = "Job is system suspended\n";
					break;
				case Session.USER_SUSPENDED:
					info = "Job is user suspended\n";
					break;
				case Session.USER_SYSTEM_SUSPENDED:
					info = "Job is user and system suspended\n";
					break;
				case Session.DONE:
					info = "DONE";
					break;
				case Session.FAILED:
					info = "Job finished, but failed\n";
					break;
				} /* switch */
			} catch (DrmaaException e) {
				String err = e.getMessage();
				if ("The job specified by the 'jobid' does not exist."
						.equals(err)) {
					info = "DONE";
				} else {
					info = "InfoError: " + e.getMessage();
				}
			} finally {
				try {
					session.exit();
				} catch (DrmaaException e) {
					e.printStackTrace();
				}
			}
		}
		httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
				info.getBytes().length); // 设置响应头属性及响应信息的长度
		OutputStream out = httpExchange.getResponseBody(); // 获得输出流
		out.write(info.getBytes());
		out.flush();
		httpExchange.close();
	}
}