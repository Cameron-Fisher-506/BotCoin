package za.co.botcoin.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DTUtils {
    public static String getCurrentDateTime()
    {
        String toReturn = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        now = calendar.getTime();

        toReturn = simpleDateFormat.format(now);

        return toReturn;

    }

    public static Date parseDateTime(String dateTime)
    {
        Date toReturn = null;

        try
        {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            toReturn = sdfDate.parse(dateTime);
        }catch(Exception e)
        {
            System.out.println("\nError: " + e.getMessage() +
                    "\nApp: TMSPulsePTO-Re" +
                    "\nMethod: parseDate" +
                    "\nData: " + dateTime +
                    "\nDate: " + GeneralUtils.getCurrentDateTime() +
                    "\n");
        }


        return toReturn;
    }

    public static long getDifferenceDateTimeInMin(String dateTime)
    {
        long toReturn = 0;

        Date parseDateTime = parseDateTime(dateTime);
        Date currentDateTime = parseDateTime(getCurrentDateTime());

        long difference = currentDateTime.getTime() - parseDateTime.getTime();
        toReturn = difference / (60 * 1000);


        return toReturn;
    }
}
