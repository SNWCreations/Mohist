package com.mohistmc.ai.koukou;

import com.mohistmc.ai.koukou.network.MyHttpHandler;
import com.mohistmc.ai.koukou.network.event.ListenRegister;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApiController {

    public static ListenRegister eventBus = ListenRegister.getInstance();
    public static Logger LOGGER = LogManager.getLogger("Mohist Http Server");

    public static void init() {
        if (!AIConfig.INSTANCE.enable()) return;
        ApiController api = new ApiController();
        api.start();
        eventBus.registerListener(new KouKouPostListener());
    }

    @SneakyThrows
    public void start() {
        var host = AIConfig.INSTANCE.http_server_hostname();
        var port = AIConfig.INSTANCE.http_server_port();
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/", new MyHttpHandler());
        server.setExecutor(Executors.newFixedThreadPool(5));
        server.start();
        LOGGER.info("已部署AI服务 {}:{}", host, port);
    }
}
