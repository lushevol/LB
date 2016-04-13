package org.kylin.klb.sysInfo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class DynamicDataDemo extends ApplicationFrame implements ActionListener {
	private TimeSeries series;
	private double lastValue = 100.0D;

	public DynamicDataDemo(String title) {
		super(title);
		this.series = new TimeSeries("Random Data", Millisecond.class);
		TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
		JFreeChart chart = createChart(dataset);

		ChartPanel chartPanel = new ChartPanel(chart);
		JButton button = new JButton("Add New Data Item");
		button.setActionCommand("ADD_DATA");
		button.addActionListener(this);

		JPanel content = new JPanel(new BorderLayout());
		content.add(chartPanel);
		content.add(button, "South");
		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(content);
	}

	private JFreeChart createChart(XYDataset dataset) {
		JFreeChart result = ChartFactory.createTimeSeriesChart(
				"Dynamic Data Demo", "Time", "Value", dataset, true, true,
				false);
		XYPlot plot = result.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(60000.0D);
		axis = plot.getRangeAxis();
		axis.setRange(0.0D, 200.0D);
		return result;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ADD_DATA")) {
			double factor = 0.9D + 0.2D * Math.random();
			this.lastValue *= factor;
			this.series.add(new Millisecond(), this.lastValue);
		}
	}

	public static void main(String[] args) {
		DynamicDataDemo demo = new DynamicDataDemo("Dynamic Data Demo");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}
}
