package poloniex.model;

/**
 * Created by kyeolee on 2017-05-16.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "poloniex_coin")
public class PoloniexCoin implements Serializable {

    @Id
    @Column(name = "coin_type")
    private String coinType;
    @Column(name = "price")
    private int price;
    @Column(name = "trade_count")
    private int tradeCount;
    @Column(name = "gap_price")
    private int gapPrice;
    @Column(name = "gap_per")
    private double gapPer;
    @Column(name = "up_per")
    private double upPer;
    @Column(name = "down_per")
    private double downPer;
    @Column(name = "sum_per")
    private double sumPer;
    @Column(name = "reg_date")
    private Date regDate = new Date();

    public double getSumPer() {
        return sumPer;
    }

    public void setSumPer(double sumPer) {
        this.sumPer = sumPer;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(int tradeCount) {
        this.tradeCount = tradeCount;
    }

    public int getGapPrice() {
        return gapPrice;
    }

    public void setGapPrice(int gapPrice) {
        this.gapPrice = gapPrice;
    }

    public double getGapPer() {
        return gapPer;
    }

    public void setGapPer(double gapPer) {
        this.gapPer = gapPer;
    }

    public double getUpPer() {
        return upPer;
    }

    public void setUpPer(double upPer) {
        this.upPer = upPer;
    }

    public double getDownPer() {
        return downPer;
    }

    public void setDownPer(double downPer) {
        this.downPer = downPer;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
}