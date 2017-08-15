package poloniex;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by kyeolee on 2017-05-16.
 */
public class PoloniexQuartz {

    public static WebDriver driver;

    public static void main(String[] args) throws Exception {
        /* Chrome Driver Setting */
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.get("https://www.poloniex.com/exchange#btc_via");

        /* One second Delay */
        long startTime = System.currentTimeMillis();
        while (false || (System.currentTimeMillis() - startTime) < 1000) {
        }

        driver.findElement(By.cssSelector("div.tableHead > div.tools > i.fa.fa-cog")).click();
        driver.findElement(By.xpath("//ul[@id='rowButtons']/li[5]/button")).click();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        JobDetail job = newJob(PoloniexJob.class)
                .withIdentity("job1", "group1")
                .build();

        /* 30seconds Interval */
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(30)
                        .repeatForever())
                .build();

        scheduler.scheduleJob(job, trigger);
    }
}
