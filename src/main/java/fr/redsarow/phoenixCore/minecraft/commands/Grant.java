package fr.redsarow.phoenixCore.minecraft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import fr.redsarow.phoenixCore.PhoenixCore;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

/**
 * @author redsarow
 */
public class Grant {

    private static final DynamicCommandExceptionType NAME_NOT_FOUND = new DynamicCommandExceptionType(name ->
            new LiteralMessage("Le joueur \"" + name + "\" n'a pas été trouvé.")
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("grant")
                .requires(source -> source.hasPermissionLevel(3))
                .then(CommandManager.argument("PlayerName", StringArgumentType.string())
                        .executes(ctx -> execute(ctx.getSource(), StringArgumentType.getString(ctx, "PlayerName")))
                )
        );
    }

    private static int execute(ServerCommandSource source, String newplayername) throws CommandSyntaxException {
        boolean addOk = PhoenixCore.getInstance().addGrant(source.getName(), newplayername);
        if(!addOk){
            throw NAME_NOT_FOUND.create(newplayername);
        }
        return 1;
    }

}
