package redStone.libs;

import redStone.libs.reports.AllReports;
import redStone.libs.reports.ChangeOrderLogs;
import redStone.libs.reports.FundWorkingCapital;

public enum EnumReports {
    CHANGEORDER_LOGS("Change_order_logs", ChangeOrderLogs.class, "Report_Report_ConstructionAlerts"),
    FUND_WORKING_CAPITAL("Fund_Working_Capital", FundWorkingCapital.class, "Report_Report_FundWorkingCapital");


    EnumReports(String reportName, Class<?extends AllReports> reportClass, String reportId) {
        this.reportName = reportName;
        this.reportClass = reportClass;
        this.reportId = reportId;
    }

    private String reportName;
    private Class<?extends AllReports> reportClass;
    private String reportId;

    public String getReportName() {
        return reportName;
    }

    public Class<?extends AllReports> getReportClass() {
        return reportClass;
    }

    public String getReportId() {
        return reportId;
    }


}
