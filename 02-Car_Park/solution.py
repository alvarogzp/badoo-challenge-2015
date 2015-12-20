#!/usr/bin/env python3

import datetime


DATE_INPUT_FORMAT = "%d.%m.%Y %H:%M:%S"
DATE_OUTPUT_FORMAT = "%d.%m.%Y"

PENNY_PRICE_FIRST_HOUR = 200
PENNY_PRICE_QUARTER_OF_AN_HOUR_AFTER_FIRST_HOUR = 50

SECONDS_IN_ONE_HOUR = 3600
SECONDS_IN_QUARTER_OF_AN_HOUR = 900


class CarRecord:
    def __init__(self, enter_time_string, exit_time_string):
        self.enter_datetime = datetime.datetime.strptime(enter_time_string, DATE_INPUT_FORMAT)
        self.exit_datetime = datetime.datetime.strptime(exit_time_string, DATE_INPUT_FORMAT)

    def get_pennies_paid(self):
        seconds_in_parking = self.seconds_in_parking()
        paid = PENNY_PRICE_FIRST_HOUR
        seconds_in_parking -= SECONDS_IN_ONE_HOUR
        while seconds_in_parking > 0:
            paid += PENNY_PRICE_QUARTER_OF_AN_HOUR_AFTER_FIRST_HOUR
            seconds_in_parking -= SECONDS_IN_QUARTER_OF_AN_HOUR
        return paid

    def seconds_in_parking(self):
        timedelta = self.exit_datetime - self.enter_datetime
        return int(timedelta.total_seconds())

    def get_enter_date(self):
        return self.enter_datetime.date()

    def get_exit_date(self):
        return self.exit_datetime.date()


class ParkingDailyCarsTracker:
    def __init__(self):
        self.current_cars = 0
        self.daily_max_cars = self.current_cars
        self.daily_penny_earnings = 0

    def new_day(self):
        self.daily_max_cars = self.current_cars
        self.daily_penny_earnings = 0

    def car_enters(self):
        self.current_cars += 1
        if self.current_cars > self.daily_max_cars:
            self.daily_max_cars = self.current_cars

    def car_leaves(self, car):
        self.current_cars -= 1
        self.daily_penny_earnings += car.get_pennies_paid()


class ParkingDailySummariesTracker:
    def __init__(self):
        self.max_cars = -1
        self.max_cars_date = None
        self.total_penny_earnings = 0

    def add_day_summary(self, summary):
        self.check_max_cars(summary.max_cars, summary.date)
        self.total_penny_earnings += summary.penny_earnings

    def check_max_cars(self, max_cars, date):
        if self.max_cars == -1 or max_cars > self.max_cars:
            self.max_cars = max_cars
            self.max_cars_date = date

    def print_total_summary(self):
        if self.max_cars != -1:
            print_date = self.max_cars_date.strftime(DATE_OUTPUT_FORMAT)
            print("PEAK", self.max_cars, "AT", print_date)
        total_pound_earnings = self.total_penny_earnings / 100
        print("TOTAL", total_pound_earnings)


class ParkingDaySummary:
    def __init__(self, date, max_cars, penny_earnings):
        self.date = date
        self.max_cars = max_cars
        self.penny_earnings = penny_earnings

    def print_summary(self):
        if self.penny_earnings > 0:
            print_date = self.date.strftime(DATE_OUTPUT_FORMAT)
            max_cars = self.max_cars
            pound_earnings = self.penny_earnings / 100
            print(print_date, max_cars, pound_earnings)


class Parking:
    def __init__(self):
        self.current_date = None
        self.actions = []
        self.cars_tracker = ParkingDailyCarsTracker()
        self.summary = ParkingDailySummariesTracker()

    def add_car(self, car):
        self.queue_car_enter(car)
        self.queue_car_exit(car)

    def queue_car_enter(self, car):
        self.actions.append((car.enter_datetime, car, self.enter_car))

    def queue_car_exit(self, car):
        self.actions.append((car.exit_datetime, car, self.exit_car))

    def enter_car(self, car):
        self.check_day(car.get_enter_date())
        self.cars_tracker.car_enters()

    def exit_car(self, car):
        self.check_day(car.get_exit_date())
        self.cars_tracker.car_leaves(car)

    def check_day(self, date):
        if self.current_date is None:  # first call
            self.current_date = date
        elif self.current_date != date:
            self.change_day_to(date)

    def change_day_to(self, date):
        self.close_day()
        self.current_date = date
        self.cars_tracker.new_day()

    def close_day(self):
        date = self.current_date
        max_cars = self.cars_tracker.daily_max_cars
        penny_earnings = self.cars_tracker.daily_penny_earnings
        day_summary = ParkingDaySummary(date, max_cars, penny_earnings)
        day_summary.print_summary()
        self.summary.add_day_summary(day_summary)

    def end(self):
        self.actions.sort(key=lambda x: x[0])
        for action in self.actions:
            action[2](action[1])
        if self.current_date is not None:
            self.close_day()
        self.summary.print_total_summary()


class Input:
    def __init__(self):
        self.parking = Parking()

    def run(self):
        number_of_records = int(input())
        for r in range(number_of_records):
            self.parse_record()
        self.parking.end()

    def parse_record(self):
        enter_time, exit_time = input().rstrip().split(" - ")
        car = CarRecord(enter_time, exit_time)
        self.parking.add_car(car)


Input().run()
