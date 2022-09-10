
/**
 * DataGenerator
 */
import java.util.Random;

public class DataGenerator {

    private int sellerAmount; // 賣家數量
    private int buyerAmount; // 買家數量

    private double gridPrice; // 電網價格
    // private double transCost; // 傳輸成本

    private double sellAmountCeil; // 賣家販售 量 上限
    private double sellAmountFloor; // 賣家販售 量 下限
    private double sellPriceCeil; // 賣家販售 價 上限
    private double sellPriceFloor; // 賣家販售 價 下限

    private double buyAmountCeil; // 買家販售 量 上限
    private double buyAmountFloor; // 買家販售 量 下限
    private double buyPriceCeil; // 買家販售 價 上限
    private double buyPriceFloor; // 買家販售 價 下限

    private Users[] sellers; // 賣家陣列
    private Users[] buyers; // 買家陣列

    private Random r = new Random();

    public DataGenerator() {
        this(9, 21, 8.0, 2.0, 0.1, 7.9, 5.0, 1.0, 0.1, 7.5, 4.0);
        generate();
    }

    public DataGenerator(int seed) {
        this(9, 21, 8.0, 2.0, 0.1, 7.9, 5.0, 1.0, 0.1, 7.5, 4.0);
        r.setSeed(seed);
        generate();
    }

    public DataGenerator(int sellerAmount, int buyerAmount, double gridPrice, double sellAmountCeil,
            double sellAmountFloor, double sellPriceCeil, double sellPriceFloor, double buyAmountCeil,
            double buyAmountFloor, double buyPriceCeil, double buyPriceFloor) {
        this.sellerAmount = sellerAmount;
        this.buyerAmount = buyerAmount;
        this.gridPrice = gridPrice;

        this.sellAmountCeil = sellAmountCeil;
        this.sellAmountFloor = sellAmountFloor;
        this.sellPriceCeil = sellPriceCeil;
        this.sellPriceFloor = sellPriceFloor;

        this.buyAmountCeil = buyAmountCeil;
        this.buyAmountFloor = buyAmountFloor;
        this.buyPriceCeil = buyPriceCeil;
        this.buyPriceFloor = buyPriceFloor;

        r.setSeed(1);
        generate();
    }

    private void generate() {
        sellers = new Users[sellerAmount];
        buyers = new Users[buyerAmount];

        for (int i = 0; i < sellers.length; i++) {
            sellers[i] = new Users("Seller", i, randomValue(sellAmountFloor, sellAmountCeil),
                    randomValue(sellPriceFloor, sellPriceCeil));
        }

        for (int i = 0; i < buyers.length; i++) {
            buyers[i] = new Users("Buyer", i, randomValue(buyAmountFloor, buyAmountCeil),
                    randomValue(buyPriceFloor, buyPriceCeil));
        }
    }

    private double randomValue(double floor, double ceil) {
        double diff = DoubleMath.sub(ceil, floor);
        double result = DoubleMath.add(floor, DoubleMath.mul(r.nextDouble(), diff));

        return result;
    }

    /*************************
     * Get & Set Function
     *************************/

    public int getSellerAmount() {
        return sellerAmount;
    }

    public void setSellerAmount(int sellerAmount) {
        this.sellerAmount = sellerAmount;
    }

    public int getBuyerAmount() {
        return buyerAmount;
    }

    public void setBuyerAmount(int buyerAmount) {
        this.buyerAmount = buyerAmount;
    }

    public double getGridPrice() {
        return gridPrice;
    }

    public void setGridPrice(double gridPrice) {
        this.gridPrice = gridPrice;
    }

    public double getSellAmountCeil() {
        return sellAmountCeil;
    }

    public void setSellAmountCell(double sellAmountCeil) {
        this.sellAmountCeil = sellAmountCeil;
    }

    public double getSellAmountFloor() {
        return sellAmountFloor;
    }

    public void setSellAmountFloor(double sellAmountFloor) {
        this.sellAmountFloor = sellAmountFloor;
    }

    public double getSellPriceCeil() {
        return sellPriceCeil;
    }

    public void setSellPriceCeil(double sellPriceCeil) {
        this.sellPriceCeil = sellPriceCeil;
    }

    public double getSellPriceFloor() {
        return sellPriceFloor;
    }

    public void setSellPriceFloor(double sellPriceFloor) {
        this.sellPriceFloor = sellPriceFloor;
    }

    public double getBuyAmountCeil() {
        return buyAmountCeil;
    }

    public void setBuyAmountCeil(double buyAmountCeil) {
        this.buyAmountCeil = buyAmountCeil;
    }

    public double getBuyAmountFloor() {
        return buyAmountFloor;
    }

    public void setBuyAmountFloor(double buyAmountFloor) {
        this.buyAmountFloor = buyAmountFloor;
    }

    public double getBuyPriceCeil() {
        return buyPriceCeil;
    }

    public void setBuyPriceCeil(double buyPriceCeil) {
        this.buyPriceCeil = buyPriceCeil;
    }

    public double getBuyPriceFloor() {
        return buyPriceFloor;
    }

    public void setBuyPriceFloor(double buyPriceFloor) {
        this.buyPriceFloor = buyPriceFloor;
    }

    public Users[] getSellers() {
        return sellers;
    }

    public Users[] getBuyers() {
        return buyers;
    }

}