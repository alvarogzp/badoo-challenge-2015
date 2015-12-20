package org.alvarogp.badoo.badoolization.resources.price.cost;

import org.alvarogp.badoo.badoolization.resources.price.Price;
import org.alvarogp.badoo.badoolization.resources.quantity.ResourceQuantity;

import java.util.Collections;
import java.util.List;

public class Cost extends Price {
    public static final Cost ZERO = new Cost(Collections.emptyList(), 1, 0);

    private int turns;
    private int buildingPoints;

    public Cost(List<ResourceQuantity> resources, int turns, int buildingPoints) {
        super(resources);
        this.turns = turns;
        this.buildingPoints = buildingPoints;
    }

    public int getTurns() {
        return turns;
    }

    public int getBuildingPoints() {
        return buildingPoints;
    }

    public static CostBuilder builder() {
        return new CostBuilder();
    }
}
