package org.alvarogp.badoo.badoolization.resources.quantity;

import org.alvarogp.badoo.badoolization.resources.Resource;

public abstract class ResourceQuantity {
    private final Resource resource;

    protected ResourceQuantity(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    public abstract int getQuantity();
}
