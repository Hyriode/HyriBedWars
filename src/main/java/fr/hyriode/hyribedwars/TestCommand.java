package fr.hyriode.hyribedwars;

import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyrame.command.HyriCommandType;
import fr.hyriode.hyriapi.rank.EHyriRank;
import fr.hyriode.hyriapi.rank.HyriPermission;
import org.bukkit.entity.Player;

public class TestCommand extends HyriCommand<HyriBedWars> {

    private enum Permission implements HyriPermission {
        USE
    }


    public TestCommand(HyriBedWars plugin) {
        super(plugin, new HyriCommandInfo("test")
                .withName("Test")
                .withDescription("Test command")
                .withUsage("/test")
                .withType(HyriCommandType.PLAYER)
                .withPermission(Permission.USE));

        Permission.USE.add(EHyriRank.ADMINISTRATOR);
    }

    @Override
    public void handle(HyriCommandContext ctx) {
        if (ctx.getSender() instanceof Player) {
            Player player = (Player) ctx.getSender();

            /*
            final HyriLanguageMessage firstHoloLine = new HyriLanguageMessage("npc.shop.hologram.first-line")
                    .addValue(HyriLanguage.FR, ChatColor.AQUA + "" + ChatColor.BOLD + "BOUTIQUE D'OBJETS")
                    .addValue(HyriLanguage.EN, ChatColor.AQUA + "" + ChatColor.BOLD + "ITEMS SHOP");

            HyriBedWarsShopNPC npc = new HyriBedWarsShopNPC(
                    player.getLocation(),
                    new NPCSkin("ewogICJ0aW1lc3RhbXAiIDogMTYxMzk2MDkxODk1OCwKICAicHJvZmlsZUlkIiA6ICJhMDVkZWVjMDdhMGU0MDc2ODdjYmRjNWRjYWNhODU4NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaWxsYWdlciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83YWY3YzA3ZDFkZWQ2MWIxZDMzMTI2ODViMzJlNDU2OGZmZGRhNzYyZWM4ZDgwODg5NWNjMzI5YTkzZDYwNmUwIgogICAgfQogIH0KfQ==",
                            "FdLsqXlKUumDDh22nUVCdGnmD042i03ZVhU8sY+mGVx80OY0z5UG/sR40CC0kxOiMnLbP+8/cse0VUMMxE3hCLtYvjvA2akf4nuTAS9V5ELZms0sSfQahjsIbhEhK1GR5HzzLCqyrDhxopF7Qc6cyDPpBLq+HxIDhPkpK7B6PGhlr2F0RAtpWUHlJPrLY4TbV9y6UQPHBvW/DeuKRWvRASBC2c3EopZgCgFHoIOqe2u73C49lrOSYBKiWN+opojNQatBv5OYQUgcOjB0b0mjQGq+oXe2zNjdb7u9Ek3x4cVt24hq9WLgPalt7l1MxRflENMMRt/bA+Mmmf8qy/7c0mgPIEDdLRRfYim7odXAcv3DvGKYU8AR5jRPGIIIMBonZ90t17MCCoqi/++casmcncKE16HL+JUrEm1aqwUHcJreaiZo8ApRavDVObkj34VHWig97F/ffJfmFPzt5XTiEbj9jUsk6MIW/H9dGFOXhxL/qf8gdzbZaPN0NipuqOMB3JRidd8LHlq6KaJDGMEICivJi4+cK3b5wLRz9Zo8fpdEzROtqoImYzasCyqRXpBeZ1kBdQfeuyF9YnZevGAqTP8DgxvFYLj9VhyxxBYHvyrCqucedTPYWs33cNBoxcR7VvbmwvsnLAtxBUB9tSCh3K9oD+mqjIg5xESh9CB2+sA="),
                    Collections.singletonList(firstHoloLine.getForPlayer(player)));

            NPCManager.createNPC(npc.getLocation(), npc.getSkin(), npc.getHologramLines());
            NPCManager.sendNPC(NPCManager.createNPC(npc.getLocation(), npc.getSkin(), npc.getHologramLines()));
            player.sendMessage("Le npc est spawn");
            */
        }
    }
}
