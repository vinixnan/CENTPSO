/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphtest;

/**
 *
 * @author Vinicius
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.HashMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.ShapeUtilities;

public class Chart extends ApplicationFrame {

    private HashMap<String, XYSeries> hash;
    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private HashMap<Integer, Paint> colors;

    public Chart(String title, String titlegraph) {
        super(title);
        this.hash = new HashMap<>();
        this.dataset = new XYSeriesCollection();
        this.defineColors();
        JPanel chartPanel = createDemoPanel(titlegraph);

        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private void defineColors() {
        this.colors=new HashMap<>();
        this.colors.put(1, Color.BLUE);
        this.colors.put(2, Color.GREEN);
        this.colors.put(3, Color.YELLOW);
        this.colors.put(4, Color.ORANGE);
    }

    public void addRoots(int qtdroots, double x_pos, double y_pos) {
        String depname="Dep.";
        for (int i = 1; i <= qtdroots; i++) {
            this.addValue(this.getIdRoute(i), x_pos, y_pos,depname);
            depname="";
        }
    }

    public void clear()
    {
        this.hash.clear();
        this.dataset.removeAllSeries();
        XYPlot  p=(XYPlot) this.chart.getPlot();
        p.clearAnnotations();
    }
    
    private String getIdRoute(int idroute) {
        return "Root" + String.valueOf(idroute);
    }

    private JFreeChart createChart(String titulografico) {

        chart = ChartFactory.createXYLineChart(
                titulografico,
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        renderer.setBaseStroke(new BasicStroke(80));
        renderer.setBaseItemLabelsVisible(true);
        renderer.setShapesVisible(true);
        renderer.setShape(ShapeUtilities.createDiamond(9));
        plot.setRenderer(renderer);
        return chart;
    }

    public JPanel createDemoPanel(String descgrafico) {
        chart = createChart(descgrafico);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    public void addValue(int iddot, double x_value, double y_value) {
        this.addValue(String.valueOf(iddot), x_value, y_value, String.valueOf(iddot));
    }

    public void addValue(String iddot, double x_value, double y_value, String label) {

        XYSeries s = new XYSeries(iddot);
        s.add(x_value, y_value);
        dataset.addSeries(s);
        this.hash.put(iddot, s);
        XYTextAnnotation annotation;
        annotation = new XYTextAnnotation(label, x_value, y_value);
        annotation.setFont(new Font("Arial", Font.BOLD, 10));
        annotation.setPaint(Color.BLACK);
        XYPlot  p=(XYPlot) this.chart.getPlot();
        p.addAnnotation(annotation);
    }

    public void addEdge(int idroute, int iddot1, int iddot2) {
        this.addEdge(idroute, String.valueOf(iddot1), String.valueOf(iddot2));
    }

    public void addEdge(int idroute, String iddot1, String iddot2) {
        XYSeries dot1 = this.hash.get(iddot1);
        XYSeries dot2 = this.hash.get(iddot2);
        if (dot1 != null && dot2 != null) {
            dot1.add(dot2.getX(0), dot2.getY(0));
            this.setColor(idroute, this.dataset.indexOf(dot1));
            this.setColor(idroute, this.dataset.indexOf(dot2));
        }
    }

    private void setColor(int idroute, int posdot) {
        XYItemRenderer renderer = chart.getXYPlot().getRenderer();
        Paint cor = colors.get(idroute);
        if (cor == null) {
            cor = renderer.getSeriesPaint(posdot);
            colors.put(idroute, cor);
        } else {
            renderer.setSeriesPaint(posdot, cor);
        }
        renderer.setSeriesShape(posdot, renderer.getSeriesShape(0));
    }
}
