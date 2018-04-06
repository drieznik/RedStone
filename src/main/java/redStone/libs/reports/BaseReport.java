package redStone.libs.reports;

import java.util.HashMap;

public class BaseReport {
    public HashMap<String, String> getRequests() {
        return requests;
    }

    protected HashMap<String, String> requests;
    protected String routeName;
    protected int reportId;

    public String getRouteName() {
        return routeName;
    }

    public int getReportId() {
        return reportId;
    }

    public void setRequests(HashMap<String, String> requests) {
        this.requests = requests;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
}
