package fr.hyriode.bedwars.api;

import com.google.gson.Gson;
import fr.hyriode.bedwars.api.manager.HyriBWPlayerManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class HyriBedWarsAPI {

    public static final String REDIS_KEY = "bw:";
    public static final Gson GSON = new Gson();

    private final HyriBWPlayerManager playerManager;

    private final LinkedBlockingQueue<Consumer<Jedis>> redisRequests;
    private final Thread redisRequestsThread;

    private final JedisPool jedisPool;

    private static HyriBedWarsAPI instance;

    private boolean running;

    public HyriBedWarsAPI(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.redisRequests = new LinkedBlockingQueue<>();
        this.redisRequestsThread = new Thread(() -> {
            try {
                while(running){
                    final Consumer<Jedis> request = this.redisRequests.take();

                    try (final Jedis jedis = this.getRedisResource()) {
                        if (jedis != null) {
                            request.accept(jedis);
                        }
                    }
                }
            } catch (InterruptedException ignored) {}
        }, "RTF API - Redis processor");
        this.playerManager = new HyriBWPlayerManager(this);
        instance = this;
    }

    public void start() {
        this.running = true;

        this.redisRequestsThread.start();
    }

    public void stop() {
        this.running = false;

        this.redisRequestsThread.interrupt();
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public Jedis getRedisResource() {
        return this.jedisPool.getResource();
    }

    public void redisRequest(Consumer<Jedis> request) {
        this.redisRequests.add(request);
    }

    public String getFromRedis(String key) {
        try (final Jedis jedis = this.getRedisResource()) {
            if (jedis != null) {
                return jedis.get(key);
            }
        }
        return null;
    }

    public HyriBWPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public static HyriBedWarsAPI get() {
        return instance;
    }
}
