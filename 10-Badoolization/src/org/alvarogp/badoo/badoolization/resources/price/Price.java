package org.alvarogp.badoo.badoolization.resources.price;

import org.alvarogp.badoo.badoolization.resources.quantity.ResourceQuantity;

import java.util.Collections;
import java.util.List;

public class Price {
    public static final Price ZERO = new Price(Collections.emptyList());

    private final List<ResourceQuantity> resources;

    public Price(List<ResourceQuantity> resources) {
        this.resources = Collections.unmodifiableList(resources);
    }

    public List<ResourceQuantity> getResources() {
        return resources;
    }

    public static PriceBuilder builder() {
        return new PriceBuilder();
    }
}
