package net.bleyel.test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawPrimitives extends JPanel {

    private BufferedImage canvas;
    private int width;
    private int height;

    public DrawPrimitives(int width, int height, Color background) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fillCanvas(background);
        setWidth(width);
        setHeight(height);
    }

    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }


    public void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }


    public void drawLine(int xStart, int yStart, int xEnd, int yEnd, Color c, boolean algorithm) {
        // Implement line drawing
        //DDA
        //algo true = DDA
        //algo false = Bresenham

        ArrayList<Object> data = new ArrayList<Object>();

        int sumRows;
        int cols;
        int xPixel;
        int yPixel;

        int dx = xEnd - xStart;
        int dy = yEnd - yStart;

        if (Math.abs(dx) > Math.abs(dy))
            sumRows = Math.abs(dx);
        else
            sumRows = Math.abs(dy);

        if (xStart >= 0 && xStart <= width && yStart >= 0 && yStart <= height) {
            canvas.setRGB(xStart, height - yStart, Color.green.getRGB());
        }

        if (algorithm) {
            //DDA

            data.add(0);
            data.add(xStart);
            data.add(yStart);
            data.add(xStart);
            data.add(yStart);

            cols = 5;
            float xk1 = xStart;
            float yk1 = yStart;

            float xIncrement = dx / (float) sumRows;
            float yIncrement = dy / (float) sumRows;

            for (int k = 1; k < sumRows; k++) {
                xk1 += xIncrement;
                yk1 += yIncrement;

                xPixel = Math.round(xk1);
                yPixel = Math.round(yk1);

                System.out.println("xpix: " + xPixel + " ypix: " + yPixel);
                if (xPixel >= 0 && xPixel <= width && yPixel >= 0 && yPixel <= height) {
                    canvas.setRGB(xPixel, height - yPixel, c.getRGB());
                }

                data.add(k);
                data.add(xk1);
                data.add(yk1);
                data.add(xPixel);
                data.add(yPixel);
            }

            data.add(sumRows);
            data.add(xEnd);
            data.add(yEnd);
            data.add(xEnd);
            data.add(yEnd);

        } else {
            //Bresenham

            cols = 4;
            int dx2 = 2 * Math.abs(dx);
            int dy2 = 2 * Math.abs(dy);

            int ix = xStart < xEnd ? 1 : -1; // increment direction
            int iy = yStart < yEnd ? 1 : -1;

            int p = dy2 - Math.abs(dx);

            data.add("-");
            data.add("-");
            data.add(xStart);
            data.add(yStart);

            xPixel = xStart;
            yPixel = yStart;
            for (int i = 0; i < Math.abs(dx); i++) {
                if (p < 0) {
                    xPixel += ix;
                    p = p + dy2;
                } else {
                    xPixel += ix;
                    yPixel += iy;
                    p = p + dy2 - dx2;
                }

                data.add(i);
                data.add(p);
                data.add(xPixel);
                data.add(yPixel);
                if (xPixel >= 0 && xPixel <= width && yPixel >= 0 && yPixel <= height) {
                    canvas.setRGB(xPixel, height - yPixel, c.getRGB());
                }
            }
        }

        if (xEnd >= 0 && xEnd <= width && yEnd >= 0 && yEnd <= height) {
            canvas.setRGB(xEnd, height - yEnd, Color.red.getRGB());
        }
        new SimpleTableClass(sumRows + 1, cols, data, algorithm, 'l');
        repaint();
    }

    public void drawCircle(int xCenter, int yCenter, int radius, boolean algorithm, Color c) {

        ArrayList<Object> data = new ArrayList<Object>();
        int sumRows = 1;
        int cols = 6;
        double xk;
        double yk;
        double theta = 1 / (float) radius;
        int xPixel;
        int yPixel;
        int j = 0;


        if (algorithm) {
            //Ecu parametricas

            data.add(0);
            data.add(0);
            data.add(xCenter + radius);
            data.add(yCenter);
            data.add(xCenter + radius);
            data.add(yCenter);

            while (theta < 2 * Math.PI) {
                xk = xCenter + radius * Math.cos(theta);
                yk = yCenter + radius * Math.sin(theta);
                xPixel = Math.round((float) xk);
                yPixel = Math.round((float) yk);

                data.add(++j);
                data.add(theta);
                data.add(xk);
                data.add(yk);
                data.add(xPixel);
                data.add(yPixel);

                if (xPixel >= 0 && xPixel <= width && yPixel >= 0 && yPixel <= height) {
                    canvas.setRGB(xPixel, height - yPixel, c.getRGB());
                }
                theta += 1 / (float) radius;
                sumRows++;
            }


        } else {
            //punto medio

            ArrayList<Integer> coordsPosPos = new ArrayList<>();
            ArrayList<Integer> allCoordsCenterZero;
            ArrayList<Integer> allCoordsCenterCorrect = new ArrayList<>();

            int pk = 1 - radius;
            int xk12;
            int yk12;
            sumRows = 1;

            xPixel = 0;
            yPixel = radius;

            data.add("-");
            data.add("-");
            data.add(xPixel);
            data.add(yPixel);
            data.add(xPixel);
            data.add(yPixel);

            coordsPosPos.add(xPixel);
            coordsPosPos.add(yPixel);

            j = 0;
            data.add(++j);
            data.add(pk);

            while (xPixel < yPixel) {
                xPixel++;
                xk12 = 2 * xPixel;
                yk12 = 2 * yPixel;
                if (pk < 0) {
                    pk = pk + xk12 + 1;
                } else {
                    yPixel--;
                    pk = pk + xk12 + 1 - yk12;
                }

                coordsPosPos.add(xPixel);
                coordsPosPos.add(yPixel);

                data.add(xPixel);
                data.add(yPixel);
                data.add(xk12);
                data.add(yk12);
                data.add(++j);
                data.add(pk);
                sumRows++;
            }
            data.remove(data.size() - 1);
            data.remove(data.size() - 2);

            allCoordsCenterZero = calcAllCords(coordsPosPos, radius);

            int n = 0;
            xPixel = 0;
            for (Integer coord : allCoordsCenterZero) {
                if (n % 2 == 0) {
                    xPixel = coord + xCenter;
                } else {
                    yPixel = coord + yCenter;
                    if (xPixel >= 0 && xPixel <= width && yPixel >= 0 && yPixel <= height) {
                        canvas.setRGB(xPixel, height - yPixel, c.getRGB());
                    }
                    allCoordsCenterCorrect.add(xPixel);
                    allCoordsCenterCorrect.add(yPixel);
                }
                n++;
            }

            data.addAll(allCoordsCenterZero);
            data.addAll(allCoordsCenterCorrect);
        }

        new SimpleTableClass(sumRows, cols, data, algorithm, 'c');
        repaint();
    }

    private ArrayList<Integer> calcAllCords(ArrayList<Integer> pospos, int radius) {
        ArrayList<Integer> coordsPosNeg = new ArrayList<>();
        ArrayList<Integer> coordsNegPos = new ArrayList<>();
        ArrayList<Integer> coordsNegNeg = new ArrayList<>();
        ArrayList<Integer> halfCoords = new ArrayList<>();
        ArrayList<Integer> otherHalfCoords = new ArrayList<>();
        ArrayList<Integer> allCoords = new ArrayList<>();

        int i = 0;

        for (Integer coord : pospos) {

            if (i % 2 == 0)  // gerade
                coordsPosNeg.add(coord);
            else // ungerade
                coordsPosNeg.add(coord * -1);
            i++;

        }
        i = 0;
        for (Integer coord : pospos) {
            if (i % 2 == 0)  // gerade
                coordsNegPos.add(coord * -1);
            else // ungerade
                coordsNegPos.add(coord);
            i++;
        }

        for (Integer coord : pospos) {
            coordsNegNeg.add(coord * -1);
        }

        halfCoords.addAll(pospos);
        halfCoords.addAll(coordsPosNeg);
        halfCoords.addAll(coordsNegPos);
        halfCoords.addAll(coordsNegNeg);

        //vertauschen
        i = 0;
        int temp = 0;
        for (Integer coord : halfCoords) {
            if (i % 2 == 0) {
                temp = coord;
            } else {
                otherHalfCoords.add(coord);
                otherHalfCoords.add(temp);
            }
            i++;
        }

        allCoords.addAll(halfCoords);
        allCoords.addAll(otherHalfCoords);
        return allCoords;
    }

    public void drawRect(Color c, int x1, int y1, int width, int height) {
        int color = c.getRGB();
        // Implement rectangle drawing
        for (int x = x1; x < x1 + width; x++) {
            for (int y = y1; y < y1 + height; y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }

    public void drawOval(Color c, int x1, int y1, int width, int height) {
        // Implement oval drawing
        repaint();
    }


    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
