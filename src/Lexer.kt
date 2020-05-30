import TokenType.*
import java.io.File

class Lexer(private val program: String) {
    private val keywords = mapOf<String, TokenType>("if" to IF, "for" to FOR, "while" to WHILE, "var" to VAR)

    private var currentIndex: Int = 0

    private var lineNumber = 1

    private fun current(): Char = program[currentIndex]

    private fun previous(): Char = if (currentIndex > 0) program[currentIndex - 1] else '\u0000'

    private fun next() = currentIndex++

    private fun hasNext(): Boolean = currentIndex < program.length

    //ronak please don't bully me this is a highly necessary function
    private fun atEnd(): Boolean = !hasNext()

    private fun token(type: TokenType): Token = Token(type, lineNumber)

    private fun token(type: TokenType, value: Any): Token = Token(type, lineNumber, value)

    private fun get(): Char = current().also { next() }

    private fun match(vararg chars: Char): Boolean = hasNext() && chars.contains(current())

    private fun consume(vararg chars: Char): Boolean = match(*chars).also { if (it) next() }

    private fun getToken(): Token {
        var char = get();
        while (hasNext() && char == ' ') char = get();
        return char.let {
            when (it) {
                in '0'..'9' -> number()
                in 'a'..'z', in 'A'..'Z', '_' -> identifier()
                '"' -> string()
                '\'' -> char()
                else -> token(
                    when (it) {
                        '!' -> BANG
                        '$' -> DOLLAR
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
                        '\n' -> NEWLINE.also { lineNumber++ }
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
                            consume('>') -> BAR_ARROW
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
                )
            }
        }
    }

    private fun number(): Token {
        val number = StringBuilder()
        var isDouble = false;
        if (previous() == '0' && consume('x', 'X')) {
            while (consume('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'))
                number.append(previous())
            return token(INT, number.toString().toLong(16))
        } else {
            number.append(previous())
            while (consume('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'))
                number.append(previous().also { if (it == '.') isDouble = true })
        }
        return if (isDouble) token(INT, number().toString().toLong()) else token(DOUBLE, number().toString().toDouble())
    }

    private fun identifier(): Token {
        val identifier = StringBuilder()
        while (get().let {
                it in 'a'..'z' || it in 'a'..'z' || it == '_' || it in '0'..'9'
            }) {
            identifier.append(previous())
        }
        identifier.toString().let {
            val tokenType = keywords.getOrDefault(it) {
                IDENTIFIER
            } as TokenType
            return if (tokenType == IDENTIFIER)
                token(IDENTIFIER, it)
            else
                token(tokenType)
        }
    }

    //TODO support string interpolation
    private fun string(): Token {
        val string = StringBuilder()
        while (!consume('"')) string.append(previous())
        if (atEnd() && previous() != '"') throw LexException("Invalid String", lineNumber)
        return token(STRING, string.toString())
    }

    private fun char(): Token {
        val char = get()
        if (consume('\'')) return token(CHAR, char)
        else throw LexException("Invalid Character", lineNumber)
    }

    public fun lex(): MutableList<Token> {
        val tokens = mutableListOf<Token>();
        while (hasNext()) tokens.add(getToken())
        tokens.add(token(EOF))
        return tokens;
    }

}
