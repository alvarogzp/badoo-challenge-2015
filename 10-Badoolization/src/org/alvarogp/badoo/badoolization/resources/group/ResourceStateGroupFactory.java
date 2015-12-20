package org.alvarogp.badoo.badoolization.resources.group;

import org.alvarogp.badoo.badoolization.resources.Resource;

public class ResourceStateGroupFactory {
    public static ResourceStateGroup createFor(Resource resource) {
        switch (resource) {
            case FOOD:
            case WOOD:
            case STONE:
            case GOLD:
                return new InertResourceStateGroup();
            default:
                return new IndependentResourceStateGroup(resource);
        }
    }
}
