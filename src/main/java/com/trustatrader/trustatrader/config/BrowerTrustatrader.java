package com.trustatrader.trustatrader.config;

import com.trustatrader.trustatrader.common.Item_Model;
import com.trustatrader.trustatrader.repo.item_repo;
import com.trustatrader.trustatrader.service.Item_Service;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class BrowerTrustatrader implements InitializingBean {
    private FirefoxDriver driver = null;

    @Autowired
    private Item_Service itemService;
    @Autowired
    private item_repo itemRepo;

    public void initialise() throws Exception {
        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);
        driver = new FirefoxDriver();
        scrape("https://www.trustatrader.com");
    }

    public void scrape(String link) throws InterruptedException, IOException {
        String trade[] = {"Electrician",
                "Locksmith",
                "Roofer",
                "Landscaping",
                "Paving",
                "Plumber",
                "Heating",
                "Boilers",
                "Scaffolding",
                "Painter",
                "Decorater",
                "Plasterer",
                "Builder",
                "Construction",
                "Drainage",
                "Guttering",
                "Cleaning",
                "Removals",
                "Bathrooms",
                "Kitchens",
                "Gardening",
                "Home Improvements",
                "Fencing",
                "Gates",
                "Garages",
                "Loft Conversions",
                "Pest Control",
                "Skip Hire",
                "Grab Hire",
                "Groundwor"};
        String locations[] = {"London",
                "Birmingham",
                "Glasgow",
                "Leeds",
                "Bristol",
                "Liverpool",
                "Manchester",
                "Sheffield",
                "Edinburgh",
                "Cardiff",
                "Leicester",
                "Stoke-on-Trent",
                "Bradford",
                "Coventry",
                "Nottingham",
                "Hampshire",
                "Hertfordshire",
                "Wiltshire",
                "Dorset",
                "Yorkshi"};


        for (String item : trade) {
            for (String location : locations) {
                driver.get(link);
                WebElement catagory = driver.findElementByXPath("//*[@id=\"search__q\"]");
                WebElement locationFilde = driver.findElementByXPath("//*[@id=\"search__location\"]");
                WebElement serachBtn = driver.findElementByXPath("/html/body/div[1]/header/form/fieldset/div/button");
                JavascriptExecutor jse = (JavascriptExecutor) driver;
                System.err.println("+++++++++++++++++++++++++++++" + item);
                System.err.println("++++++++++++++++++++++++++++++++" + location);
                Thread.sleep(2000);
                catagory.sendKeys(item);
                catagory.sendKeys(Keys.ENTER);
                locationFilde.sendKeys(location);
                locationFilde.sendKeys(Keys.ENTER);
                Thread.sleep(2000);
                catagory.sendKeys(Keys.ENTER);

                scrapeInerpage(location, item);
            }
        }
    }

    private void scrapeInerpage(String location, String trade) throws InterruptedException {
        while (true) {
            Item_Model model = null;
            Thread.sleep(5000);
            try {
                WebElement allList = driver.findElementByXPath("/html/body/div[1]/section/div/div[2]/ul");
                for (WebElement li : allList.findElements(By.xpath("./*"))) {
                    if (li.getAttribute("class").equalsIgnoreCase("profile-card")) {
                        System.err.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                        WebElement webElement = li.findElements(By.xpath("./*")).get(0);
                        WebElement itemName = webElement.findElements(By.xpath("./*")).get(1);
                        WebElement phoneNum = webElement.findElements(By.xpath("./*")).get(2);
                        String name = itemName.findElement(By.tagName("h3")).getAttribute("innerText");
                        WebElement num1 = phoneNum.findElements(By.xpath("./*")).get(0).findElement(By.tagName("span"));
                        String innerText2 = "";
                        try {
                            WebElement num2 = phoneNum.findElements(By.xpath("./*")).get(1).findElement(By.tagName("span"));
                            innerText2 = num2.getAttribute("innerText");
                        } catch (IndexOutOfBoundsException e) {
                        }
                        String innerText1 = num1.getAttribute("innerText");
                        System.out.println("Item Name :" + trade);
                        System.out.println("Location Name :" + location);
                        System.out.println("Header Name :" + name);
                        System.out.println("Phone Number 1 :" + innerText1);
                        System.out.println("Phone Number 2 :" + innerText2);

                        model = new Item_Model();
                        model.setItemTrade(trade);
                        model.setItemLocation(location);
                        model.setItemName(name);
                        model.setPhoneNumber1(innerText1);
                        model.setPhoneNumber2(innerText2);
                        itemRepo.save(model);
                        System.out.println("================================================================");

                    }
                }
            } catch (NoSuchElementException e) {
                break;
            }
            try {
                try {
                    WebElement nextBtn = driver.findElementByXPath("/html/body/div[1]/section/div/nav/a[2]");
                    nextBtn.click();
                    Thread.sleep(2000);
                } catch (NoSuchElementException e) {
                    break;
                }
            } catch (ElementClickInterceptedException e) {
                break;
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initialise();
    }
}
