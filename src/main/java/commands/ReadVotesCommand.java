package commands;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

public class ReadVotesCommand extends AbstractCommand {

    public ReadVotesCommand() {
        super("votes", "get know votes");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        execute(sender, getVotes(chat));
    }

    public SendMessage getVotes(Chat chat) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId()
                              .toString());

        // Build a new authorized API client service.
        ValueRange response = null;
        Sheets service;
        String spreadsheetId = "1SIwXULzoQxtW_2dDPO7-uGwTgNsfwXofbktL4sijnDo";
        try {
            service = SheetsServiceUtil.getSheetsService();

            final String range = "Osen' - 2019!B5:O21";
            response = service.spreadsheets()
                              .values()
                              .get(spreadsheetId, range)
                              .execute();

        } catch (Exception e) {

        }
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            message.setText("No data found.");
        } else {
            String namesVotes = "";
            String vote = "";
            int counter = 0;
            int dateColumnNumber = DateService.getDateColumnNumber(spreadsheetId);
            String naturals = "";
            String gays = "";
            for (List row : values) {
                if (row.get(dateColumnNumber).toString().equals("TRUE")) {
                    counter++;
                    vote = "+";
                    naturals += vote + "  " + row.get(0)
                                                   .toString() + "\n";
                } else {
                    vote = "-";
                    gays += vote + "  " + row.get(0)
                                             .toString() + "\n";
                }
            }
            namesVotes = "Who will be:\n" + naturals;
            namesVotes += "\nWho not will be:\n" + gays;
            namesVotes += "\nSummary : " + counter;
            message.setText(namesVotes);
        }
        return message;
    }
}
