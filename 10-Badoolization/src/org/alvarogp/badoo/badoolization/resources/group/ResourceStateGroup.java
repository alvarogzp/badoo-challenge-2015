package org.alvarogp.badoo.badoolization.resources.group;

import org.alvarogp.badoo.badoolization.resources.ResourceState;

import java.util.List;

public interface ResourceStateGroup {
    ResourceStateGroup EMPTY = new InertResourceStateGroup();

    void add();
    int size();
    void remove(int i);
    boolean isEmpty();
    List<ResourceState> getAll();
    ResourceStateGroup copy();
}
