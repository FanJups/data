package jakarta.data.displaynamegeneration;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

public class ReplaceCamelCaseAndUnderscore extends DisplayNameGenerator.Standard {

    public ReplaceCamelCaseAndUnderscore() {
        super();
    }

    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return replaceCamelCaseAndUnderscore(super.generateDisplayNameForClass(testClass));
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return replaceCamelCaseAndUnderscore(super.generateDisplayNameForNestedClass(nestedClass));
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return replaceCamelCaseAndUnderscore(testMethod.getName()) + DisplayNameGenerator.parameterTypesAsString(testMethod);
    }

    public String replaceCamelCaseAndUnderscore(String input) {
        StringBuilder result = new StringBuilder();
        /*
         * Each method name starts with "should" then the displayed name starts with "Should"
         * */
        result.append(Character.toUpperCase(input.charAt(0)));

        /*
         * There are 2 groups of method name: with and without underscore
         * */
        if (input.contains("_")) {
            boolean insideUnderscores = false;
            for (int i = 1; i < input.length(); i++) {
                char currentChar = input.charAt(i);
                if (currentChar == '_') {
                    result.append(' ');
                    /*
                     * If the current char is an underscore and insideUnderscores is true,
                     * it means there is an opening underscore and this one is the closing one
                     * then we set insideUnderscores to false.
                     * */
                    /*
                     * If the current char is an underscore and insideUnderscores is false,
                     * it means there is not an opening underscore and this one is the opening one
                     * then we set insideUnderscores to true.
                     * */
                    insideUnderscores = !insideUnderscores;
                } else {
                    /*
                     * If the character is inside underscores, we append the character as it is.
                     * */
                    if (insideUnderscores) {
                        result.append(currentChar);
                    } else {
                        //CamelCase handling for method name containing "_"
                        if (Character.isUpperCase(currentChar)) {
                            //We already replace "_" with " ". If the previous character is "_", we will not add extra space
                            if (!(input.charAt(i - 1) == '_')) {
                                result.append(' ');
                            }
                            result.append(Character.toLowerCase(currentChar));
                        } else {
                            result.append(currentChar);
                        }
                    }
                }
            }
        } else {
            //CamelCase handling for method name not containing "_"
            for (int i = 1; i < input.length(); i++) {
                if (Character.isUpperCase(input.charAt(i))) {
                    result.append(' ');
                    result.append(Character.toLowerCase(input.charAt(i)));
                } else {
                    result.append(input.charAt(i));
                }
            }
        }
        return result.toString();
    }
}