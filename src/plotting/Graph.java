package src.plotting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;


public class Graph {
    public static final String VERSION = "Function Graph of f(x)";

    // A graph may plot as many functions as it wants. These may all be of different types
    public Vector<Plotter> functions;

    // The area and general settings of the graph are all defined by a PlotArea object
    public PlotSettings plotSettings;

    // Initialises the graph with the plot settings to use
    public Graph(PlotSettings p) {
        this.functions = new Vector<Plotter>(5);
        this.plotSettings = p;
    }

    protected double plotRangeX, plotRangeY;

    // How many pixels are there available to use in the graph? This is the size of the image minus the border size
    protected int chartWidth, chartHeight;

    protected double unitsPerPixelX, unitsPerPixelY;

    /**
     * Draws the graph using a graphics object.
     * Note, X axis labels come from the first function (this only applies to discrete functions)
     *
     * @param g      The graphics context on which to draw
     * @param width  The width to make the graph
     * @param height The height to make the graph
     */
    public void draw(Graphics g, int width, int height) {

        // Draw the title
        if (plotSettings.title != null) {
            g.setColor(plotSettings.fontColor);

            // ensure the border top can accommodate the title
            if (plotSettings.marginTop < g.getFontMetrics().getHeight() + 20) {
                plotSettings.marginTop = g.getFontMetrics().getHeight() + 20;
            }

            int titleXPosition = (width / 2) - ((g.getFontMetrics().stringWidth(plotSettings.title)) / 2);
            g.drawString(plotSettings.title, titleXPosition, 10 + g.getFontMetrics().getHeight());
        }

        // Calculate the plot range
        plotRangeX = Math.abs(plotSettings.maxX - plotSettings.minX);
        plotRangeY = Math.abs(plotSettings.maxY - plotSettings.minY);

        /*
         * First we need to know how many pixels there are across the panel
         * And we can divide that number between the range that we've been assigned.
        */
        chartWidth = width - (plotSettings.marginLeft + plotSettings.marginRight);
        chartHeight = height - (plotSettings.marginTop + plotSettings.marginBottom);

        // Calculate the number of units per pixel
        unitsPerPixelX = plotRangeX / chartWidth;
        unitsPerPixelY = plotRangeY / chartHeight;

        // Set the background colour
        g.setColor(plotSettings.backgroundColor);
        g.fillRect(plotSettings.marginLeft, plotSettings.marginTop, chartWidth - 1, chartHeight - 1);

        // Draw a box around the whole graph to delimit the Axes
        g.setColor(plotSettings.axisColor);
        g.drawRect(plotSettings.marginLeft, plotSettings.marginTop, chartWidth, chartHeight);

        // Draw the horizontal and vertical axes that go through the point at 0,0
        int yEqualsZero = getPlotY(0) + 0;
        if (0 > plotSettings.getMinY() && 0 < plotSettings.getMaxY())
            g.drawLine(plotSettings.marginLeft, yEqualsZero, plotSettings.marginLeft + chartWidth - 1, yEqualsZero);

        int xEqualsZero = getPlotX(0) + 0;
        if (0 > plotSettings.getMinX() && 0 < plotSettings.getMaxX())
            g.drawLine(xEqualsZero, plotSettings.marginTop, xEqualsZero, plotSettings.marginTop + chartHeight);

        // And finally - draw the results of the function onto the chart
        for (int i = 0; i < functions.size(); i++) {
            Plotter function = functions.elementAt(i);
            g.setColor(plotSettings.getPlotColor());
            function.plot(this, g, chartWidth, chartHeight);
        }

    }


    // Uses the numeric value of Y (as returned by a function) and figures out which pixel on screen this relates to
    public int getPlotY(double y) {

        // Convert Y into pixel coordinates again
        int pixelY = ((int) ((y - plotSettings.minY) / unitsPerPixelY));

        // We also need to flip the Y axis because Y is counted from the top and not the bottom. Add the various borders
        return ((chartHeight - pixelY) + plotSettings.marginTop);
    }

    // Uses the numeric value of X, and figures out which pixel on the screen this relates to
    public int getPlotX(double x) {
        return (int) (((x - plotSettings.minX) / unitsPerPixelX) + plotSettings.marginLeft);
    }

    // Takes a numeric distance and calculates how many actual pixels high that is
    public double getActualHeight(double height) {
        return height / unitsPerPixelY;
    }

    // Takes a numeric distance and calculates how many actual pixels wide that is
    public double getActualWidth(double width) {
        return width / unitsPerPixelX;
    }

    // Takes a set number of actual pixels on the screen (in the Y direction) and returns how long they are, if plotted on the graph
    public double getPlotHeight(double height) {
        return height * unitsPerPixelY;
    }

    // Takes a set number of actual pixels on the screen (in the X direction) and returns how long they are, if plotted on the graph
    public double getPlotWidth(double width) {
        return width * unitsPerPixelX;
    }

    public double getActualX(int pixelX) {
        return plotSettings.minX + (pixelX * unitsPerPixelX);
    }


    /**
     * Plots a line between two sets of values
     *
     * @param g  Graphics context upon which to write
     * @param x1 First point X
     * @param y1 First point Y
     * @param x2 Second point X
     * @param y2 Second point Y
     */
    public void drawLine(Graphics g, double x1, double y1, double x2, double y2) {
        g.drawLine(getPlotX(x1), getPlotY(y1), getPlotX(x2), getPlotY(y2));
    }
    

   // Returns the graph as an image so that it can be saved
    public BufferedImage getImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(plotSettings.backgroundColor);
        g.fillRect(0, 0, width, height);
        draw(g, width, height);
        return image;
    }

}