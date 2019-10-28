package commands;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ALL")
public class VoteCommand extends AbstractCommand {

    public VoteCommand() {
        super("vote", "Vote for yourself!\n");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        Sheets sheetsService = null;
        ValueRange response = null;
        String spreadsheetId = "1SIwXULzoQxtW_2dDPO7-uGwTgNsfwXofbktL4sijnDo";
        String rangeFoVote = "";
        try {
            response = SheetsServiceUtil.getSheetsService().spreadsheets()
                              .values()
                              .get(spreadsheetId, "Test!A4:A6")
                              .execute();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        int dateColumnNumber = DateService.getDateColumnNumber(spreadsheetId);
        String dateColumnLetter = DateService.toAlphabetic(dateColumnNumber);
        int userNamePosition = 0;
        String userName = user.getFirstName() + " " + user.getLastName();
        List<List<Object>> values = response.getValues();
        int counter = 4;
        for (List row : values) {
            if (row.get(0).equals(userName)) {
                userNamePosition = counter;
            }
            counter++;
        }
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId()
                              .toString());
        if (userNamePosition != 0) {
            try {
                sheetsService = SheetsServiceUtil.getSheetsService();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }

            ValueRange body = new ValueRange()
                    .setValues(Arrays.asList(
                            Arrays.asList("TRUE")));
            try {
                String rangeForVote = dateColumnLetter + userNamePosition + ":" + dateColumnLetter +
                        userNamePosition;
                sheetsService.spreadsheets()
                             .values()
                             .update(spreadsheetId,
                                     rangeForVote, body)
                             .setValueInputOption("USER_ENTERED")
                             .execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            message.setText("Such sexy user not found: " + userName + ".");
            execute(sender, message, user);
        }
    }
}
