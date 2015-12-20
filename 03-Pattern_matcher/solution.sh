#!/bin/bash

# This sed script converts a pattern received on the standard input
# to a valid regex pattern
# It substitutes
#   *     to .*          to match any characters
#   ?     to .           to match one character
#   ^     to [[:upper:]] to match uppercase characters
#   !...! to (.*)        to match any characters and allow later extraction to check its content
# Finally, it wraps pattern with ^...$ to match the entire line
PATTERN_CONVERTER_SED_SCRIPT='
	s/\*/.*/g
	s/\?/./g
	s/\^/[[:upper:]]/g
	s/![^!]*!/\\(.*\\)/g
	s/\(.*\)/^\1$/
'

# Splits the received pattern by the exclamation marks to store the
# forbidden text that is inside an exclamation mark scope
function store_text_inside_exclamation_scopes()
{
	local inside_exclamation_scope="n"
	declare -ga exclamation_scopes
	declare -i array_index=0
	while read subpattern
	do
		if [ "$inside_exclamation_scope" = "n" ]
		then
			# This time we are not on an exclamation mark scope
			# The next time we will be
			inside_exclamation_scope="y"
		else
			# We are inside an exclamation mark scope
			# Store the text in the current index of the array
			exclamation_scopes[$array_index]="$subpattern"
			array_index+=1
			# The next time we will not be inside the exclamation
			# marks scope
			inside_exclamation_scope="n"
		fi
	done < <(echo "$pattern" | tr '!' '\n')
}

# Converts the pattern from the received to a valid regex pattern to be
# used with grep and sed
# It uses the $PATTERN_CONVERTER_SED_SCRIPT sed script defined above
function convert_pattern()
{
	converted_pattern="$(echo "$pattern" | sed -e "$PATTERN_CONVERTER_SED_SCRIPT")"
}

# Check if there pattern contains any exclamation scope
# It uses the $exclamation_scopes array length to determine it
# Returns true if there are exclamation scopes, false otherwise
function pattern_contains_exclamation_scopes()
{
	[ "${#exclamation_scopes[@]}" -gt 0 ]
}

# For each exclamation scope, it checks that the forbidden text is not
# contained in the substring matched for that scope
function check_forbidden_text_in_exclamation_scopes()
{
	for (( i=0; i < "${#exclamation_scopes[@]}"; i++ ))
	do
		# Isolate the substring of the string_to_match that
		# matches for that scope
		scope_substring="$(echo "$string_to_match" | sed -e "s/$converted_pattern/\\$i/")"
		# Check if the substring contains the forbidden text for
		# the scope
		echo "$scope_substring" | grep -q "${exclamation_scopes[$i]}"
		if [ $? == 0 ]
		then
			# Forbidden text is contained in the substring,
			# so the entire string does not match this pattern
			echo "n"
			return
		fi
	done
	# No pattern contains the forbidden text, so the string matches
	# the pattern
	echo "y"
}

while read pattern
do
	read string_to_match

	store_text_inside_exclamation_scopes
	convert_pattern

	# Try matching the string with the converted pattern
	echo "$string_to_match" | grep -q "$converted_pattern"

	if [ $? == 0 ] # matched
	then
		# Once we know the pattern matches, if there are exclamation
		# scopes in the pattern we need to check that the forbidden
		# text for each scope is not present in the matched substring
		# for that scope
		if pattern_contains_exclamation_scopes
		then
			check_forbidden_text_in_exclamation_scopes
		else
			# No exclamation scopes and pattern already matched
			echo "y"
		fi
	else # not matched
		echo "n"
	fi
done
