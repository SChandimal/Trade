package com.trustatrader.trustatrader.config;

import com.trustatrader.trustatrader.common.Combination;
import com.trustatrader.trustatrader.common.Item_Model;
import com.trustatrader.trustatrader.repo.CombinationRepo;
import com.trustatrader.trustatrader.repo.item_repo;
import com.trustatrader.trustatrader.service.Item_Service;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RestController
public class BrowerTrustatrader implements InitializingBean {


    @Autowired
    private Item_Service itemService;
    @Autowired
    private item_repo itemRepo;
    @Autowired
    private CombinationRepo combinationRepo;

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void initialise() throws Exception {

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {

                System.setProperty("webdriver.gecko.driver", "/var/lib/tomcat8/geckodriver");
                FirefoxOptions options = new FirefoxOptions();
                options.setHeadless(true);

                FirefoxDriver driver = new FirefoxDriver(options);
                System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
                System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");


                try {

                    scrape("https://www.trustatrader.com", driver);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();
        }

    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Combination getCombination() {
        readWriteLock.writeLock().lock();
        Combination top = combinationRepo.getTopByStatusEquals("ADD");
        top.setStatus("SEARCHING");
        combinationRepo.saveAndFlush(top);
        readWriteLock.writeLock().unlock();
        return top;
    }

    public void scrape(String link, FirefoxDriver driver) throws InterruptedException, IOException {
        while (true) {
            Combination combination = getCombination();
            driver.get(link);
            WebElement catagory = driver.findElementByXPath("//*[@id=\"search__q\"]");
            WebElement locationFilde = driver.findElementByXPath("//*[@id=\"search__location\"]");
            WebElement serachBtn = driver.findElementByXPath("/html/body/div[1]/header/form/fieldset/div/button");
            JavascriptExecutor jse = (JavascriptExecutor) driver;

            Thread.sleep(2000);
            catagory.sendKeys(combination.getTrade());
            catagory.sendKeys(Keys.ENTER);
            locationFilde.sendKeys(combination.getLocation());
            locationFilde.sendKeys(Keys.ENTER);
            Thread.sleep(2000);
            catagory.sendKeys(Keys.ENTER);

            scrapeInerpage(combination.getLocation(), combination.getTrade(), driver);
            combination.setStatus("DONE");
            combinationRepo.saveAndFlush(combination);
        }


    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void scrapeInerpage(String location, String trade, FirefoxDriver driver) throws InterruptedException {
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
