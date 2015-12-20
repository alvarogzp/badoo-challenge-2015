package org.alvarogp.badoo.badoolization.resources.price;

import org.alvarogp.badoo.badoolization.resources.Resource;
import org.alvarogp.badoo.badoolization.resources.quantity.FixedResourceQuantity;
import org.alvarogp.badoo.badoolization.resources.quantity.ResourceQuantity;

import java.util.ArrayList;
import java.util.List;

public class PriceBuilder {
    protected List<ResourceQuantity> resources = new ArrayList<>();

    public PriceBuilder add(int quantity, Resource resource) {
        resources.add(new FixedResourceQuantity(resource, quantity));
        return this;
    }

    public Price get() {
        return new Price(resources);
    }
}
