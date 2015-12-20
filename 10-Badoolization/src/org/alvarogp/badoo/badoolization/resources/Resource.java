package org.alvarogp.badoo.badoolization.resources;

import org.alvarogp.badoo.badoolization.resources.price.Price;
import org.alvarogp.badoo.badoolization.resources.price.cost.Cost;

public enum Resource {
    // GOODS

    FOOD(
            Cost.ZERO,
            Price.ZERO
    ),
    WOOD(
            Cost.ZERO,
            Price.ZERO
    ),
    STONE(
            Cost.ZERO,
            Price.ZERO
    ),
    GOLD(
            Cost.ZERO,
            Price.ZERO
    ),
    BUILDING_POINTS(
            Cost.ZERO,
            Price.ZERO
    ),

    // HUMANS

    PEASANT(
            Cost.builder().add(50, FOOD).addTurns(3).get(),
            Price.builder().add(2, BUILDING_POINTS).add(10, FOOD).add(10, WOOD).add(10, STONE).add(10, GOLD).get()
    ),

    SOLDIER(
            Cost.builder().add(30, FOOD).add(20, WOOD).addTurns(4).get(),
            Price.ZERO
    ),

    ARCHER(
            Cost.builder().add(30, WOOD).add(25, GOLD).addTurns(3).get(),
            Price.ZERO
    ),

    // STRUCTURES

    TOWN_HALL(
            Cost.builder().add(500, FOOD).add(500, STONE).addBuildingPoints(20).get(),
            Price.builder().add(1, PEASANT).get()
    ),

    BARRACKS(
            Cost.builder().add(100, WOOD).add(50, STONE).addBuildingPoints(10).get(),
            Price.builder().add(1, SOLDIER).get()
    ),

    ARCHERY_RANGE(
            Cost.builder().add(200, WOOD).add(30, GOLD).addBuildingPoints(12).get(),
            Price.builder().add(1, ARCHER).get()
    ),

    TOWN_HALL_BUILDER(
            Cost.ZERO,
            Price.builder().add(1, TOWN_HALL).get()
    ),

    BARRACKS_BUILDER(
            Cost.ZERO,
            Price.builder().add(1, BARRACKS).get()
    ),

    ARCHERY_RANGE_BUILDER(
            Cost.ZERO,
            Price.builder().add(1, ARCHERY_RANGE).get()
    ),

    ;

    public final Cost cost;
    public final Price production;

    Resource(Cost cost, Price production) {
        this.cost = cost;
        this.production = production;
    }
}
