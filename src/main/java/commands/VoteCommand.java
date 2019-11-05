package commands;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("Duplicates")
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
                              .get(spreadsheetId, "Osen' - 2019!B5:B21")
                              .execute();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        int dateColumnNumber = DateService.getDateColumnNumber(spreadsheetId);
        String dateColumnLetter = DateService.toAlphabetic(dateColumnNumber + 1);
        int userNamePosition = 0;
        String userName = user.getFirstName() + " " + user.getLastName();
        List<List<Object>> values = response.getValues();
        int counter = 5;
        for (List row : values) {
            String nameFromTable = row.get(0).toString();
            if (nameFromTable.trim().equals(userName)) {
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
                String rangeForVote = "Osen' - 2019!" + dateColumnLetter + userNamePosition + ":" + dateColumnLetter +
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
            message.setText("Such sexy user not found: " + userName + "." +
                                    " Check the correspondence of the name in the telegram and google table");
            execute(sender, message);
        }
    }
}
