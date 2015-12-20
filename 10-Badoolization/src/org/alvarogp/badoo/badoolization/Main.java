package org.alvarogp.badoo.badoolization;

import org.alvarogp.badoo.badoolization.builder.BadoolizationStateBuilder;
import org.alvarogp.badoo.badoolization.calculator.BadoolizationMinimumTurnsCalculator;
import org.alvarogp.badoo.badoolization.state.BadoolizationState;

import java.util.Scanner;

/**
 * Execution start.
 *
 * This class parses input from stdin and writes output to stdout.
 */
public class Main {
    private Scanner in = new Scanner(System.in);

    public void run() {
        BadoolizationState initialState = readState();
        BadoolizationState finalState = readState();
        int ralphTurns = readInt();

        BadoolizationMinimumTurnsCalculator minimumTurnsCalculator = new BadoolizationMinimumTurnsCalculator(initialState, finalState, ralphTurns);
        minimumTurnsCalculator.calculateMinimumTurns();

        write(minimumTurnsCalculator.getReport());
    }

    private BadoolizationState readState() {
        int numberOfLines = readInt();
        BadoolizationStateBuilder stateBuilder = new BadoolizationStateBuilder(numberOfLines);
        for (int i = 0; i < numberOfLines; i++) {
            String resource = readString();
            int quantity = readInt();
            stateBuilder.addResource(resource, quantity);
        }
        return stateBuilder.createState();
    }

    private int readInt() {
        return in.nextInt();
    }

    private String readString() {
        return in.next();
    }

    private void write(String line) {
        System.out.println(line);
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
