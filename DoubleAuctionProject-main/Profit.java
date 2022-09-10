import java.util.List;

public class Profit {

    public Profit() {
    }

    public double getTraditionalProfit(Point intersectPoint) {
        double profit = 0;

        profit = DoubleMath.mul(intersectPoint.getX(), intersectPoint.getY());

        return profit;
    }

    public double totalProfit;
    public double sellerProfit;
    public double buyerProfit;
    public double priceAvg;
    public double priceMax;
    public double priceMin;

    public void calculateModernProfit(List<Point> modernBuyerDemand, List<Point> shiftedSellerSupply) {
        totalProfit = 0;
        sellerProfit = 0;
        buyerProfit = 0;
        priceMax = 0;

        // calculate total profit
        double volume = shiftedSellerSupply.get(shiftedSellerSupply.size() - 1).getX();
        for (int i = 2; i < modernBuyerDemand.size(); i += 2) {
            totalProfit = DoubleMath.add(totalProfit,
                    DoubleMath.mul(
                            DoubleMath.sub(modernBuyerDemand.get(i).getX(), modernBuyerDemand.get(i - 1).getX()),
                            modernBuyerDemand.get(i).getY()));
            if (volume <= modernBuyerDemand.get(i).getX()) {
                break;
            }
        }

        // calculate seller's profit & price max
        for (int i = 2; i < shiftedSellerSupply.size() - 1; i += 2) {
            if (shiftedSellerSupply.get(i - 1).getX() <= 0 && shiftedSellerSupply.get(i).getX() <= 0) {
                continue;
            } else if (shiftedSellerSupply.get(i - 1).getX() <= 0 && shiftedSellerSupply.get(i).getX() > 0) {
                if (priceMax == 0) {
                    priceMax = shiftedSellerSupply.get(i).getY();
                }
                sellerProfit = DoubleMath.add(sellerProfit,
                        DoubleMath.mul(shiftedSellerSupply.get(i).getX(), shiftedSellerSupply.get(i).getY()));
            } else {
                if (priceMax == 0) {
                    priceMax = shiftedSellerSupply.get(i).getY();
                }
                sellerProfit = DoubleMath.add(sellerProfit, DoubleMath.mul(
                        DoubleMath.sub(shiftedSellerSupply.get(i).getX(), shiftedSellerSupply.get(i - 1).getX()),
                        shiftedSellerSupply.get(i).getY()));
            }
        }

        // price avg
        priceAvg = DoubleMath.div(sellerProfit, volume);
        // price min
        priceMin = shiftedSellerSupply.get(shiftedSellerSupply.size() - 2).getY();
        // buyer's profit
        buyerProfit = DoubleMath.sub(totalProfit, sellerProfit);
    }

    /*************************
     * Get Function
     *************************/

    public double getModernProfit() {
        return totalProfit;
    }

    public double getModernSellerProfit() {
        return sellerProfit;
    }

    public double getModernBuyerProfit() {
        return buyerProfit;
    }

    public double getPriceAvg() {
        return priceAvg;
    }

    public double getPriceMax() {
        return priceMax;
    }

    public double getPriceMin() {
        return priceMin;
    }
}
