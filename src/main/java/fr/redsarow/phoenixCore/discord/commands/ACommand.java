package fr.redsarow.phoenixCore.discord.commands;

import discord4j.core.object.entity.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author redsarow
 */
public abstract class ACommand {

    private final String name;
    private final String description;
    private final String usage;
    private final List<String> exemple;
    private final String alias;

    public ACommand(String name, String description, String usage, List<String> exemple, String... alias) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.exemple = exemple == null ? new ArrayList<>() : exemple;

        if (alias != null && alias.length > 0) {
            this.alias = Arrays.stream(alias).reduce((s, s2) -> s + "/" + s2).get();
        }else {
            this.alias = "N/C";
        }

        CommandManagement.registerCommand(this);
        CommandManagement.registerAlias(name, alias);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public List<String> getExemple() {
        return exemple;
    }

    public String getAlias() {
        return alias;
    }

    public abstract boolean run(Message message);

}
