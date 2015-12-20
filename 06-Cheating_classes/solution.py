#!/usr/bin/env python3

import math


class StudentsClass:
    def __init__(self, answers):
        self.answers = answers
        number_of_students = len(answers)
        self.minimum_students_with_repeated_answers = math.ceil(number_of_students / 2)  # half or more students

    def is_cheating(self, minimum_sequence_of_answers):
        for answer in self.answers:
            current_start_index = 0
            current_end_index = minimum_sequence_of_answers
            while current_end_index <= len(answer):
                sequence = answer[current_start_index:current_end_index]
                number_of_answers_with_sequence = self.number_of_answers_with_sequence(sequence, current_start_index)
                if number_of_answers_with_sequence >= self.minimum_students_with_repeated_answers:
                    return True
                current_start_index += 1
                current_end_index += 1
        return False

    def number_of_answers_with_sequence(self, sequence, start_index):
        answers_with_sequence = 0
        for answer in self.answers:
            if answer.startswith(sequence, start_index):
                answers_with_sequence += 1
        return answers_with_sequence


class CheatingDetector:
    def __init__(self, minimum_sequence_of_answers):
        self.minimum_sequence_of_answers = minimum_sequence_of_answers
        self.class_index = 0
        self.cheating_classes = []

    def check_class(self, students_class):
        if students_class.is_cheating(self.minimum_sequence_of_answers):
            self.cheating_classes.append(self.class_index)
        self.class_index += 1


class Input:
    def run(self):
        number_of_cases = int(input())
        for c in range(number_of_cases):
            self.parse_case()

    def parse_case(self):
        number_of_classes, number_of_questions, answers_per_question, minimum_sequence_of_answers = [int(i) for i in input().split()]
        cheating_detector = CheatingDetector(minimum_sequence_of_answers)
        for class_index in range(number_of_classes):
            answers = input().split()[1:]
            students_class = StudentsClass(answers)
            cheating_detector.check_class(students_class)
        print(" ".join([str(i) for i in cheating_detector.cheating_classes]))


Input().run()
