package org.alvarogp.badoo.badoolization.resources.price.cost;

import org.alvarogp.badoo.badoolization.resources.Resource;
import org.alvarogp.badoo.badoolization.resources.price.PriceBuilder;

public class CostBuilder extends PriceBuilder {
    private int turns = 1;
    private int buildingPoints = 0;

    @Override
    public CostBuilder add(int quantity, Resource resource) {
        super.add(quantity, resource);
        return this;
    }

    public CostBuilder addTurns(int turns) {
        this.turns = turns;
        return this;
    }

    public CostBuilder addBuildingPoints(int buildingPoints) {
        this.buildingPoints = buildingPoints;
        return this;
    }

    @Override
    public Cost get() {
        return new Cost(resources, turns, buildingPoints);
    }
}
