package redStone.tests;

import org.apache.http.HttpConnection;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import redStone.libs.EnumReports;
import redStone.libs.HttpURLConnectionClient;
import redStone.pages.AssetsPage;
import redStone.BaseTest;
import redStone.pages.SwaggerPage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class test1 extends BaseTest{


    private SwaggerPage swaggerPage;


    @BeforeClass
    public void navigateToAssetsPage(){
        swaggerPage = getLoginPage()
                .goToAssetsPage()
                .openSwaggerTab();
    }

    @Test (dataProvider = "parseLocaleData")
    public void checkLogin(EnumReports report) throws InterruptedException {
        String actualResponse = swaggerPage.fillReportForm(report);
        Assert.assertEquals("200", actualResponse);
    }

    @DataProvider
    public Object[] parseLocaleData() {
        return EnumReports.values();
    }



}
