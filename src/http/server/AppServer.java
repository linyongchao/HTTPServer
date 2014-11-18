package http.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

public class AppServer {
	// 启动服务，监听来自客户端的请求
	public static void appService() throws IOException {
		HttpServerProvider provider = HttpServerProvider.provider();
		HttpServer httpserver = provider.createHttpServer(
				new InetSocketAddress(6666), 100);// 监听端口6666,能同时接 受100个请求
		httpserver.createContext("/runApp", new AppHandler());
		httpserver.setExecutor(null);
		httpserver.start();
		System.out.println("appService started");
	}

	public static void main(String[] args) throws IOException {
		appService();
	}
}
