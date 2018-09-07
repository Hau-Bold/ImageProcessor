package statistics;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

/** the class ShowStatisticsDialog */
public class ShowStatisticsDialog extends JFrame {

	private static final long serialVersionUID = 1L;

	public ShowStatisticsDialog(int[] frequencies) {

		JPanel chartPanel = createChartPanel(frequencies);
		add(chartPanel, BorderLayout.CENTER);

		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private JPanel createChartPanel(int[] frequencies) {
		String chartTitle = "Statistics";
		String xAxisLabel = "RGB";
		String yAxisLabel = "Frequency";

		XYDataset dataset = createDataset(frequencies);

		boolean showLegend = false;
		boolean createURL = true;
		boolean createTooltip = false;

		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset,
				PlotOrientation.VERTICAL, showLegend, createTooltip, createURL);
		// XYSplineRenderer splineRenderer = new XYSplineRenderer(10);

		// chart.getXYPlot().setRenderer(splineRenderer);
		chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.BLACK);
		chart.getXYPlot().getRenderer().setSeriesShape(0, ShapeUtilities.createRegularCross((float) 0.0, (float) 0.0));

		return new ChartPanel(chart);
	}

	private XYDataset createDataset(int[] frequencies) {

		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("1");

		for (int i = 0; i < frequencies.length; i++) {

			int currentfrequency = frequencies[i];

			series1.add(i, currentfrequency);

		}

		dataset.addSeries(series1);

		return dataset;
	}

}
