package redStone.libs.reports;

import java.util.HashMap;

public abstract class AllReports {

    public HashMap<String, String> getRequests() {
        return requests;
    }

    protected HashMap<String, String> requests;

}
