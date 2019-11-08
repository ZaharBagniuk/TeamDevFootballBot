import commands.ByeCommand;
import commands.HelpCommand;
import commands.HiCommand;
import commands.ReadVotesCommand;
import commands.StartCommand;
import commands.UnvoteCommand;
import commands.VoteCommand;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingCommandBot {

    private static final String BOT_NAME = "@TeamDevFootballBot";
    private static final String BOT_TOKEN = "888166484:AAFar0ScoX8AqJ7ifB94BUaPimn00ZIPqCY";
    private final ReadVotesCommand readVotesCommand = new ReadVotesCommand();
    private final VoteCommand voteCommand = new VoteCommand();
    private final UnvoteCommand unvoteCommand = new UnvoteCommand();

    public Bot(DefaultBotOptions botOptions) {
        super(botOptions, BOT_NAME);

        register(new ByeCommand());
        register(new HiCommand());
        register(this.readVotesCommand);
        register(new StartCommand());
        register(this.voteCommand);
        register(this.unvoteCommand);
        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction(((absSender, message) -> {

            SendMessage text = new SendMessage();
            text.setChatId(message.getChatId());
            text.setText(message.getText() + " command not found!");

            try {
                absSender.execute(text);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        }));
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if(update.hasCallbackQuery()) {
            try {
                String message = update.getCallbackQuery()
                                    .getData();
                SendMessage sendMessage = new SendMessage().setText(
                        message).setChatId(update.getCallbackQuery()
                                                 .getMessage()
                                                 .getChatId());
                execute(sendMessage);
                User user = update.getCallbackQuery()
                                  .getFrom();
                Chat chat = update.getCallbackQuery().getMessage().getChat();
                if (message.startsWith("God grant you health! See ya on football")) {
                    voteCommand.execute(this, user, chat, new String[]{});
                } else if (message.startsWith("Current football team")) {
                    readVotesCommand.execute(this, user, chat, new String[]{});
                } else if (message.startsWith("Well.. See ya next time")) {
                    unvoteCommand.execute(this, user, chat, new String[]{});
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            if (!update.hasMessage()) {
                throw new IllegalStateException("Update doesn't have a body!");
            }

            Message msg = update.getMessage();

            String clearMessage = msg.getText();

            SendMessage answer = new SendMessage();

            answer.setText(clearMessage);
            answer.setChatId(msg.getChatId());
            try {
                execute(answer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
