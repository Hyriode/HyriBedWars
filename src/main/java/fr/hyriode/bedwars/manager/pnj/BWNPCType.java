package fr.hyriode.bedwars.manager.pnj;

import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.util.Skin;
import fr.hyriode.api.language.HyriLanguageMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum BWNPCType {
    SHOP(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTYxMzk2MDkxODk1OCwKICAicHJvZmlsZUlkIiA6ICJhMDVkZWVjMDdhMGU0MDc2ODdjYmRjNWRjYWNhODU4NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaWxsYWdlciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83YWY3YzA3ZDFkZWQ2MWIxZDMzMTI2ODViMzJlNDU2OGZmZGRhNzYyZWM4ZDgwODg5NWNjMzI5YTkzZDYwNmUwIgogICAgfQogIH0KfQ==",
            "FdLsqXlKUumDDh22nUVCdGnmD042i03ZVhU8sY+mGVx80OY0z5UG/sR40CC0kxOiMnLbP+8/cse0VUMMxE3hCLtYvjvA2akf4nuTAS9V5ELZms0sSfQahjsIbhEhK1GR5HzzLCqyrDhxopF7Qc6cyDPpBLq+HxIDhPkpK7B6PGhlr2F0RAtpWUHlJPrLY4TbV9y6UQPHBvW/DeuKRWvRASBC2c3EopZgCgFHoIOqe2u73C49lrOSYBKiWN+opojNQatBv5OYQUgcOjB0b0mjQGq+oXe2zNjdb7u9Ek3x4cVt24hq9WLgPalt7l1MxRflENMMRt/bA+Mmmf8qy/7c0mgPIEDdLRRfYim7odXAcv3DvGKYU8AR5jRPGIIIMBonZ90t17MCCoqi/++casmcncKE16HL+JUrEm1aqwUHcJreaiZo8ApRavDVObkj34VHWig97F/ffJfmFPzt5XTiEbj9jUsk6MIW/H9dGFOXhxL/qf8gdzbZaPN0NipuqOMB3JRidd8LHlq6KaJDGMEICivJi4+cK3b5wLRz9Zo8fpdEzROtqoImYzasCyqRXpBeZ1kBdQfeuyF9YnZevGAqTP8DgxvFYLj9VhyxxBYHvyrCqucedTPYWs33cNBoxcR7VvbmwvsnLAtxBUB9tSCh3K9oD+mqjIg5xESh9CB2+sA="),
            "npc.shop.name"),
    UPGRADE(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTYxMzk2MDkxODk1OCwKICAicHJvZmlsZUlkIiA6ICJhMDVkZWVjMDdhMGU0MDc2ODdjYmRjNWRjYWNhODU4NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaWxsYWdlciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83YWY3YzA3ZDFkZWQ2MWIxZDMzMTI2ODViMzJlNDU2OGZmZGRhNzYyZWM4ZDgwODg5NWNjMzI5YTkzZDYwNmUwIgogICAgfQogIH0KfQ==",
            "FdLsqXlKUumDDh22nUVCdGnmD042i03ZVhU8sY+mGVx80OY0z5UG/sR40CC0kxOiMnLbP+8/cse0VUMMxE3hCLtYvjvA2akf4nuTAS9V5ELZms0sSfQahjsIbhEhK1GR5HzzLCqyrDhxopF7Qc6cyDPpBLq+HxIDhPkpK7B6PGhlr2F0RAtpWUHlJPrLY4TbV9y6UQPHBvW/DeuKRWvRASBC2c3EopZgCgFHoIOqe2u73C49lrOSYBKiWN+opojNQatBv5OYQUgcOjB0b0mjQGq+oXe2zNjdb7u9Ek3x4cVt24hq9WLgPalt7l1MxRflENMMRt/bA+Mmmf8qy/7c0mgPIEDdLRRfYim7odXAcv3DvGKYU8AR5jRPGIIIMBonZ90t17MCCoqi/++casmcncKE16HL+JUrEm1aqwUHcJreaiZo8ApRavDVObkj34VHWig97F/ffJfmFPzt5XTiEbj9jUsk6MIW/H9dGFOXhxL/qf8gdzbZaPN0NipuqOMB3JRidd8LHlq6KaJDGMEICivJi4+cK3b5wLRz9Zo8fpdEzROtqoImYzasCyqRXpBeZ1kBdQfeuyF9YnZevGAqTP8DgxvFYLj9VhyxxBYHvyrCqucedTPYWs33cNBoxcR7VvbmwvsnLAtxBUB9tSCh3K9oD+mqjIg5xESh9CB2+sA="),
            "npc.upgrade.name")
    ;

    private final Skin defaultSkin;
    private final String keyName;

    BWNPCType(Skin defaultSkin, String keyName){
        this.defaultSkin = defaultSkin;
        this.keyName = keyName;
    }

    public Skin getDefaultSkin() {
        return defaultSkin;
    }

    public String getKeyName() {
        return keyName;
    }

    public HyriLanguageMessage getLanguageName(){
        return HyriLanguageMessage.get(this.keyName);
    }

    public List<String> getLore(Player language){
        return Arrays.asList(ChatColor.AQUA + this.getLanguageName().getValue(language),
                ChatColor.YELLOW + "" + ChatColor.BOLD + HyriLanguageMessage.get("npc.click").getValue(language));
    }
}
