package rick.expensestrackerv2.Utils;

/**
 * Created by Rick on 1/22/2018.
 */

public class Injection {

    public static FirebaseDatabaseHelper getFirebaseDatabaseClassInstance() {
        return FirebaseDatabaseHelper.getDatabaseClassInstance();
    }

    public static DateHelper getDateInstance() {
        return DateHelper.getInstance();
    }
}
