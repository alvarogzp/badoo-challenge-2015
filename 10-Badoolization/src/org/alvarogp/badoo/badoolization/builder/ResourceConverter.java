package org.alvarogp.badoo.badoolization.builder;

import org.alvarogp.badoo.badoolization.resources.Resource;

/**
 * Converts from {@link Resource} enum names to external string representation of them.
 *
 * Resource enum names follow standard naming conventions for enums: UPPERCASE_AND_WITH_UNDERSCORES
 * External string representation is: CamelCase
 */
public class ResourceConverter {
    public static Resource fromString(String resourceName) {
        String enumName = getEnumNameFromStringRepresentation(resourceName);
        return Resource.valueOf(enumName);
    }

    public static String toString(Resource resource) {
        String enumName = resource.name();
        return getStringRepresentationFromEnumName(enumName);
    }

    private static String getEnumNameFromStringRepresentation(String stringRepresentation) {
        StringBuilder enumName = new StringBuilder();
        enumName.append(stringRepresentation.charAt(0));
        for (int i = 1; i < stringRepresentation.length(); i++) {
            char c = stringRepresentation.charAt(i);
            if (Character.isUpperCase(c)) {
                enumName.append("_");
            }
            enumName.append(Character.toUpperCase(c));
        }
        return enumName.toString();
    }

    private static String getStringRepresentationFromEnumName(String enumName) {
        char firstChar = enumName.charAt(0);
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append(firstChar);
        char previousChar = firstChar;
        for (int i = 1; i < enumName.length(); i++) {
            char currentChar = enumName.charAt(i);
            if (previousChar != '_') {
                currentChar = Character.toLowerCase(currentChar);
            }
            stringRepresentation.append(currentChar);
            previousChar = currentChar;
        }
        return stringRepresentation.toString();
    }
}
