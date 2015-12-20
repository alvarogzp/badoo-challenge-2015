package org.alvarogp.badoo.badoolization.resources.quantity;

import org.alvarogp.badoo.badoolization.resources.Resource;

public class FixedResourceQuantity extends ResourceQuantity {
    private final int quantity;

    public FixedResourceQuantity(Resource resource, int quantity) {
        super(resource);
        this.quantity = quantity;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }
}
