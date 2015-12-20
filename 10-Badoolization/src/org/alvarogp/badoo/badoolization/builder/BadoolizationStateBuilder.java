package org.alvarogp.badoo.badoolization.builder;

import org.alvarogp.badoo.badoolization.state.BadoolizationState;
import org.alvarogp.badoo.badoolization.resources.Resource;
import org.alvarogp.badoo.badoolization.resources.quantity.ResourceQuantity;
import org.alvarogp.badoo.badoolization.resources.quantity.VariableResourceQuantity;

import java.util.ArrayList;
import java.util.List;

public class BadoolizationStateBuilder {
    private List<ResourceQuantity> resources;

    public BadoolizationStateBuilder(int numberOfResources) {
        this.resources = new ArrayList<>(numberOfResources);
    }

    public void addResource(String resourceName, int quantity) {
        Resource resource = ResourceConverter.fromString(resourceName);
        resources.add(new VariableResourceQuantity(resource, quantity));
    }

    public BadoolizationState createState() {
        return new BadoolizationState(resources);
    }
}
