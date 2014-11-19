package http.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

public class AppServer {
	// 启动服务，监听来自客户端的请求
	public static void runService() throws IOException {
		HttpServerProvider provider = HttpServerProvider.provider();
		HttpServer httpserver = provider.createHttpServer(
				new InetSocketAddress(6666), 100);// 监听端口6666,能同时接 受100个请求
		httpserver.createContext("/run", new RunHandler());
		httpserver.setExecutor(null);
		httpserver.start();
		System.out.println("runService started");
	}

	public static void infoService() throws IOException {
		HttpServerProvider provider = HttpServerProvider.provider();
		HttpServer httpserver = provider.createHttpServer(
				new InetSocketAddress(8888), 100);
		httpserver.createContext("/info", new InfoHandler());
		httpserver.setExecutor(null);
		httpserver.start();
		System.out.println("infoService started");
	}

	public static void main(String[] args) throws IOException {
		runService();
		infoService();
	}
}
