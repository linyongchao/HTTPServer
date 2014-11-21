package http.server;

import http.handler.ResourcesHandler;
import http.handler.RunHandler;
import http.handler.StatusHandler;

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

	public static void statusService() throws IOException {
		HttpServerProvider provider = HttpServerProvider.provider();
		HttpServer httpserver = provider.createHttpServer(
				new InetSocketAddress(7777), 100);
		httpserver.createContext("/status", new StatusHandler());
		httpserver.setExecutor(null);
		httpserver.start();
		System.out.println("statusService started");
	}

	public static void resService() throws IOException {
		HttpServerProvider provider = HttpServerProvider.provider();
		HttpServer httpserver = provider.createHttpServer(
				new InetSocketAddress(8888), 100);
		httpserver.createContext("/res", new ResourcesHandler());
		httpserver.setExecutor(null);
		httpserver.start();
		System.out.println("resService started");
	}

	public static void main(String[] args) throws IOException {
		runService();
		statusService();
		resService();
	}
}