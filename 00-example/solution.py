#!/usr/bin/env python3

def get_case_data():
	return [int(i) for i in input().split()]

# Using recursive implementation
def get_gcd(a, b):
	return get_gcd(b, a % b) if b != 0 else a

def print_number_or_ok_if_equals(number, guess):
	print("OK" if number == guess else number)

number_of_cases = int(input())
for case in range(number_of_cases):
	first_integer, second_integer, proposed_gcd = get_case_data()
	real_gcd = get_gcd(first_integer, second_integer)
	print_number_or_ok_if_equals(real_gcd, proposed_gcd)
