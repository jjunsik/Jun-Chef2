package com.hojung.junchef.util.parser.constant;

public class ChatGptParserConstant {
    public static final String RECIPE_NAME_TITLE = "음식 이름";
    public static final String INGREDIENT_RECIPE_REGEX = "\\[(재료|레시피)]:|\\[(재료|레시피)]|(재료:|레시피:)";
    public static final String START_INGREDIENT_TEXT = "[재료]";
    public static final String START_COOKING_ORDER_TEXT = "[만드는 방법]";

    public static boolean parserErrorCondition(StringBuilder ingredients, StringBuilder cookingOrder){
        return ingredients.toString().length() < 10 || cookingOrder.toString().length() < 20;
    }
}
