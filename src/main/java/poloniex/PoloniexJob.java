package poloniex;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import poloniex.constant.PoloniexConstant;
import poloniex.model.PoloniexCoin;
import util.CommonUtil;
import util.HibernateUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by kyeolee on 2017-05-16.
 */
public class PoloniexJob implements org.quartz.Job {

    private static Map<String, Integer> tempPrice = new HashMap<>();
    private static Map<String, Double> upPer = new HashMap<>();
    private static Map<String, Double> downPer = new HashMap<>();
    private static Map<String, Double> sumPer = new HashMap<>();

    private String[] coinTypes = PoloniexConstant.coinTypes;

    private WebDriver driver = PoloniexQuartz.driver;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            for (String coin : coinTypes) {
                int price = (int) (Double.parseDouble(driver.findElement(By.cssSelector("#marketRowbtc_" + coin + " > td.price")).getText()) * 100000000);
                int tradeCount = (int) (Double.parseDouble(driver.findElement(By.cssSelector("#marketRowbtc_" + coin + " > td.volume.sorting_1")).getText()));

                /* tradeCount more than 500 bitCoin */
                if (tempPrice.get(coin) != null && tradeCount > 500) {
                    int gapPrice = price - tempPrice.get(coin);
                    double gapPer = Math.round(((100.0 * gapPrice) / price) * 100d) / 100d;

                    double tempDownPer;
                    double tempUpPer;

                    if (gapPer < 0.0) {
                        double preDownPer = downPer.get(coin);

                        tempDownPer = preDownPer + gapPer;

                        if (gapPer <= -0.5)
                            upPer.put(coin, 0.00);

                        downPer.put(coin, tempDownPer);
                    } else if (gapPer > 0.0) {
                        double preUpPer = upPer.get(coin);

                        tempUpPer = preUpPer + gapPer;

                        if (gapPer >= 0.5)
                            downPer.put(coin, 0.00);

                        upPer.put(coin, tempUpPer);
                    }

                    PoloniexCoin poloniexCoin = new PoloniexCoin();
                    poloniexCoin.setCoinType(coin);
                    poloniexCoin.setPrice(price);
                    poloniexCoin.setGapPrice(gapPrice);
                    poloniexCoin.setTradeCount(tradeCount);
                    poloniexCoin.setGapPer(gapPer);
                    poloniexCoin.setUpPer(upPer.get(coin));
                    poloniexCoin.setDownPer(downPer.get(coin));
                    poloniexCoin.setSumPer(poloniexCoin.getUpPer() + poloniexCoin.getDownPer());

                    if (sumPer.get(coin) == null || (sumPer.get(coin) != poloniexCoin.getSumPer())) {
                        if (poloniexCoin.getSumPer() < -5.0)
                            CommonUtil.sendTelegram("▽--" + coin + "--sum : " + percentStr(poloniexCoin.getSumPer()) + "_d : " + percentStr(downPer.get(coin)) + "_u : " + percentStr(upPer.get(coin)) + "_p : " + price + "_t : " + tradeCount);
                        else if (poloniexCoin.getSumPer() > 5.0)
                            CommonUtil.sendTelegram("▲--" + coin + "--sum : " + percentStr(poloniexCoin.getSumPer()) + "_d : " + percentStr(downPer.get(coin)) + "_u : " + percentStr(upPer.get(coin)) + "_p : " + price + "_t : " + tradeCount);

                        sumPer.put(coin, poloniexCoin.getSumPer());
                    }

                    session.save(poloniexCoin);
                } else {
                    upPer.put(coin, 0.00);
                    downPer.put(coin, 0.00);
                }

                tempPrice.put(coin, price);
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.close();
        }
    }

    private String percentStr(double d) {
        return String.valueOf(Math.round(d * 100d) / 100d);
    }
}