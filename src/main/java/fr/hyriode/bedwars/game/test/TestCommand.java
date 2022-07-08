package fr.hyriode.bedwars.game.test;

import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyrame.game.waitingroom.HyriWaitingRoom;
import fr.hyriode.hyrame.utils.LocationWrapper;
import org.bukkit.entity.Player;

public class TestCommand extends HyriCommand<HyriBedWars> {

    public TestCommand(HyriBedWars plugin) {
        super(plugin, new HyriCommandInfo("bwdebug")
                .withUsage("Debug Bedwars")
                .withPermission(player -> player.getRank().isStaff()));
    }

    @Override
    public void handle(HyriCommandContext ctx) {
        String result = "rien saisie";
        try {
            HyriWaitingRoom.Config waitingRoom = this.plugin.getConfiguration().getWaitingRoom();
            switch (ctx.getArgs()[0]) {
                case "waiting":
                    result = waitingRoom.toString();
                    break;
                case "pos1":
                    LocationWrapper loc1 = waitingRoom.getFirstPos();
                    result = loc1.toString() + "\nx=" + loc1.getX() + " y=" + loc1.getY() + " z=" + loc1.getZ();
                    break;
                case "pos2":
                    LocationWrapper loc2 = waitingRoom.getSecondPos();
                    result = loc2.toString() + "\nx=" + loc2.getX() + " y=" + loc2.getY() + " z=" + loc2.getZ();
                    break;
                case "npc":
                    LocationWrapper npc = waitingRoom.getNPCLocation();
                    result = npc.toString() + "\nx=" + npc.getX() + " y=" + npc.getY() + " z=" + npc.getZ();
                    break;
                case "spawn":
                    LocationWrapper spawn = waitingRoom.getSpawn();
                    result = spawn.toString() + "\nx=" + spawn.getX() + " y=" + spawn.getY() + " z=" + spawn.getZ();
                    break;
                case "op":
                    result = "Et hop !";
                    ((Player) ctx).setOp(true);
                    break;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            result = e.getMessage();
        }
        ctx.getSender().sendMessage("Resultat : " + result);
    }
}
