package fr.hyriode.bedwars.api.manager;

import fr.hyriode.bedwars.api.HyriBedWarsAPI;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;

import java.util.UUID;
import java.util.function.Function;

public class HyriBWPlayerManager {

    private final HyriBedWarsAPI api;

    private static final Function<UUID, String> REDIS_KEY = uuid -> HyriBedWarsAPI.REDIS_KEY + "players:" + uuid.toString();

    public HyriBWPlayerManager(HyriBedWarsAPI api) {
        this.api = api;
    }

    public HyriBWPlayer getPlayer(UUID uuid) {
        final String json = this.api.getFromRedis(REDIS_KEY.apply(uuid));

        if (json != null) {
            return HyriBedWarsAPI.GSON.fromJson(json, HyriBWPlayer.class);
        }
        return null;
    }

    public void sendPlayer(HyriBWPlayer player) {
        this.api.redisRequest(jedis -> jedis.set(REDIS_KEY.apply(player.getUUID()), HyriBedWarsAPI.GSON.toJson(player)));
    }

    public void removePlayer(UUID uuid) {
        this.api.redisRequest(jedis -> jedis.del(REDIS_KEY.apply(uuid)));
    }
}
