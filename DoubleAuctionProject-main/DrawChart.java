import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.Color;

public class DrawChart {

    private List<Point> traditionalSellerSupply = new ArrayList<Point>();
    private List<Point> traditionalBuyerDemand = new ArrayList<Point>();
    private List<Point> modernSellerSupply = new ArrayList<Point>();
    private List<Point> modernBuyerDemand = new ArrayList<Point>();
    private List<Point> shiftedSellerSupply = new ArrayList<Point>();

    Point traditionalIntersectPoint = new Point();
    Point modernIntersectPoint = new Point();

    public DrawChart(List<Point> traditionalSellerSupply, List<Point> traditionalBuyerDemand,
            List<Point> modernSellerSupply, List<Point> modernBuyerDemand) {
        this.traditionalSellerSupply = traditionalSellerSupply;
        this.traditionalBuyerDemand = traditionalBuyerDemand;
        this.modernSellerSupply = modernSellerSupply;
        this.modernBuyerDemand = modernBuyerDemand;

    }

    public void draw(double floor, double ceil, double quantity, int index) throws IOException {

        drawAndSaveToFile("Traditional", floor, ceil, quantity, index);
        drawAndSaveToFile("Modern", floor, ceil, quantity, index);

        // System.out.println("\tDone !!!");
    }

    private void drawAndSaveToFile(String functionType, double floor, double ceil, double quantity, int index)
            throws IOException {
        System.out.println(
                "\t\t- Save Chart \"" + functionType + "_Supply_and_Demand_LineChart_" + (index + 1)
                        + "\" to output Folder......");

        List<Point> seller = new ArrayList<Point>();
        List<Point> buyer = new ArrayList<Point>();
        if (functionType == "Traditional") {
            seller = traditionalSellerSupply;
            buyer = traditionalBuyerDemand;
        } else if (functionType == "Modern") {
            seller = modernSellerSupply;
            buyer = modernBuyerDemand;
        }

        // Supply
        XYSeries supply = new XYSeries("Supply");
        for (int i = 0; i < seller.size(); i++) {
            supply.add(seller.get(i).getX(), seller.get(i).getY());
        }

        // Demand
        XYSeries demand = new XYSeries("Demand");
        for (int i = 0; i < buyer.size(); i++) {
            demand.add(buyer.get(i).getX(), buyer.get(i).getY());
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(supply);
        dataset.addSeries(demand);

        // Find Intersect Point
        XYSeries intersect = new XYSeries("Intersect");
        XYSeries shifted = new XYSeries("Shifted");
        Point intersectPoint = new Point(0, 0);
        if (functionType == "Traditional") {
            intersectPoint = findTraditionalIntersectPoint();
        } else if (functionType == "Modern") {
            double Qmv = getQFunction();

            try {
                System.out.print("\t\tQmv: " + Qmv);
                BufferedWriter out = new BufferedWriter(new FileWriter("./output/log.txt", true));

                out.write("\t\tQmv: " + Qmv);
                out.close();
            } catch (IOException e) {
                System.out.println("Exception occurred " + e);
            }

            // Shifted Line
            Collections.reverse(modernSellerSupply);
            for (int i = 0; i < modernSellerSupply.size(); i++) {
                shifted.add(DoubleMath.add(modernSellerSupply.get(i).getX(), Qmv), modernSellerSupply.get(i).getY());
                shiftedSellerSupply.add(new Point(DoubleMath.add(modernSellerSupply.get(i).getX(), Qmv),
                        modernSellerSupply.get(i).getY()));
            }

            intersectPoint = findModernIntersectPoint();
        }
        intersect.add(intersectPoint.getX(), intersectPoint.getY());

        dataset.addSeries(intersect);
        if (!shifted.getItems().isEmpty()) {
            dataset.addSeries(shifted);
        }

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                functionType + " Supply and Demand LineChart",
                "Quantity",
                "Price",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        XYPlot xyPlot = (XYPlot) xylineChart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // Setting Supply Line Blue
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesPaint(0, Color.blue);

        // Setting Demand Line Red
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesPaint(1, Color.red);

        // Setting Intersect Point Location
        renderer.setSeriesLinesVisible(2, false);
        renderer.setSeriesShapesVisible(2, true);
        xyPlot.setRenderer(renderer);

        // Setting Shifted Supply Line
        renderer.setSeriesLinesVisible(3, true);
        renderer.setSeriesShapesVisible(3, false);
        renderer.setSeriesPaint(3, new Color(30, 144, 255));

        // Setting Intersect Point Message
        XYTextAnnotation annotation = new XYTextAnnotation("(" +
                intersectPoint.getX() + ", " + intersectPoint.getY() + ")",
                intersectPoint.getX(), intersectPoint.getY() - 0.1);
        xyPlot.addAnnotation(annotation);

        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);

        // Change X-Axis Range
        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setRange(0 - quantity - 0.2, quantity + 0.2);
        domain.setTickUnit(new NumberTickUnit(2));
        // domain.setVerticalTickLabels(true);

        // Change Y-Axis Range
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
        range.setRange(floor - 0.2, ceil + 0.2);
        range.setTickUnit(new NumberTickUnit(1));

        // Save Chart to Image
        int width = 640; /* Width of the image */
        int height = 480; /* Height of the image */

        String outputDirectory = "./output";
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            System.out.println("新建 output 資料夾中...");
            directory.mkdir();
        }

        File XYChart = new File("./output/" + functionType + "_Supply_and_Demand_LineChart_" + (index + 1) + ".jpeg");
        ChartUtils.saveChartAsJPEG(XYChart, xylineChart, width, height);
    }

    private Point findTraditionalIntersectPoint() {
        int currentSeller = 2;
        int prevSeller = 2;
        int currentBuyer = 2;
        int prevBuyer = 2;

        double sellerRemain = 0;
        double buyerRemain = 0;
        double temp = 0;
        double quantity = 0;

        while (traditionalSellerSupply.get(currentSeller).getY() < traditionalBuyerDemand.get(currentBuyer).getY()) {
            if (currentSeller == prevSeller && temp != 0) {
                sellerRemain = temp;
            } else {
                sellerRemain = DoubleMath.sub(traditionalSellerSupply.get(currentSeller).getX(),
                        traditionalSellerSupply.get(currentSeller - 1).getX());
                prevSeller = currentSeller;
            }
            if (currentBuyer == prevBuyer && temp != 0) {
                buyerRemain = temp;
            } else {
                buyerRemain = DoubleMath.sub(traditionalBuyerDemand.get(currentBuyer).getX(),
                        traditionalBuyerDemand.get(currentBuyer - 1).getX());
                prevBuyer = currentBuyer;
            }

            if (sellerRemain > buyerRemain) {
                temp = DoubleMath.sub(sellerRemain, buyerRemain);
                quantity = DoubleMath.add(quantity, buyerRemain);
                currentBuyer += 2;
            } else if (sellerRemain < buyerRemain) {
                temp = DoubleMath.sub(buyerRemain, sellerRemain);
                quantity = DoubleMath.add(quantity, sellerRemain);
                currentSeller += 2;
            } else {
                temp = 0;
                quantity = DoubleMath.add(quantity, sellerRemain);
                currentSeller += 2;
                currentBuyer += 2;
            }
        }

        if (currentSeller != prevSeller) {
            traditionalIntersectPoint = new Point(quantity, traditionalBuyerDemand.get(currentBuyer).getY());
        } else {
            if (traditionalBuyerDemand.get(currentBuyer).getY() == traditionalSellerSupply.get(currentSeller).getY()) {
                traditionalIntersectPoint = new Point(
                        Math.min(traditionalBuyerDemand.get(currentBuyer).getX(),
                                traditionalSellerSupply.get(currentSeller).getX()),
                        traditionalSellerSupply.get(currentSeller).getY());
            } else {
                traditionalIntersectPoint = new Point(quantity, traditionalSellerSupply.get(currentSeller).getY());
            }
        }
        // System.out.println("Traditional Intersect Point: (" +
        // traditionalIntersectPoint.getX() + ", " + traditionalIntersectPoint.getY() +
        // ")");

        return traditionalIntersectPoint;
    }

    private Point findModernIntersectPoint() {

        Collections.reverse(modernBuyerDemand);
        int currentDemandIndex = 0;

        for (int i = 2; i < shiftedSellerSupply.size() - 1; i += 2) {
            if (shiftedSellerSupply.get(i).getX() >= 0) {
                while (shiftedSellerSupply.get(i).getY() < modernBuyerDemand.get(currentDemandIndex).getY()
                        && shiftedSellerSupply.get(i).getY() < modernBuyerDemand.get(currentDemandIndex + 1).getY()) {
                    currentDemandIndex += 2;
                }
                if (shiftedSellerSupply.get(i).getX() == modernBuyerDemand.get(currentDemandIndex).getX()) {
                    modernIntersectPoint = new Point(shiftedSellerSupply.get(i).getX(),
                            shiftedSellerSupply.get(i).getY());
                    break;
                }
            }
        }
        // System.out.println("Modern Intersect Point: (" + modernIntersectPoint.getX()
        // + ", " + modernIntersectPoint.getY() + ")");

        return modernIntersectPoint;
    }

    private double getQFunction() {
        double qMin = 0;
        // Wrap Up as Queues
        Queue<Point> Asks = new LinkedList<>();
        Queue<Point> Bids = new LinkedList<>();

        Collections.reverse(modernSellerSupply);
        Collections.reverse(modernBuyerDemand);
        // Turn List to Queue
        for (int i = 1; i < modernSellerSupply.size() - 2; i += 2) {
            Asks.add(new Point(DoubleMath.sub(modernSellerSupply.get(i).getX(), modernSellerSupply.get(i + 1).getX()),
                    modernSellerSupply.get(i).getY()));
        }
        for (int i = 1; i < modernBuyerDemand.size() - 2; i += 2) { // not include last extend line
            Bids.add(new Point(DoubleMath.sub(modernBuyerDemand.get(i).getX(), modernBuyerDemand.get(i + 1).getX()),
                    modernBuyerDemand.get(i).getY()));
        }
        Point a = Asks.poll();
        if (a != null) {
            Point b = Bids.poll();

            while (b != null && b.getY() < a.getY()) {
                b = Bids.poll();
            }

            // qd: Will Be The Demand at Price 0
            double qd = 0;
            // q: Current Horizontal Distance Between The Flipped Supply and The Demand
            // Curves (Minus The Final Value of qd, Which is Unknow at Present).
            double q = 0;
            while (b != null) {
                if (a != null && a.getY() <= b.getY()) {
                    q = DoubleMath.add(q, a.getX());
                    a = Asks.poll();
                } else {
                    q = DoubleMath.sub(q, b.getX());
                    qMin = Math.min(qMin, q);
                    qd = DoubleMath.add(qd, b.getX());
                    b = Bids.poll();
                }
            }

            qMin = DoubleMath.add(qMin, qd);
        }
        return qMin;
    }

    /*************************
     * Get Function
     *************************/

    public Point getTraditionalIntersectPoint() {
        return traditionalIntersectPoint;
    }

    public Point getModernIntersectPoint() {
        return modernIntersectPoint;
    }

    public List<Point> getShiftedSellerSupply() {
        return shiftedSellerSupply;
    }
}
