package org.alvarogp.badoo.badoolization.state;

import org.alvarogp.badoo.badoolization.resources.quantity.ResourceQuantity;

import java.util.List;

public class BadoolizationState {
    private final List<ResourceQuantity> resources;

    public BadoolizationState(List<ResourceQuantity> resources) {
        this.resources = resources;
    }

    public List<ResourceQuantity> getResources() {
        return resources;
    }
}
