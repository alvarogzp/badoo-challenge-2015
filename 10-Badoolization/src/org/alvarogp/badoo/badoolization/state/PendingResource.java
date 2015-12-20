package org.alvarogp.badoo.badoolization.state;

import org.alvarogp.badoo.badoolization.resources.quantity.ResourceQuantity;

public class PendingResource {
    private final ResourceQuantity resourceAndQuantity;
    private int buildingPoints;
    private int turns;

    public PendingResource(ResourceQuantity resourceAndQuantity, int buildingPoints, int turns) {
        this.resourceAndQuantity = resourceAndQuantity;
        this.buildingPoints = buildingPoints;
        this.turns = turns;
    }

    public PendingResource(PendingResource other) {
        resourceAndQuantity = other.resourceAndQuantity;
        buildingPoints = other.buildingPoints;
        turns = other.turns;
    }

    public ResourceQuantity getResourceAndQuantity() {
        return resourceAndQuantity;
    }

    public int getBuildingPoints() {
        return buildingPoints;
    }

    public int decreaseBuildingPoints(int points) {
        buildingPoints -= points;
        if (buildingPoints < 0) {
            int remainingBuildingPoints = -buildingPoints;
            buildingPoints = 0;
            return remainingBuildingPoints;
        }
        return 0;
    }

    public void decreaseTurn() {
        if (turns > 0) {
            turns--;
        }
    }

    public boolean canBeAdded() {
        return buildingPoints == 0 && turns == 0;
    }
}
