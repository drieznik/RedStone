package redStone.libs.reports;

import java.util.HashMap;

public class ChangeOrderLogs extends AllReports {

    public ChangeOrderLogs() {
        requests = new HashMap<>();
        requests.put("assets", "9,89");
        requests.put("Authorization", "dummy");
    }
}
