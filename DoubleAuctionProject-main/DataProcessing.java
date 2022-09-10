
/**
 * DataProcessing
 */
import java.util.*;
import java.util.Map.Entry;

public class DataProcessing {

    private Users[] originalSeller;
    private Users[] originalBuyers;

    List<Point> traditionalSellerData = new ArrayList<Point>();
    List<Point> modernSellerData = new ArrayList<Point>();
    List<Point> traditionalBuyerData = new ArrayList<Point>();
    List<Point> modernBuyerData;

    List<Entry<Integer, Double>> sortingSellersPrice;
    List<Entry<Integer, Double>> sortingBuyersPrice;

    public DataProcessing(Users[] originalSeller, Users[] originalBuyers, Map<Integer, Double> sellersPriceMap,
            Map<Integer, Double> buyersPricMap, double ceilPrice) {
        this.originalSeller = originalSeller;
        this.originalBuyers = originalBuyers;

        // Sorting by Price
        sortingSellersPrice = new ArrayList<>(sellersPriceMap.entrySet());
        sortingSellersPrice.sort(Entry.comparingByValue());
        // System.out.println("\nsortingSellersPrice:");
        // sortingSellersPrice.forEach(result -> System.out.println("Key: " +
        // result.getKey() + "\t\tValue: " + result.getValue() + "\tQuantity: " +
        // originalSeller[result.getKey()].getQuantity()));

        // Sorting by Price
        sortingBuyersPrice = new ArrayList<>(buyersPricMap.entrySet());
        sortingBuyersPrice.sort(Entry.<Integer, Double>comparingByValue().reversed());
        // System.out.println("\nsortingBuyersPrice:");
        // sortingBuyersPrice.forEach(result -> System.out.println("Key: " +
        // result.getKey() + "\t\tValue: " + result.getValue() + "\tQuantity: " +
        // originalBuyers[result.getKey()].getQuantity()));

        ProcessingSellerSupply(ceilPrice);
        ProcessingBuyerDemand(ceilPrice);
    }

    private void ProcessingSellerSupply(double ceilPrice) {

        Point prevTraditional = new Point(0, 0);
        Point prevModern = new Point(0, 0);

        // Find Traditional Point
        for (int i = 0; i < sortingSellersPrice.size(); i++) {
            if (sortingSellersPrice.get(i).getValue() > prevTraditional.getY()) {
                traditionalSellerData.add(prevTraditional);
                modernSellerData.add(prevModern);
                prevTraditional = new Point(prevTraditional.getX(), sortingSellersPrice.get(i).getValue());
                prevModern = new Point(prevModern.getX(), sortingSellersPrice.get(i).getValue());

                traditionalSellerData.add(prevTraditional);
                modernSellerData.add(prevModern);
                prevTraditional = new Point(prevTraditional.getX(), prevTraditional.getY());
                prevModern = new Point(prevModern.getX(), prevModern.getY());
            }
            prevTraditional = new Point(
                    DoubleMath.add(prevTraditional.getX(),
                            originalSeller[sortingSellersPrice.get(i).getKey()].getQuantity()),
                    sortingSellersPrice.get(i).getValue());
            prevModern = new Point(
                    DoubleMath.add(prevModern.getX(),
                            originalSeller[sortingSellersPrice.get(i).getKey()].getQuantity()),
                    sortingSellersPrice.get(i).getValue());
            if (i + 1 == sortingSellersPrice.size()) {
                traditionalSellerData.add(prevTraditional);
                modernSellerData.add(prevModern);
                prevTraditional = new Point(prevTraditional.getX(), prevTraditional.getY());
                prevModern = new Point(prevModern.getX(), prevModern.getY());
            }
        }
        // Add Price Extend Line
        traditionalSellerData
                .add(new Point(traditionalSellerData.get(traditionalSellerData.size() - 1).getX(), ceilPrice));
        modernSellerData.add(new Point(modernSellerData.get(modernSellerData.size() - 1).getX(), ceilPrice));

        for (int i = 0; i < modernSellerData.size(); i++) {
            modernSellerData.get(i).setX(DoubleMath.sub(0, modernSellerData.get(i).getX()));
        }
        Collections.reverse(modernSellerData);
    }

    private void ProcessingBuyerDemand(double ceilPrice) {

        Point prev = new Point(0, ceilPrice);

        // Find Point
        for (int i = 0; i < sortingBuyersPrice.size(); i++) {
            if (sortingBuyersPrice.get(i).getValue() < prev.getY()) {
                traditionalBuyerData.add(prev);
                prev = new Point(prev.getX(), sortingBuyersPrice.get(i).getValue());

                traditionalBuyerData.add(prev);
                prev = new Point(prev.getX(), prev.getY());
            }
            prev = new Point(
                    DoubleMath.add(prev.getX(), originalBuyers[sortingBuyersPrice.get(i).getKey()].getQuantity()),
                    sortingBuyersPrice.get(i).getValue());
            if (i + 1 == sortingBuyersPrice.size()) {
                traditionalBuyerData.add(prev);
                prev = new Point(prev.getX(), prev.getY());
            }
        }

        traditionalBuyerData.add(new Point(traditionalBuyerData.get(traditionalBuyerData.size() - 1).getX(), 0));

        modernBuyerData = new ArrayList<Point>(traditionalBuyerData);

    }

    /*************************
     * Get Function
     *************************/

    public List<Point> getTraditionalSellerData() {
        return traditionalSellerData;
    }

    public List<Point> getModernSellerData() {
        return modernSellerData;
    }

    public List<Point> getTraditionalBuyerData() {
        return traditionalBuyerData;
    }

    public List<Point> getModernBuyerData() {
        return modernBuyerData;
    }

    public List<Entry<Integer, Double>> getSortingSellersPrice() {
        return sortingSellersPrice;
    }

    public List<Entry<Integer, Double>> getSortingBuyersPrice() {
        return sortingBuyersPrice;
    }

}