package org.alvarogp.badoo.badoolization.state;

import org.alvarogp.badoo.badoolization.calculator.BadoolizationMinimumTurnsCalculator;
import org.alvarogp.badoo.badoolization.resources.Resource;
import org.alvarogp.badoo.badoolization.resources.ResourceState;
import org.alvarogp.badoo.badoolization.resources.group.ResourceStateGroup;
import org.alvarogp.badoo.badoolization.resources.group.ResourceStateGroupFactory;
import org.alvarogp.badoo.badoolization.resources.quantity.FixedResourceQuantity;
import org.alvarogp.badoo.badoolization.resources.quantity.ResourceQuantity;

import java.util.*;
import java.util.stream.Collectors;

public class BadoolizationCurrentState {
    private final Map<Resource, ResourceStateGroup> resources = new HashMap<>();
    private final Map<Resource, ResourceQuantity> target;
    private final List<PendingResource> pendingResources;
    private int turn;

    private BadoolizationMinimumTurnsCalculator badoolizationMinimumTurnsCalculator;

    public BadoolizationCurrentState(BadoolizationState initialState,
                                     BadoolizationState finalState,
                                     BadoolizationMinimumTurnsCalculator badoolizationMinimumTurnsCalculator) {
        for (ResourceQuantity resourceQuantity : initialState.getResources()) {
            Resource resource = resourceQuantity.getResource();
            int quantity = resourceQuantity.getQuantity();
            ResourceStateGroup resourceStates = ResourceStateGroupFactory.createFor(resource);
            resources.put(resource, resourceStates);
            for (int i = 0; i < quantity; i++) {
                resourceStates.add();
            }
        }
        target = new HashMap<>();
        for (ResourceQuantity resourceQuantity : finalState.getResources()) {
            Resource resource = resourceQuantity.getResource();
            target.put(resource, resourceQuantity);
        }
        pendingResources = new ArrayList<>();
        turn = 0;
        this.badoolizationMinimumTurnsCalculator = badoolizationMinimumTurnsCalculator;
    }

    public BadoolizationCurrentState(BadoolizationCurrentState previousState) {
        for (Map.Entry<Resource, ResourceStateGroup> entry : previousState.resources.entrySet()) {
            ResourceStateGroup resourceStates = entry.getValue().copy();
            resources.put(entry.getKey(), resourceStates);
        }
        target = previousState.target; // does not change
        pendingResources = new ArrayList<>(previousState.pendingResources.stream().map(PendingResource::new).collect(Collectors.toList()));
        turn = previousState.turn;
        badoolizationMinimumTurnsCalculator = previousState.badoolizationMinimumTurnsCalculator;
    }

    public int getMissingResource(Resource resource) {
        if (resource == Resource.BUILDING_POINTS) {
            int missingBuildingPoints = 0;
            for (PendingResource pendingResource : pendingResources) {
                missingBuildingPoints += pendingResource.getBuildingPoints();
            }
            return missingBuildingPoints;
        } else {
            return target.get(resource).getQuantity() - resources.getOrDefault(resource, ResourceStateGroup.EMPTY).size();
        }
    }

    public PendingResource produce(Resource resource, int quantity) {
        if (resource == Resource.BUILDING_POINTS) {
            consumeBuildingPoints(quantity);
            return null;
        } else {
            if (!resourceProductionCanBePaid(resource)) {
                // does not have enough resources to produce it
                return null;
            }
            payForProductionOfNew(resource);
            int buildingPoints = resource.cost.getBuildingPoints();
            int turns = resource.cost.getTurns();
            PendingResource pendingResource = new PendingResource(new FixedResourceQuantity(resource, quantity), buildingPoints, turns);
            pendingResources.add(pendingResource);
            return pendingResource;
        }
    }

    private void consumeBuildingPoints(int points) {
        if (points == 0) {
            return;
        }
        for (PendingResource pendingResource : pendingResources) {
            points = pendingResource.decreaseBuildingPoints(points);
            if (points == 0) {
                break;
            }
        }
    }

    private boolean resourceProductionCanBePaid(Resource resource) {
        for (ResourceQuantity resourceQuantity : resource.cost.getResources()) {
            Resource resourceToPayWith = resourceQuantity.getResource();
            int quantity = resourceQuantity.getQuantity();
            int resourcesAvailable = resources.get(resourceToPayWith).size();
            if (resourcesAvailable < quantity) {
                return false;
            }
        }
        return true;
    }

    private void payForProductionOfNew(Resource resource) {
        for (ResourceQuantity resourceQuantity : resource.cost.getResources()) {
            Resource resourceToPayWith = resourceQuantity.getResource();
            int quantity = resourceQuantity.getQuantity();
            ResourceStateGroup resourcesToRemoveFrom = resources.get(resourceToPayWith);
            for (int i = 0; i < quantity; i++) {
                resourcesToRemoveFrom.remove(resourcesToRemoveFrom.size()-1);
            }
        }
    }

    public void advanceTurn() {
        ArrayList<Map.Entry<Resource, ResourceStateGroup>> resourceEntries = new ArrayList<>(this.resources.entrySet());
        Collections.sort(resourceEntries, (o1, o2) -> {
            Resource r1 = o1.getKey();
            Resource r2 = o2.getKey();
            if (r1 == r2) {
                return 0;
            }
            if (r1 == Resource.TOWN_HALL || r1 == Resource.BARRACKS || r1 == Resource.ARCHERY_RANGE) {
                return -1;
            } else if (r2 == Resource.TOWN_HALL || r2 == Resource.BARRACKS || r2 == Resource.ARCHERY_RANGE) {
                return 1;
            }
            return r1.compareTo(r2);
        });

        List<ResourceState> totalResources = new ArrayList<>();

        totalResources.add(new ResourceState(Resource.TOWN_HALL_BUILDER));
        totalResources.add(new ResourceState(Resource.BARRACKS_BUILDER));
        if (!resources.getOrDefault(Resource.BARRACKS, ResourceStateGroup.EMPTY).isEmpty()) {
            totalResources.add(new ResourceState(Resource.ARCHERY_RANGE_BUILDER));
        }

        resourceEntries.stream().map(Map.Entry::getValue).map(ResourceStateGroup::getAll).forEach(totalResources::addAll);

        produceFor(totalResources, 0);
    }

    private void produceFor(List<ResourceState> totalResources, int index) {
        if (index >= totalResources.size()) {
            continueToNextTurn();
            return;
        }
        ResourceState resourceState = totalResources.get(index);
        for (ResourceQuantity productionOption : resourceState.produce()) {
            Resource resource = productionOption.getResource();
            int quantity = productionOption.getQuantity();
            BadoolizationCurrentState stateCopy = new BadoolizationCurrentState(this);
            PendingResource pendingResource = stateCopy.produce(resource, quantity);
            for (ResourceStateGroup resourceStateListFromCopy : stateCopy.resources.values()) {
                resourceStateListFromCopy.getAll().stream()
                        .filter(resourceStateFromCopy -> resourceStateFromCopy == resourceState)
                        .forEach(resourceStateFromCopy -> resourceStateFromCopy.setProducingResource(pendingResource));
            }
            stateCopy.produceFor(totalResources, index+1);
        }
    }

    private void continueToNextTurn() {
        Iterator<PendingResource> pendingResourceIterator = pendingResources.iterator();
        while (pendingResourceIterator.hasNext()) {
            PendingResource pendingResource = pendingResourceIterator.next();
            pendingResource.decreaseTurn();
            if (pendingResource.canBeAdded()) {
                ResourceQuantity resourceQuantity = pendingResource.getResourceAndQuantity();
                Resource resource = resourceQuantity.getResource();
                int quantity = resourceQuantity.getQuantity();
                ResourceStateGroup resourceStates = resources.get(resource);
                if (resourceStates == null) {
                    resourceStates = ResourceStateGroupFactory.createFor(resource);
                    resources.put(resource, resourceStates);
                }
                for (int i = 0; i < quantity; i++) {
                    resourceStates.add();
                }
                pendingResourceIterator.remove();
            }
        }

        turn++;

        solve();
    }

    public boolean hasReachedTarget() {
        for (ResourceQuantity resourceQuantity : target.values()) {
            Resource resource = resourceQuantity.getResource();
            int missing = getMissingResource(resource);
            if (missing > 0) {
                return false;
            }
        }
        return true;
    }

    public void solve() {
        if (hasReachedTarget()) {
            badoolizationMinimumTurnsCalculator.reachedTargetInTurn(turn);
        } else if (badoolizationMinimumTurnsCalculator.shouldContinueToTurn(turn+1)) {
            advanceTurn();
        }
    }
}
