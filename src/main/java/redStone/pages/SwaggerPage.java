package redStone.pages;

import org.apache.tools.ant.taskdefs.PathConvert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import redStone.libs.EnumReports;
import redStone.libs.reports.AllReports;
import org.testng.Assert;

import java.util.Map;
import java.util.regex.Pattern;

public class SwaggerPage extends BasePage{

    @FindBy(css = "#Report_endpoint_list")
    WebElement reportsList;

    @FindBy(css = ".operation-params")
    WebElement paramsList;



    String listItemLocator = "li#%s";


    public SwaggerPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#Report_endpoint_list")));
    }

    private WebElement expandReportForm(EnumReports report){
        WebElement reportForm = reportsList.findElement(By.cssSelector(String.format(listItemLocator, report.getReportId())));
        reportForm.findElement(By.xpath("./div/h3//a")).click();
        return wait.until(ExpectedConditions.visibilityOf(reportForm.findElement(By.cssSelector("div.content"))));

    }

    public String fillReportForm(EnumReports reports) throws InterruptedException {
        WebElement formContent = expandReportForm(reports);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", formContent);
        Thread.sleep(500);
        AllReports allReports = null;
        try {
             allReports = reports.getReportClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(allReports);


        for (Map.Entry<String, String> entry: allReports.getRequests().entrySet()){
            WebElement requestForm = formContent.findElement(By.cssSelector(".operation-params"));
            requestForm.findElement(By.xpath(".//*[@name='"+ entry.getKey() + "']")).sendKeys(entry.getValue());
        }

        formContent.findElement(By.className("submit")).click();

        return wait.until(ExpectedConditions.visibilityOf(formContent.findElement(By.xpath(".//div[@class='block response_code']")))).getText().trim();

    }
}
