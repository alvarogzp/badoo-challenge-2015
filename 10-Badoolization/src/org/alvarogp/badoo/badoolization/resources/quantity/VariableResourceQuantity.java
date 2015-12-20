package org.alvarogp.badoo.badoolization.resources.quantity;

import org.alvarogp.badoo.badoolization.resources.Resource;

public class VariableResourceQuantity extends ResourceQuantity {
    private int quantity;

    public VariableResourceQuantity(Resource resource, int quantity) {
        super(resource);
        this.quantity = quantity;
    }

    public void add(int quantity) {
        this.quantity += quantity;
    }

    public void remove(int quantity) {
        this.quantity -= quantity;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }
}
