package org.aio.paint;

import org.osbot.rs07.api.Mouse;
import org.osbot.rs07.canvas.paint.Painter;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayDeque;
import java.util.Iterator;

public class MouseTrail implements Painter {

    private final ArrayDeque<Point> mousePoints;
    private final Mouse mouse;
    private final int maxTrailLength;
    private final Color colour;

    public MouseTrail(final Mouse mouse, final int maxTrailLength, final Color colour) {
        this.mouse = mouse;
        this.maxTrailLength = maxTrailLength;
        this.colour = colour;
        mousePoints = new ArrayDeque<>(maxTrailLength);
    }

    @Override
    public void onPaint(final Graphics2D g) {
        updatePoints();
        if (mousePoints.size() > 1) drawPath(g);
    }

    private void updatePoints() {
        Point mousePos = mouse.getPosition();
        if (mousePos.equals(mousePoints.peekLast())){
            mousePoints.poll();
            return;
        }
        if (mousePoints.size() >= maxTrailLength) mousePoints.poll();
        if (mousePos.x < 0 || mousePos.y < 0){
            if(mousePoints.size() > 0) mousePoints.offer(mousePoints.peekLast());
        }
        else mousePoints.offer(mousePos);
    }

    private void drawPath(final Graphics2D g) {

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(colour);

        GeneralPath path = new GeneralPath();

        Iterator<Point> mousePointIterator = mousePoints.iterator();

        Point firstPoint = mousePointIterator.next();
        path.moveTo(firstPoint.getX(), firstPoint.getY());

        Point prev = firstPoint;
        while (mousePointIterator.hasNext()) {
            Point current = mousePointIterator.next();
            path.quadTo(prev.getX(), prev.getY(), current.getX(), current.getY());
            prev = current;
        }
        g.draw(path);
    }
}
