package fr.hyriode.bedwars.manager.pnj;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.hyrame.hologram.Hologram;
import fr.hyriode.hyrame.npc.NPCInteractCallback;
import fr.hyriode.hyrame.packet.IPacketContainer;
import fr.hyriode.hyrame.packet.IPacketHandler;
import fr.hyriode.hyrame.packet.PacketType;
import fr.hyriode.hyrame.reflection.entity.EntityUseAction;
import fr.hyriode.hyrame.utils.ThreadUtil;
import net.minecraft.server.v1_8_R3.EntityCreature;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityInteractManager {

    private static final Map<PNJ, Hologram> ENTITIES = new HashMap<>();

    private static HyriBedWars plugin;

    public static void init(HyriBedWars plugin){
        EntityInteractManager.plugin = plugin;
        plugin.getHyrame().getPacketInterceptor().addHandler(PacketType.Play.Client.USE_ENTITY, new IPacketHandler() {
            @Override
            public void onReceive(IPacketContainer container) {
                final Player player = container.getPlayer();
                final Location location = player.getLocation();
                final int entityId = container.getIntegers().read(0);

                for (PNJ npc : EntityInteractManager.getEntities().keySet()) {
                    final NPCInteractCallback callback = npc.getInteractCallback();

                    if (npc.getId() != entityId || callback == null || location.distance(npc.getLocation()) > 3.0D) {
                        continue;
                    }

                    final Object object = container.getValue("action");

                    if (object != null) {
                        ThreadUtil.backOnMainThread(plugin, () -> callback.call(object.toString().equals(EntityUseAction.INTERACT.name()), player));
                    }
                }
            }
        });
    }

    /**
     * Create a PNJ
     *
     * @param location PNJ location
     * @param entity PNJ entity
     * @return - {@link PNJ} object
     */
    public static PNJ createEntity(Location location, Class<? extends EntityCreature> entity, List<String> hologramLines){
        Hologram hologram = new Hologram.Builder(plugin, location.clone().add(0, 1.5, 0))
                .withLines(hologramLines.stream().map(line -> new Hologram.Line(() -> line)).collect(Collectors.toList()))
                .build();
        PNJ pnj = new PNJ(location, entity, hologram);
        ENTITIES.put(pnj, hologram);
        return pnj;
    }

    public static Map<PNJ, Hologram> getEntities() {
        return ENTITIES;
    }
}
