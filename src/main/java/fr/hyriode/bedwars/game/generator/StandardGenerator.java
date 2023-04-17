package fr.hyriode.bedwars.game.generator;

import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.type.BWGameType;

import java.util.Arrays;
import java.util.List;

public enum StandardGenerator {

    DIAMOND(
            Arrays.asList(
                    new BWGenerator.Tier(0, "diamond", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "I", 4, 600, false, ItemMoney.DIAMOND)
                    )),
                    new BWGenerator.Tier(1, "diamond", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "II", 4, 460, false, ItemMoney.DIAMOND)
                    )),
                    new BWGenerator.Tier(2, "diamond", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "III", 4, 240, false, ItemMoney.DIAMOND)
                    ))
            ),
            Arrays.asList(
                    new BWGenerator.Tier(0, "diamond", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "I", 8, 600, false, ItemMoney.DIAMOND)
                    )),
                    new BWGenerator.Tier(1, "diamond", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "II", 8, 460, false, ItemMoney.DIAMOND)
                    )),
                    new BWGenerator.Tier(2, "diamond", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "III", 8, 240, false, ItemMoney.DIAMOND)
                    ))
            )
    ),
    EMERALD(
            Arrays.asList(
                    new BWGenerator.Tier(0, "emerald", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "I", 2, 1300, false, ItemMoney.EMERALD)
                    )),
                    new BWGenerator.Tier(1, "emerald", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "II", 2, 1000, false, ItemMoney.EMERALD)
                    )),
                    new BWGenerator.Tier(2, "emerald", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "III", 2, 600, false, ItemMoney.EMERALD)
                    ))
            ),
            Arrays.asList(
                    new BWGenerator.Tier(0, "emerald", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "I", 4, 1300, false, ItemMoney.EMERALD)
                    )),
                    new BWGenerator.Tier(1, "emerald", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "II", 4, 1000, false, ItemMoney.EMERALD)
                    )),
                    new BWGenerator.Tier(2, "emerald", Arrays.asList(
                            new BWGenerator.Tier.Drop((__) -> "III", 4, 600, false, ItemMoney.EMERALD)
                    ))
            )
    ),
    FORGE(
            Arrays.asList(
                    new BWGenerator.Tier(0, "forge", Arrays.asList(
                            new BWGenerator.Tier.Drop(48, 30, true, ItemMoney.IRON),
                            new BWGenerator.Tier.Drop(16, 100, true, ItemMoney.GOLD)
                    )),
                    new BWGenerator.Tier(1, "forge", Arrays.asList(
                            new BWGenerator.Tier.Drop(48, 23, true, ItemMoney.IRON),
                            new BWGenerator.Tier.Drop(16, 60, true, ItemMoney.GOLD)
                    )),
                    new BWGenerator.Tier(2, "forge", Arrays.asList(
                            new BWGenerator.Tier.Drop(48, 18, true, ItemMoney.IRON),
                            new BWGenerator.Tier.Drop(16, 40, true, ItemMoney.GOLD),
                            new BWGenerator.Tier.Drop(8, 70*20, true, ItemMoney.EMERALD)
                    )),
                    new BWGenerator.Tier(3, "forge", Arrays.asList(
                            new BWGenerator.Tier.Drop(48, 15, true, ItemMoney.IRON),
                            new BWGenerator.Tier.Drop(16, 30, true, ItemMoney.GOLD),
                            new BWGenerator.Tier.Drop(8, 60*20, true, ItemMoney.EMERALD)
                    ))
            ),
            Arrays.asList(
                    new BWGenerator.Tier(0, "forge", Arrays.asList(
                            new BWGenerator.Tier.Drop(48, 30, true, ItemMoney.IRON),
                            new BWGenerator.Tier.Drop(16, 100, true, ItemMoney.GOLD)
                    )),
                    new BWGenerator.Tier(1, "forge", Arrays.asList(
                            new BWGenerator.Tier.Drop(48, 23, true, ItemMoney.IRON),
                            new BWGenerator.Tier.Drop(16, 60, true, ItemMoney.GOLD)
                    )),
                    new BWGenerator.Tier(2, "forge", Arrays.asList(
                            new BWGenerator.Tier.Drop(48, 18, true, ItemMoney.IRON),
                            new BWGenerator.Tier.Drop(16, 40, true, ItemMoney.GOLD),
                            new BWGenerator.Tier.Drop(8, 70*20, true, ItemMoney.EMERALD)
                    )),
                    new BWGenerator.Tier(3, "forge", Arrays.asList(
                            new BWGenerator.Tier.Drop(48, 15, true, ItemMoney.IRON),
                            new BWGenerator.Tier.Drop(16, 30, true, ItemMoney.GOLD),
                            new BWGenerator.Tier.Drop(8, 60*20, true, ItemMoney.EMERALD)
                    ))
            )
    );

    private final List<BWGenerator.Tier> tiersSolo;
    private final List<BWGenerator.Tier> tiersTrio;

    StandardGenerator(List<BWGenerator.Tier> tiersSolo, List<BWGenerator.Tier> tiersTrio) {
        this.tiersSolo = tiersSolo;
        this.tiersTrio = tiersTrio;
    }

    public List<BWGenerator.Tier> getTiers(BWGameType gameType) {
        return gameType == BWGameType.SOLO || gameType == BWGameType.DOUBLES ? this.tiersSolo : this.tiersTrio;
    }
}
