#!/usr/bin/env python3


class Case:
    def __init__(self, number_of_switches):
        self.number_of_switches = number_of_switches
        self.number_of_toggles = 0

    def run(self):
        self.number_of_toggles = self.min_toggles_for(self.number_of_switches)

    def min_toggles_for(self, switches):
        """
        If there are no switches, the toggles needed are 0.

        If there is only one switch, an unique toggle is needed.

        For two or more switches, the minimum number of toggles is
        two times the minimum number of toggles needed for one switch less,
        and for odd number of switches an additional toggle is necessary.
        """
        if switches < 1:
            return 0
        elif switches == 1:
            return 1
        else:
            toggles = self.min_toggles_for(switches - 1) * 2
            if switches % 2 == 1:  # odd number of switches
                toggles += 1
            return toggles


class Input:
    def run(self):
        number_of_cases = int(input())
        for c in range(number_of_cases):
            self.parse_case()

    def parse_case(self):
        number_of_switches = int(input())
        switch_system = Case(number_of_switches)
        switch_system.run()
        print(switch_system.number_of_toggles)


Input().run()
