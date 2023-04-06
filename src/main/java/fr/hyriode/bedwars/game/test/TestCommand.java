package fr.hyriode.bedwars.game.test;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.host.BWForgeValues;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandCheck;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;

import java.util.Arrays;

public class TestCommand extends HyriCommand<HyriBedWars> {
    public TestCommand(HyriBedWars plugin) {
        super(plugin, new HyriCommandInfo("test")
                .withDescription("test")
                .withPermission(p -> p.getRank().isStaff()));
    }

    @Override
    public void handle(HyriCommandContext ctx) {

        this.handleArgument(ctx, "spawnBetween " + HyriCommandCheck.INPUT, output -> {
            String[] args = output.get(String.class).split("-");
            String name = args[0];
            int tier = Integer.parseInt(args[1]);
            String drop = args[2];

            ctx.getSender().sendMessage("spawnBetween: " + Arrays.toString(args));
            ctx.getSender().sendMessage("spawnBetween: " + name + " " + tier + " " + drop);
            ctx.getSender().sendMessage("spawnBetween: " + BWForgeValues.getSpawnBetween(name, tier, drop));
        });

        this.handleArgument(ctx, "spawnLimit " + HyriCommandCheck.INPUT, output -> {
            String[] args = output.get(String.class).split("-");
            String name = args[0];
            int tier = Integer.parseInt(args[1]);
            String drop = args[2];

            ctx.getSender().sendMessage("spawnLimit: " + Arrays.toString(args));
            ctx.getSender().sendMessage("spawnLimit: " + name + " " + tier + " " + drop);
            ctx.getSender().sendMessage("spawnLimit: " + BWForgeValues.getSpawnLimit(name, tier, drop));
        });

        this.handleArgument(ctx, "splitting " + HyriCommandCheck.INPUT, output -> {
            String[] args = output.get(String.class).split("-");
            String name = args[0];
            int tier = Integer.parseInt(args[1]);
            String drop = args[2];

            ctx.getSender().sendMessage("splitting: " + Arrays.toString(args));
            ctx.getSender().sendMessage("splitting: " + name + " " + tier + " " + drop);
            ctx.getSender().sendMessage("splitting: " + BWForgeValues.getSplitting(name, tier, drop));
        });


    }
}
