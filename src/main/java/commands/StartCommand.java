package commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

public class StartCommand extends AbstractCommand {
    public StartCommand() {
        super("start", "Choose your destiny!\n");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        execute(sender, sendInlineKeyBoardMessage(chat));
    }

    public static SendMessage sendInlineKeyBoardMessage(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton voteButton = new InlineKeyboardButton();
        InlineKeyboardButton unvoteButton = new InlineKeyboardButton();
        InlineKeyboardButton readVotesButton = new InlineKeyboardButton();

        voteButton.setText("I will Go!");
        unvoteButton.setText("I won't go.");
        readVotesButton.setText("Check others");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();

        keyboardButtonsRow1.add(voteButton.setCallbackData("God grant you health! See ya on football "
                                                                   + chat.getFirstName() + " " + chat.getLastName()));
        keyboardButtonsRow1.add(unvoteButton.setCallbackData("Well.. See ya next time "
                                                                     + chat.getFirstName() + " " + chat.getLastName()));
        keyboardButtonsRow2.add(readVotesButton.setCallbackData("Current football team:\n"));

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return new SendMessage().setChatId(chat.getId()).setText("Let the game begin!")
                                .setReplyMarkup(inlineKeyboardMarkup);
    }
}
