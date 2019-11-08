package commands;

import com.google.api.services.sheets.v4.model.ValueRange;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateService {

    public static int getDateColumnNumber(String id) {
        ValueRange response;
        int dateColumnNumber = 1;
        try {
            final String rangeForDates = "Osen' - 2019!C2:O2";
            response = SheetsServiceUtil.getSheetsService().spreadsheets()
                              .values()
                              .get(id, rangeForDates)
                                        .setValueRenderOption("UNFORMATTED_VALUE")
                              .execute();
            List<List<Object>> values = response.getValues();
            List<Date> dates = new ArrayList<>();
            Date currentDate = new Date();
            int counter = 1;
            boolean isFirst = true;
            for (List row : values) {
                for (Object columnFromRow : row) {
                    LocalDate localDate = LocalDate.of(1899, Month.DECEMBER, 30).plusDays(convertToLong(columnFromRow));
                    Date dateObj = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    if (compareTwoDates(currentDate, dateObj) <= 0 && isFirst) {
                        dates.add(dateObj);
                        dateColumnNumber = counter;
                        isFirst = false;
                    }
                    counter++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateColumnNumber;
    }

    public static int compareTwoDates(Date startDate, Date endDate) {
        Date sDate = getZeroTimeDate(startDate);
        Date eDate = getZeroTimeDate(endDate);
        if (sDate.before(eDate)) {
            return -1;
        }
        if (sDate.after(eDate)) {
            return 1;
        }
        return 0;
    }

    private static Date getZeroTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        return date;
    }

    public static String toAlphabetic(int i) {
        if( i<0 ) {
            return "-"+toAlphabetic(-i-1);
        }

        int quot = i/26;
        int rem = i%26;
        char letter = (char)((int)'A' + rem);
        if( quot == 0 ) {
            return ""+letter;
        } else {
            return toAlphabetic(quot-1) + letter;
        }
    }

    public static Long convertToLong(Object o){
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;

    }
}
