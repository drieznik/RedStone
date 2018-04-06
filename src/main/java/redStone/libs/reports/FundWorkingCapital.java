package redStone.libs.reports;

import java.util.HashMap;

public class FundWorkingCapital extends AllReports {

    public FundWorkingCapital() {
        requests = new HashMap<>();
        requests.put("fundId", "1");
        requests.put("Authorization", "dummy");
    }
}
