#!/usr/bin/env python

import pylpsolve


class Weaver:
    def __init__(self, max_hours_of_work, hours_to_complete_goods):
        self.max_hours_of_work = max_hours_of_work
        self.hours_to_complete_goods = hours_to_complete_goods

    def get_hours_to_complete(self, good):
        return self.hours_to_complete_goods[good.index]


class Good:
    def __init__(self, index):
        self.index = index


class OptimalFactoryProductionCalculator:
    def __init__(self, goods, weavers, expected_hours):
        self.goods = goods
        self.weavers = weavers
        self.expected_hours = expected_hours
        self.min_worked_hours = None
        self.lp = pylpsolve.LP()

    def calculate_optimal_production(self):
        self.set_objective()
        self.add_constraints()
        self.solve()

    def set_objective(self):
        objective = [1 for w in self.weavers for g in self.goods]
        self.lp.setObjective(objective, mode="minimize")

    def add_constraints(self):
        self.add_weaver_cannot_exceed_max_hours_constraint()
        self.add_all_goods_need_to_be_done_constraint()

    def add_weaver_cannot_exceed_max_hours_constraint(self):
        for weaver in self.weavers:
            weaver_cells = [1 if w == weaver else 0 for w in self.weavers for g in self.goods]
            self.lp.addConstraint(weaver_cells, "<=", weaver.max_hours_of_work)

    def add_all_goods_need_to_be_done_constraint(self):
        for good in self.goods:
            good_cells_prorated_by_weaver_hours = [1.0 / w.get_hours_to_complete(g) if g == good else 0
                                                   for w in self.weavers for g in self.goods]
            self.lp.addConstraint(good_cells_prorated_by_weaver_hours, "==", 1)

    def solve(self):
        try:
            self.lp.solve()
        except pylpsolve.LPException:
            # cannot produce all goods
            self.min_worked_hours = None
        else:
            self.min_worked_hours = self.lp.getSolution().sum()

    def get_report(self):
        if self.min_worked_hours is None:
            # there is no way to produce all goods
            return "NO"
        hours_diff = int(round(self.min_worked_hours - self.expected_hours))
        if hours_diff == 0:
            return "OK"
        else:
            return hours_diff


class Input:
    def run(self):
        number_of_cases = self.read_single_int_line()
        for c in range(number_of_cases):
            self.run_case()

    def run_case(self):
        number_of_goods = self.read_single_int_line()
        goods = []
        for good_index in range(number_of_goods):
            good = Good(good_index)
            goods.append(good)
        number_of_weavers = self.read_single_int_line()
        expected_hours = self.read_single_int_line()
        weavers = []
        for weaver_index in range(number_of_weavers):
            weaver_data = self.read_multiple_ints_line()
            hours_of_work = weaver_data[0]
            hours_to_complete_goods = weaver_data[1:]
            weaver = Weaver(hours_of_work, hours_to_complete_goods)
            weavers.append(weaver)
        optimal_factory_production_calculator = OptimalFactoryProductionCalculator(goods, weavers, expected_hours)
        optimal_factory_production_calculator.calculate_optimal_production()
        print(optimal_factory_production_calculator.get_report())

    def read_single_int_line(self):
        return int(self.read_single_item_line())

    def read_multiple_ints_line(self):
        return [int(i) for i in self.read_multiple_items_line()]

    def read_single_item_line(self):
        return raw_input()

    def read_multiple_items_line(self):
        return self.read_single_item_line().split()


Input().run()
