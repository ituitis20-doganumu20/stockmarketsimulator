import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.util.Random;

public class RandomNumberVisualization {
    public static void main(String[] args) {
        double[] values = new double[1000];
        Random random = new Random();

        for (int i = 0; i < values.length; i++) {
            double u1 = random.nextDouble(); // Uniform random value in [0, 1)
            double u2 = random.nextDouble(); // Uniform random value in [0, 1)
            double randStdNormal = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2 * Math.PI * u2);
            values[i] = Math.max(0,1 + 0.1 * randStdNormal) ; // Adjust mean and standard deviation as needed
        }

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Random Numbers", values, 10); // Adjust bin count as needed

        JFreeChart chart = ChartFactory.createHistogram(
                "Random Number Distribution",
                "Value",
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ApplicationFrame frame = new ApplicationFrame("Random Number Distribution");
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }
}
