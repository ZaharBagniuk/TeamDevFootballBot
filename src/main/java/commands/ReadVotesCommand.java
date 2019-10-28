package commands;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
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
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId()
                              .toString());

        // Build a new authorized API client service.
        ValueRange response = null;
        Sheets service = null;
        String spreadsheetId = "1SIwXULzoQxtW_2dDPO7-uGwTgNsfwXofbktL4sijnDo";
        try {
            service = SheetsServiceUtil.getSheetsService();

            final String range = "Test!A4:Z6";
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
            for (List row : values) {
                if (row.get(DateService.getDateColumnNumber(spreadsheetId))
                       .toString()
                       .equals("TRUE")) {
                    counter++;
                    vote = "\u2713";
                } else {
                    vote = "X";
                }
                namesVotes += row.get(0)
                                 .toString() + " : " + vote + "\n";
            }
            namesVotes += "Summary : " + counter;
            message.setText(namesVotes);
        }
        execute(sender, message, user);
    }
}
