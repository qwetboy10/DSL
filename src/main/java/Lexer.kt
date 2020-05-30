import TokenType.*


class Lexer private constructor(private val program: String) {

    companion object {
        fun lex(program: String): MutableList<Token> {
            return Lexer(program).lex()
        }
    }

    private fun lex(): MutableList<Token> {
        val tokens = mutableListOf<Token>()
        while (!atEnd()) tokens.add(getToken())
        tokens.add(token(EOF))
        return tokens
    }

    private val keywords =
        mapOf(
            "if" to IF,
            "for" to FOR,
            "while" to WHILE,
            "var" to VAR,
            "null" to NULL,
            "true" to TRUE,
            "false" to FALSE
        )

    private var currentIndex: Int = 0

    private var lineNumber = 1

    private fun current(): Char = if (!atEnd()) program[currentIndex] else '\u0000'

    private fun previous(): Char = if (currentIndex > 0) program[currentIndex - 1] else '\u0000'

    private fun next() = currentIndex++

    private fun atEnd(): Boolean = currentIndex >= program.length

    private fun token(type: TokenType): Token = Token(type, lineNumber)

    private fun token(type: TokenType, value: Any): Token = Token(type, lineNumber, value)

    private fun get(): Char = current().also { next() }

    private fun match(vararg chars: Char): Boolean = !atEnd() && chars.contains(current())

    private fun consume(vararg chars: Char): Boolean = match(*chars).also { if (it) next() }

    private fun getToken(): Token {
        var char = get()
        while (!atEnd() && char == ' ') char = get()
        return char.let {
            when (it) {
                in '0'..'9' -> number()
                in 'a'..'z', in 'A'..'Z', '_' -> identifier()
                '"' -> string()
                '\'' -> char()
                '$' -> command()
                else -> token(
                    when (it) {
                        '!' -> BANG
                        '(' -> OPEN_PAREN
                        ')' -> CLOSE_PAREN
                        '[' -> OPEN_SQUARE
                        ']' -> CLOSE_SQUARE
                        '{' -> OPEN_BRACKET
                        '}' -> CLOSE_BRACKET
                        ':' -> COLON
                        ',' -> COMMA
                        ';' -> SEMICOLON
                        '.' -> DOT
                        '\\' -> BACKSLASH
                        '\n' -> NEWLINE
                        '?' -> when {
                            consume('?') -> QUESTION_QUESTION
                            else -> QUESTION
                        }
                        '+' -> when {
                            consume('=') -> PLUS_EQUAL
                            consume('+') -> PLUS_PLUS
                            else -> PLUS
                        }
                        '-' -> when {
                            consume('=') -> MINUS_EQUAL
                            consume('-') -> MINUS_MINUS
                            else -> MINUS
                        }
                        '*' -> when {
                            consume('=') -> STAR_EQUAL
                            consume('*') -> STAR_STAR
                            else -> STAR
                        }
                        '/' -> when {
                            consume('=') -> SLASH_EQUAL
                            else -> SLASH
                        }
                        '&' -> when {
                            consume('=') -> AMPERSAND_EQUAL
                            else -> AMPERSAND
                        }
                        '^' -> when {
                            consume('=') -> CARET_EQUAL
                            else -> CARET
                        }
                        '|' -> when {
                            consume('=') -> BAR_EQUAL
                            else -> BAR
                        }
                        '<' -> when {
                            consume('=') -> LESS_EQUAL
                            else -> LESS
                        }
                        '>' -> when {
                            consume('=') -> GREATER_EQUAL
                            else -> GREATER
                        }
                        '=' -> when {
                            consume('=') -> EQUAL_EQUAL
                            consume('>') -> EQUAL_ARROW
                            else -> EQUAL
                        }
                        else -> throw LexException(it, lineNumber)
                    }
                    //increment line number after token is created
                ).also { token -> if (token.type == NEWLINE) lineNumber++ }
            }
        }
    }

    //TODO add interpolation
    private fun command(): Token {
        if (!consume('(')) throw LexException("$ must be found before '('", lineNumber)
        val command = StringBuffer()
        while (!consume(')')) {
            command.append(get())
            if(atEnd()) throw LexException("Invalid Command Expression", lineNumber)
        }
        if (atEnd() && previous() != ')') throw LexException("Invalid Command Expression", lineNumber)
        return token(COMMAND, command.toString())
    }

    private fun number(): Token {
        val number = StringBuilder()
        var isDouble = false
        if (previous() == '0' && consume('x', 'X')) {
            while (consume(
                    '0',
                    '1',
                    '2',
                    '3',
                    '4',
                    '5',
                    '6',
                    '7',
                    '8',
                    '9',
                    'a',
                    'b',
                    'c',
                    'd',
                    'e',
                    'f',
                    'A',
                    'B',
                    'C',
                    'D',
                    'E',
                    'F'
                )
            )
                number.append(previous())
            return token(INT, number.toString().toLong(16))
        } else {
            number.append(previous())
            while (consume('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'))
                number.append(previous().also { if (it == '.') isDouble = true })
        }

        return if (!isDouble) token(INT, number.toString().toLong()) else token(DOUBLE, number.toString().toDouble())
    }

    private fun identifier(): Token {
        val identifier = StringBuilder()
        identifier.append(previous())
        while (get().let { it in 'a'..'z' || it in 'A'..'Z' || it == '_' || it in '0'..'9' })
            identifier.append(previous())

        identifier.toString().let {
            val tokenType = keywords.getOrDefault(it, IDENTIFIER)
            return if (tokenType == IDENTIFIER)
                token(IDENTIFIER, it)
            else
                token(tokenType)
        }
    }

    //TODO support string interpolation
    //TODO add more escape sequences?
    private fun string(): Token {
        val string = StringBuilder()
        while (!consume('"')) {
            get().let {
                if (it == '\\') {
                    when (get()) {
                        't' -> string.append('\t')
                        'n' -> string.append('\n')
                        '\\' -> string.append('\\')
                        else -> throw LexException("Invalid Escape Sequence", lineNumber)
                    }
                } else
                    string.append(it)
            }
            if (atEnd()) throw LexException("Invalid String", lineNumber)
        }
        if (atEnd() && previous() != '"') throw LexException("Invalid String", lineNumber)
        return token(STRING, string.toString())
    }

    //TODO add more escape sequences?
    private fun char(): Token {
        val char = get()
        if (char == '\\') {
            return token(
                CHAR,
                when (get()) {
                    't' -> '\t'
                    'n' -> '\n'
                    '\\' -> '\\'
                    else -> throw LexException("Invalid Escape Sequence", lineNumber)
                }
            ).also {
                if (!consume('\''))
                    throw LexException("Invalid Character '$char'", lineNumber)
            }

        }
        else {
            if (consume('\'')) return token(CHAR, char)
            else throw LexException("Invalid Character? '$char'", lineNumber)
        }
    }

}
