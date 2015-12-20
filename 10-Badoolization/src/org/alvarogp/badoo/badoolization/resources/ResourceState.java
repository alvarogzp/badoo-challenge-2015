package org.alvarogp.badoo.badoolization.resources;

import org.alvarogp.badoo.badoolization.resources.quantity.FixedResourceQuantity;
import org.alvarogp.badoo.badoolization.resources.quantity.ResourceQuantity;
import org.alvarogp.badoo.badoolization.state.PendingResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceState {
    private final Resource resource;
    private PendingResource producingResource;

    public ResourceState(Resource resource) {
        this.resource = resource;
    }

    public ResourceState(ResourceState other) {
        resource = other.resource;
        producingResource = other.producingResource;
    }

    public List<ResourceQuantity> produce() {
        if (producingResource != null) {
            if (producingResource.canBeAdded()) {
                producingResource = null;
            } else {
                // we are producing a multiple-turn resource, cannot produce anything else
                return Collections.emptyList();
            }
        }

        List<ResourceQuantity> availableResourcesToProduce = new ArrayList<>(resource.production.getResources());
        availableResourcesToProduce.add(new FixedResourceQuantity(Resource.BUILDING_POINTS, 0)); // do not produce anything, its a possibility
        return availableResourcesToProduce;
    }

    public void setProducingResource(PendingResource producingResource) {
        this.producingResource = producingResource;
    }
}
