import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

public class mainTest {
    public static void main(String[] args) throws IOException {

        clearTheFile();
        appendStrToFile("==============================\n");
        appendStrToFile("             Start            \n");
        appendStrToFile("==============================\n");

        /************************
         * Generate Data
         ************************/

        // DataGenerator dataGenerator = new DataGenerator();
        // DataGenerator dataGenerator = new DataGenerator(1);
        DataGenerator dataGenerator = new DataGenerator(10, 20, 8.0, 2.0, 0.1, 7.9,
                5.0, 1.0, 0.1, 7.5, 4.0);
        Users[] originalSeller;
        Users[] originalBuyers;
        Map<Integer, Double> sellersPriceMap = new HashMap<>();
        Map<Integer, Double> buyersPricMap = new HashMap<>();

        originalSeller = dataGenerator.getSellers();
        // Set Seller's Price to Map
        for (int i = 0; i < originalSeller.length; i++) {
            sellersPriceMap.put(i, originalSeller[i].getPrice());
        }
        // Print Result
        appendStrToFile("\n[+] Generate Seller Data\n");
        for (Map.Entry<Integer, Double> entry : sellersPriceMap.entrySet()) {
            appendStrToFile("\tSeller: " + entry.getKey() + "\tQuantity: "
                    + originalSeller[entry.getKey()].getQuantity()
                    + "\tPrice: " + entry.getValue() + "\n");
        }

        originalBuyers = dataGenerator.getBuyers();
        // Set Buyer's Price to Map
        for (int i = 0; i < originalBuyers.length; i++) {
            buyersPricMap.put(i, originalBuyers[i].getPrice());
        }
        // Print Result
        appendStrToFile("\n[+] Generate Buyer Data\n");
        for (Map.Entry<Integer, Double> entry : buyersPricMap.entrySet()) {
            appendStrToFile(
                    "\tBuyer: " + entry.getKey() + "\tQuantity: " + originalBuyers[entry.getKey()].getQuantity()
                            + "\tPrice: " + entry.getValue() + "\n");
        }

        /************************
         * For Loop 執行次數
         ************************/
        for (int i = 0; i < 20; i++) { // 改 i 調整執行次數

            appendStrToFile("\n==============================\n");
            appendStrToFile("             No " + (i + 1) + "            \n");
            appendStrToFile("==============================\n");
            /************************
             * Data Processing
             ************************/

            DataProcessing dataProcessing = new DataProcessing(originalSeller, originalBuyers, sellersPriceMap,
                    buyersPricMap, dataGenerator.getGridPrice());
            List<Point> traditionalSellerSupply = dataProcessing.getTraditionalSellerData();
            List<Point> modernSellerSupply = dataProcessing.getModernSellerData();
            List<Point> traditionalBuyerDemand = dataProcessing.getTraditionalBuyerData();
            List<Point> modernBuyerDemand = dataProcessing.getModernBuyerData();
            List<Entry<Integer, Double>> sortingSellersPrice = dataProcessing.getSortingSellersPrice();
            List<Entry<Integer, Double>> sortingBuyersPrice = dataProcessing.getSortingBuyersPrice();

            // Print Result
            /*
             * System.out.println();
             * System.out.println("\t[+] Sorting By Price\n");
             * System.out.println("\t- sortingSellersPrice:\n");
             * sortingSellersPrice.forEach(result -> System.out.println("\t\tKey: " +
             * result.getKey() + "\t\tValue: " + result.getValue() + "\tQuantity: " +
             * originalSeller[result.getKey()].getQuantity()));
             * System.out.println();
             * System.out.println("\t- sortingBuyersPrice:\n");
             * sortingBuyersPrice.forEach(result -> System.out.println("\t\tKey: " +
             * result.getKey() + "\t\tValue: " + result.getValue() + "\tQuantity: " +
             * originalBuyers[result.getKey()].getQuantity()));
             */
            // for (int i = 0; i < modernSellerSupply.size(); i++) {
            // System.out.println(modernSellerSupply.get(i).getX() + "\t" +
            // modernSellerSupply.get(i).getY());
            // }

            // Print Chart Point Result
            // System.out.println();
            // for (int i = 0; i < traditionalSellerSupply.size(); i++) {
            // System.out.println("X: " + traditionalSellerSupply.get(i).getX() + "\t\tY: "
            // + traditionalSellerSupply.get(i).getY());
            // }
            // System.out.println("traditionalSellerSupply");
            // for (int i = 0; i < traditionalSellerSupply.size(); i++) {
            // System.out.println("X: " + traditionalSellerSupply.get(i).getX() + "\t\tY: "
            // + traditionalSellerSupply.get(i).getY());
            // }

            /************************
             * Draw Chart
             ************************/

            appendStrToFile("\n\t[+] Draw Chart & Save to Image\n");
            DrawChart drawChart = new DrawChart(traditionalSellerSupply, traditionalBuyerDemand, modernSellerSupply,
                    modernBuyerDemand);

            double floor;
            double ceil;
            double quantity;

            // Floor
            if (traditionalSellerSupply.get(1).getY() < traditionalBuyerDemand.get(traditionalBuyerDemand.size() - 2)
                    .getY()) {
                floor = traditionalSellerSupply.get(1).getY();
            } else {
                floor = traditionalBuyerDemand.get(traditionalBuyerDemand.size() - 2).getY();
            }

            // Ceiling
            if (traditionalSellerSupply.get(traditionalSellerSupply.size() - 2).getY() > traditionalBuyerDemand.get(1)
                    .getY()) {
                ceil = traditionalSellerSupply.get(traditionalSellerSupply.size() - 2).getY();
            } else {
                ceil = traditionalBuyerDemand.get(1).getY();
            }

            // Quantity
            if (traditionalSellerSupply.get(traditionalSellerSupply.size() - 1).getX() > traditionalBuyerDemand
                    .get(traditionalBuyerDemand.size() - 1).getX()) {
                quantity = traditionalSellerSupply.get(traditionalSellerSupply.size() - 1).getX();
            } else {
                quantity = traditionalBuyerDemand.get(traditionalBuyerDemand.size() - 1).getX();
            }

            drawChart.draw(floor, ceil, quantity, i);

            /*
             * System.out.println();
             * System.out.println("\t[+] Find Intersect Point\n");
             */
            Point traditionalIntersectPoint = drawChart.getTraditionalIntersectPoint();
            Point modernIntersectPoint = drawChart.getModernIntersectPoint();
            /*
             * System.out.println("\t\tTraditional Intersect Point: (" +
             * traditionalIntersectPoint.getX() + ", "
             * + traditionalIntersectPoint.getY() + ")");
             * System.out.println(
             * "\t\tModern Intersect Point: (" + modernIntersectPoint.getX() + ", " +
             * modernIntersectPoint.getY()
             * + ")");
             */
            appendStrToFile("\n\n\t[+] Calculate Profit\n");

            List<Point> shiftedSellerSupply = drawChart.getShiftedSellerSupply();

            Profit profit = new Profit();
            appendStrToFile("\n\t\tTraditional Profit: " + profit.getTraditionalProfit(traditionalIntersectPoint));

            appendStrToFile("\n\t\t\t傳統成交量: " + traditionalIntersectPoint.getX());
            appendStrToFile("\n\t\t\t傳統成交價: " + traditionalIntersectPoint.getY());

            profit.calculateModernProfit(modernBuyerDemand, shiftedSellerSupply);
            appendStrToFile("\n\n\t\tModern Profit: ");
            appendStrToFile(
                    "\n\t\t\tSeller Profit = " + profit.getModernSellerProfit());
            appendStrToFile(
                    "\n\t\t\tBuyer Profit = " + profit.getModernBuyerProfit());
            appendStrToFile(
                    "\n\t\t\tTotal Profit = " + profit.getModernProfit());

            appendStrToFile("\n\n\t\t成交量: " + shiftedSellerSupply.get(shiftedSellerSupply.size() - 1).getX());
            appendStrToFile("\n\n\t\t均價: " + profit.getPriceAvg());
            appendStrToFile("\n\t\t最高: " + profit.getPriceMax());
            appendStrToFile("\n\t\t最低: " + profit.getPriceMin());

            /************************
             * Pricing Function
             ************************/

            double r_deal = 0.1; // r =================> 需求程度
            double r_notDeal = 0.4; // r =================> 需求程度

            // Seller
            for (Map.Entry<Integer, Double> entry : sellersPriceMap.entrySet()) {
                // 沒成交 P_(t,n+1) = P_(t,n) - r * (P_(t,n) - P_(t,n)^min)
                if (entry.getValue() > profit.getPriceMax()) {
                    originalSeller[entry.getKey()].setPrice(DoubleMath.sub(entry.getValue(),
                            DoubleMath.mul(r_notDeal, DoubleMath.sub(entry.getValue(), profit.getPriceMin()))));
                } else { // 成交 P_(t,n+1) = P_(t,n) - r * (P_(t,n) - P_(t,n)^avg)
                    originalSeller[entry.getKey()].setPrice(DoubleMath.sub(entry.getValue(),
                            DoubleMath.mul(r_deal, DoubleMath.sub(entry.getValue(), profit.getPriceAvg()))));
                }
            }

            // Buyer
            for (Map.Entry<Integer, Double> entry : buyersPricMap.entrySet()) {
                // 沒成交 P_(t,n+1) = P_(t,n) - r * (P_(t,n) - P_(t,n)^max)
                if (entry.getValue() < profit.getPriceMin()) {
                    originalBuyers[entry.getKey()].setPrice(DoubleMath.sub(entry.getValue(),
                            DoubleMath.mul(r_notDeal, DoubleMath.sub(entry.getValue(), profit.getPriceMax()))));
                } else { // 成交 P_(t,n+1) = P_(t,n) - r * (P_(t,n) - P_(t,n)^avg)
                    originalBuyers[entry.getKey()].setPrice(DoubleMath.sub(entry.getValue(),
                            DoubleMath.mul(r_deal, DoubleMath.sub(entry.getValue(), profit.getPriceAvg()))));
                }
            }

            // Reset Seller's Price to Map
            sellersPriceMap.clear();
            for (int j = 0; j < originalSeller.length; j++) {
                sellersPriceMap.put(j, originalSeller[j].getPrice());
            }
            // Print Result
            appendStrToFile("\n\n\n[+] New Seller Data\n");
            for (Map.Entry<Integer, Double> entry : sellersPriceMap.entrySet()) {
                appendStrToFile("\tSeller: " + entry.getKey() + "\tQuantity: "
                        + originalSeller[entry.getKey()].getQuantity()
                        + "\tPrice: " + entry.getValue() + "\n");
            }

            // Reset Buyer's Price to Map
            buyersPricMap.clear();
            for (int j = 0; j < originalBuyers.length; j++) {
                buyersPricMap.put(j, originalBuyers[j].getPrice());
            }
            // Print Result
            appendStrToFile("\n[+] New Buyer Data\n");
            for (Map.Entry<Integer, Double> entry : buyersPricMap.entrySet()) {
                appendStrToFile(
                        "\tBuyer: " + entry.getKey() + "\tQuantity: "
                                + originalBuyers[entry.getKey()].getQuantity()
                                + "\tPrice: " + entry.getValue() + "\n");
            }

        }
        appendStrToFile("\n\n==============================\n");
        appendStrToFile("  Mission Completed !!!!!!!   \n");
        appendStrToFile("==============================\n\n");
    }

    public static void appendStrToFile(String str) {
        try {
            System.out.print(str);
            BufferedWriter out = new BufferedWriter(new FileWriter("./output/log.txt", true));

            out.write(str);
            out.close();
        } catch (IOException e) {
            System.out.println("Exception occurred " + e);
        }
    }

    public static void clearTheFile() throws IOException {
        FileWriter fw = new FileWriter("./output/log.txt", false);
        PrintWriter pw = new PrintWriter(fw, false);
        pw.flush();
        pw.close();
        fw.close();
    }
}
