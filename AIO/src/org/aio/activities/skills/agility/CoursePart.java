package org.aio.activities.skills.agility;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;

public class CoursePart {

    final Obstacle obstacle;
    public final Area activeArea;
    Position[] pathToArea;
    Position obstaclePosition;

    CoursePart(final Obstacle obstacle, final Area activeArea){
        this.obstacle = obstacle;
        this.activeArea = activeArea;
    }

    CoursePart(final Obstacle obstacle, final Area activeArea, final Position[] pathToObstacle){
        this.obstacle = obstacle;
        this.activeArea = activeArea;
        this.pathToArea = pathToObstacle;
    }

    CoursePart(final Obstacle obstacle, final Area activeArea, final Position obstaclePosition) {
        this.obstacle = obstacle;
        this.activeArea = activeArea;
        this.obstaclePosition = obstaclePosition;
    }

    CoursePart(final Obstacle obstacle, final Area activeArea, final Position[] pathToObstacle, final Position obstaclePosition){
        this.obstacle = obstacle;
        this.activeArea = activeArea;
        this.pathToArea = pathToObstacle;
        this.obstaclePosition = obstaclePosition;
    }
}
