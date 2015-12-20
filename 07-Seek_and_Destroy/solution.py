#!/usr/bin/env python3

import networkx as nx

TILE_TRAVERSABLE = "O"
TILE_IMPASSABLE = "X"
TILE_START = "B"
TILE_MOUSE = "M"


class NestBuilder:
    def __init__(self):
        self.graph = nx.Graph()
        self.start_position = None
        self.mice_positions = []
        self.current_row_index = 0
        self.current_column_index = 0

    def add_row(self, row):
        self.current_column_index = 0
        for tile_value in row:
            self.add_tile(tile_value)
            self.current_column_index += 1
        self.current_row_index += 1

    def add_tile(self, tile_value):
        tile_position = self.current_row_index, self.current_column_index
        tile_is_traversable = True
        if tile_value == TILE_TRAVERSABLE:
            self.traversable_tile(tile_position)
        elif tile_value == TILE_IMPASSABLE:
            tile_is_traversable = False
        elif tile_value == TILE_START:
            self.start_position = tile_position
            self.traversable_tile(tile_position)
        elif tile_value == TILE_MOUSE:
            self.mice_positions.append(tile_position)
            self.traversable_tile(tile_position)
        self.graph.add_node(tile_position, traversable=tile_is_traversable)

    def traversable_tile(self, tile):
        # check if we can go in up and left direction and add them to the graph
        # down and right directions will be checked in reverse by the corresponding tile's up and left lookup
        up_tile = self.get_up_tile_from_current()
        self.add_edge_if_dest_is_valid_and_traversable(tile, up_tile)
        left_tile = self.get_left_tile_from_current()
        self.add_edge_if_dest_is_valid_and_traversable(tile, left_tile)

    def get_up_tile_from_current(self):
        if self.current_row_index == 0:
            return None
        return self.current_row_index - 1, self.current_column_index

    def get_left_tile_from_current(self):
        if self.current_column_index == 0:
            return None
        return self.current_row_index, self.current_column_index - 1

    def add_edge_if_dest_is_valid_and_traversable(self, source, dest):
        if dest is not None and self.is_traversable(dest):
            self.graph.add_edge(source, dest)

    def is_traversable(self, tile):
        return self.get_tile_data(tile)["traversable"]

    def get_tile_data(self, tile):
        return self.graph.node[tile]


class Nest:
    def __init__(self, graph, start, mice):
        self.graph = graph
        self.start = start
        self.mice = mice
        self.max_steps = 0  # max steps allowed to perform, used to speed up
        self.min_steps = 0  # steps of the shortest path found

    def get_go_to_the_nearest_mouse_path_steps(self, current_steps, current_position, mice_remaining):
        # Return the number of steps for a path that is created by going to the nearest mouse at every step
        # This is not the optimal path, but it serves to know that a path exists with X steps, and then on the
        # optimal path search this value can be used to speed it up by stopping when the current path has reached
        # that number of steps
        if len(mice_remaining) == 0:
            return current_steps
        min_steps = None
        min_steps_mouse_position = None
        for mouse_position in mice_remaining:
            steps = current_steps + nx.shortest_path_length(self.graph, current_position, mouse_position)
            if min_steps is None or steps < min_steps:
                min_steps = steps
                min_steps_mouse_position = mouse_position
        mice_remaining_without_min = mice_remaining[:]  # copy list
        mice_remaining_without_min.remove(min_steps_mouse_position)
        return self.get_go_to_the_nearest_mouse_path_steps(min_steps, min_steps_mouse_position,
                                                           mice_remaining_without_min)

    def calculate_all_paths(self, max_steps):
        self.max_steps = max_steps
        # set min_steps to a tentative guess to speed up by stopping recursion when this guess is reached
        self.min_steps = self.get_go_to_the_nearest_mouse_path_steps(0, self.start, self.mice)
        self.calculate_path(0, self.start, self.mice)

    def calculate_path(self, current_steps, current_position, mice_remaining):
        if len(mice_remaining) == 1:  # last mouse
            mouse_position = mice_remaining[0]
            steps = current_steps + nx.shortest_path_length(self.graph, current_position, mouse_position)
            if steps <= self.max_steps and steps < self.min_steps:
                self.min_steps = steps
        else:  # more than one mouse left
            mouse_index = 0
            for mouse_position in mice_remaining:
                steps = current_steps + nx.shortest_path_length(self.graph, current_position, mouse_position)
                if steps < self.max_steps and steps < self.min_steps:
                    mice_remaining_without_current = mice_remaining[:]  # copy list
                    mice_remaining_without_current.pop(mouse_index)
                    self.calculate_path(steps, mouse_position, mice_remaining_without_current)
                mouse_index += 1


class MicePathCalculator:
    def __init__(self, nest, max_steps):
        self.nest = nest
        self.max_steps = max_steps

    def calculate_path(self):
        self.nest.calculate_all_paths(self.max_steps)

    def get_steps_left(self):
        if self.nest.min_steps > self.max_steps:
            return "NO"
        else:
            return self.max_steps - self.nest.min_steps


class Input:
    def run(self):
        number_of_cases = self.read_single_int_line()
        for c in range(number_of_cases):
            self.run_case()

    def run_case(self):
        max_steps = self.read_single_int_line()
        nest_width, nest_height = self.read_multiple_ints_line()
        nest_builder = NestBuilder()
        for row_index in range(nest_height):
            row = self.read_multiple_items_line()
            nest_builder.add_row(row)
        nest = Nest(nest_builder.graph, nest_builder.start_position, nest_builder.mice_positions)
        mice_path_calculator = MicePathCalculator(nest, max_steps)
        mice_path_calculator.calculate_path()
        print(mice_path_calculator.get_steps_left())

    def read_single_int_line(self):
        return int(self.read_single_item_line())

    def read_multiple_ints_line(self):
        return [int(i) for i in self.read_multiple_items_line()]

    def read_single_item_line(self):
        return input()

    def read_multiple_items_line(self):
        return self.read_single_item_line().split()


Input().run()
