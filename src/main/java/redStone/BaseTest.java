package redStone;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import redStone.pages.RedStoneLoginPage;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public abstract class BaseTest {

    protected RedStoneLoginPage loginPage;

    public RedStoneLoginPage getLoginPage(){
        return loginPage;
    }

    public void setChromeProperties() {
        Properties properties = new Properties();

        try {
            properties.load(BaseTest.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException i) {
        }
        String pathToChromeDriver = "";

        final String osVersion = properties.getProperty("prefered.os.version");
        switch (osVersion) {
            case "Win":
                pathToChromeDriver = new File("").getAbsolutePath()
                        + File.separator + "drivers" + File.separator + "chromedriver.exe";
                break;
            case "Mac":
                pathToChromeDriver = new File("").getAbsolutePath() + File.separator
                        + "drivers" + File.separator + "chromedriver";
                break;

            default:
                throw new AssertionError("Platform is undefined");

        }
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);

    }

    @BeforeSuite
    public void setProperties(){
        //
    }

    @BeforeClass
    public void setupTests(){
        System.out.println("BeforeClass");
        setChromeProperties();
        loginPage = new RedStoneLoginPage( new ChromeDriver()).navigateToLoginPage("http://rsequity-test.cloudapp.net/Account");
    }


    @AfterClass
    public void close(){
        loginPage.getDriver().quit();
    }


}
