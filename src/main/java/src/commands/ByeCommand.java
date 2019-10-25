package src.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ByeCommand extends AbstractCommand {

    public ByeCommand() {
        super("bye","say bye to bot\n");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());
        List<String> footballers = new ArrayList<String>(){{
            add("Messi");
            add("Iniesta");
            add("Xavi");
            add("Dybala");
        }};
        Random rand = new Random();
        String randomElement = footballers.get(rand.nextInt(footballers.size()));
        message.setText("Bye my little " + user.getFirstName() + " " + randomElement + "...c u...");
        execute(sender, message, user);
    }
}
