package rick.expensestrackerv2.Utils;

import android.content.Context;

import java.util.Calendar;

/**
 * Created by Rick on 1/22/2018.
 */

public class DateHelper {

    static DateHelper INSTANCE;

    public static DateHelper getInstance() {

        if (INSTANCE == null) {

            INSTANCE = new DateHelper();
        }

        return INSTANCE;
    }

    public String getCurrentMonth() {

        final Calendar calendar = Calendar.getInstance();

        String currentMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);

        return currentMonth;
    }

    public boolean checkMonth() {

        final Calendar calendar = Calendar.getInstance();

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        boolean check = false;

        if (currentDay - 1 == 0) {

            check = true;
        }

        return check;
    }
}
