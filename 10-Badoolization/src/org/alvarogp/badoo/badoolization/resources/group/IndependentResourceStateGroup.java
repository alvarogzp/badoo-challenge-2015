package org.alvarogp.badoo.badoolization.resources.group;

import org.alvarogp.badoo.badoolization.resources.Resource;
import org.alvarogp.badoo.badoolization.resources.ResourceState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IndependentResourceStateGroup implements ResourceStateGroup {
    private final Resource resource;
    private List<ResourceState> resourceStates;

    public IndependentResourceStateGroup(Resource resource) {
        this.resource = resource;
        resourceStates = new ArrayList<>();
    }

    public IndependentResourceStateGroup(IndependentResourceStateGroup other) {
        resource = other.resource;
        resourceStates = new ArrayList<>(other.resourceStates.stream().map(ResourceState::new).collect(Collectors.toList()));
    }

    public void add() {
        resourceStates.add(new ResourceState(resource));
    }

    public int size() {
        return resourceStates.size();
    }

    public void remove(int i) {
        resourceStates.remove(i);
    }

    public boolean isEmpty() {
        return resourceStates.isEmpty();
    }

    public List<ResourceState> getAll() {
        return resourceStates;
    }

    public ResourceStateGroup copy() {
        return new IndependentResourceStateGroup(this);
    }
}
