package fr.redsarow.phoenixCore.discord;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import fr.redsarow.phoenixCore.Main;
import fr.redsarow.phoenixCore.discord.commands.*;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author redsarow
 */
public class Bot {

    private static Bot INSTANCE;
    private static Logger LOGGER = Main.getLogger(Bot.class.getName());

    public final String prefix;
    public final List<String> roles;
    public final SendDiscordMsg sendMsg;
    private GatewayDiscordClient client;
    private Channel channelOut;
    private List<Channel> channelIn;

    private Bot() {

        prefix = Main.conf.prefix;
        roles = Main.conf.roles;

        createClient(Main.conf.token);

        // events
        EventDispatcher dispatcher = client.getEventDispatcher();
        // commands
        dispatcher.on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> channelIn.contains(msg.getChannel().block()))
                .filter(msg -> msg.getContent().startsWith(prefix))
                .flatMap(CommandManagement::run)
                .subscribe();
        // ready
        dispatcher.on(ReadyEvent.class)
                .subscribe(this::onReady);

        // commands register
        new Info();
        new Help();
        new DeathCount();
        new Grant();

        sendMsg = new SendDiscordMsg();
    }

    public static Bot getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Bot();
        }
        return INSTANCE;
    }

    private void createClient(String token) {
        client = DiscordClientBuilder.create(token)
                .build()
                .gateway()
                .setInitialStatus(s -> Presence.doNotDisturb(Activity.playing("Init ...")))
                .login()
                .block();
    }

    public void disconnect() {
        client.updatePresence(Presence.doNotDisturb(Activity.playing("disconnect"))).block();
        client.onDisconnect().subscribe();
    }

    private Mono<Void> onReady(ReadyEvent event) {

        List<GuildChannel> channels = client.getGuilds()
                .toStream()
                .findFirst()
                .get()
                .getChannels()
                .collectList()
                .block();

        List<String> stringChannels = Main.conf.channelIn;

        channelIn = stringChannels.stream().collect(
                ArrayList<Channel>::new
                , (channels1, s) -> channels1.add(channels.stream().filter(channel -> channel.getName().equalsIgnoreCase(s)).findFirst().get())
                , ArrayList::addAll
        );

        String stringChannel = Main.conf.channelOut;
        channelOut = channels.stream().filter(channel -> channel.getName().equalsIgnoreCase(stringChannel)).findFirst().get();

        LOGGER.info("Bot is now ready!");
        // set status
        client.updatePresence(Presence.online(Activity.playing(prefix + "help"))).block();

        return Mono.empty().then();
    }

    public GatewayDiscordClient getClient() {
        return client;
    }

    public Channel getChannelOut() {
        return channelOut;
    }
}
