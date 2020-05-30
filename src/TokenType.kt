enum class TokenType {
    //Single char tokens
    PLUS, MINUS, STAR, SLASH, BANG, AMPERSAND, BAR, CARET, QUESTION, COLON, SEMICOLON, LESS, GREATER, COMMA, DOT, OPEN_PAREN, CLOSE_PAREN, OPEN_BRACKET, CLOSE_BRACKET, OPEN_SQUARE, CLOSE_SQUARE, DOLLAR, EQUAL, NEWLINE, EOF,

    //Two char tokens
    PLUS_EQUAL, MINUS_EQUAL, STAR_EQUAL, SLASH_EQUAL, AMPERSAND_EQUAL, BAR_EQUAL, CARET_EQUAL, LESS_EQUAL, GREATER_EQUAL, EQUAL_EQUAL, STAR_STAR, PLUS_PLUS, MINUS_MINUS, EQUAL_ARROW, BAR_ARROW, QUESTION_QUESTION,

    //Keywords
    FOR, IF, WHILE, VAR,

    //Complex tokens
    KEYWORD, IDENTIFIER, INT, DOUBLE, CHAR, STRING
}