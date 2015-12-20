#!/usr/bin/env python3


class MessageBroadcast:
    def __init__(self, friend_graph, start_person):
        self.friend_graph = friend_graph
        self.start_person = start_person
        self.people_without_message = list(friend_graph.keys())
        self.step_number = 0
        self.new_people_with_message_in_last_step = []

    def broadcast(self):
        self.people_without_message.remove(self.start_person)
        self.new_people_with_message_in_last_step.append(self.start_person)
        while len(self.people_without_message) > 0 and len(self.new_people_with_message_in_last_step) > 0:
            self.broadcast_step()
            self.step_number += 1

    def broadcast_step(self):
        people_who_broadcast_in_this_step = self.new_people_with_message_in_last_step
        self.new_people_with_message_in_last_step = []
        for broadcaster in people_who_broadcast_in_this_step:
            self.send_message_to_all_friends(broadcaster)

    def send_message_to_all_friends(self, broadcaster):
        friends_of_broadcaster = self.friend_graph[broadcaster]
        for receiver in friends_of_broadcaster:
            self.receive_message(receiver)

    def receive_message(self, receiver):
        if receiver in self.people_without_message:
            self.people_without_message.remove(receiver)
            self.new_people_with_message_in_last_step.append(receiver)

    def has_notified_every_people(self):
        return len(self.people_without_message) == 0

    def get_number_of_steps(self):
        return self.step_number


class Case:
    def __init__(self):
        self.friend_graph = {}
        self.min_steps = -1
        self.start_persons_with_min_steps = []

    def add_person(self, person_id, their_friends):
        self.friend_graph[person_id] = their_friends

    def run_case(self):
        for person in self.friend_graph:
            self.try_broadcast_starting_with(person)

    def try_broadcast_starting_with(self, person):
        broadcast = MessageBroadcast(self.friend_graph, person)
        broadcast.broadcast()
        if broadcast.has_notified_every_people():
            steps = broadcast.get_number_of_steps()
            self.successful_broadcast_for(person, steps)

    def successful_broadcast_for(self, person, steps):
        if self.min_steps == -1 or steps < self.min_steps:
            self.new_broadcast_min_steps(steps, person)
        elif steps == self.min_steps:
            self.same_as_current_min_steps(person)

    def new_broadcast_min_steps(self, steps, person):
        self.min_steps = steps
        self.start_persons_with_min_steps = [person]

    def same_as_current_min_steps(self, person):
        self.start_persons_with_min_steps.append(person)

    def get_person_to_notify(self):
        if self.min_steps == -1:
            return "0"
        else:
            self.start_persons_with_min_steps.sort()
            return " ".join([str(i) for i in self.start_persons_with_min_steps])


number_of_cases = int(input())
for c in range(number_of_cases):
    case = Case()
    number_of_people = int(input())
    for p in range(number_of_people):
        case.add_person(p + 1, [int(i) for i in input().split()])
    case.run_case()
    print(case.get_person_to_notify())
