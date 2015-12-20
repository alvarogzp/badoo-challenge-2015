package org.alvarogp.badoo.badoolization.resources.group;

import org.alvarogp.badoo.badoolization.resources.ResourceState;

import java.util.Collections;
import java.util.List;

public class InertResourceStateGroup implements ResourceStateGroup {
    private int quantity;

    public InertResourceStateGroup() {
        quantity = 0;
    }

    public InertResourceStateGroup(InertResourceStateGroup other) {
        quantity = other.quantity;
    }

    @Override
    public void add() {
        quantity += 1;
    }

    @Override
    public int size() {
        return quantity;
    }

    @Override
    public void remove(int i) {
        quantity -= 1;
    }

    @Override
    public boolean isEmpty() {
        return quantity == 0;
    }

    @Override
    public List<ResourceState> getAll() {
        return Collections.emptyList();
    }

    @Override
    public ResourceStateGroup copy() {
        return new InertResourceStateGroup(this);
    }
}
