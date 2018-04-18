package redStone.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import redStone.libs.HttpURLConnectionClient;
import redStone.libs.reports.BaseReport;

import java.io.IOException;

public class test2 {

    private HttpURLConnectionClient conn = new HttpURLConnectionClient();

    @BeforeClass
    public void setupTests() throws IOException {
        System.out.println("BeforeClass");
        conn.initialize();
        conn.loginToRedStone();
        conn.getReportsList();
        conn.getAssetsList();
    }

    @Test (dataProvider =  "populateReportsList")
    public void validateReportsResponse(BaseReport report) throws IOException {
        Assert.assertEquals( conn.generateReport(report), 200);
    }


    @Test
    public void testMe() throws IOException {
        Assert.assertEquals( conn.generatePArticularReport(), 200);
    }

    @DataProvider
    public Object[] populateReportsList(){
       return conn.getReportsArray().toArray();
    }
}
