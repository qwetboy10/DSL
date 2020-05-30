import TokenType.*
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotlintest.specs.StringSpec

class LexerTests : StringSpec({
    "Test basic tokens" {
        val testProgram = """
            + - * / ! & |
            ^ ? : ; < > , . ( ) { } [ ] = \
            += -= *= /= &= |= ^= <= >= == ** ++ -- => ??
            for if while var null true false
        """.trimIndent()
        val answers =
            listOf(
                //Single char tokens
                PLUS,
                MINUS,
                STAR,
                SLASH,
                BANG,
                AMPERSAND,
                BAR,
                NEWLINE,
                CARET,
                QUESTION,
                COLON,
                SEMICOLON,
                LESS,
                GREATER,
                COMMA,
                DOT,
                OPEN_PAREN,
                CLOSE_PAREN,
                OPEN_BRACKET,
                CLOSE_BRACKET,
                OPEN_SQUARE,
                CLOSE_SQUARE,
                EQUAL,
                BACKSLASH,
                NEWLINE,
                //Two char tokens
                PLUS_EQUAL,
                MINUS_EQUAL,
                STAR_EQUAL,
                SLASH_EQUAL,
                AMPERSAND_EQUAL,
                BAR_EQUAL,
                CARET_EQUAL,
                LESS_EQUAL,
                GREATER_EQUAL,
                EQUAL_EQUAL,
                STAR_STAR,
                PLUS_PLUS,
                MINUS_MINUS,
                EQUAL_ARROW,
                QUESTION_QUESTION,
                NEWLINE,
                //Keywords
                FOR,
                IF,
                WHILE,
                VAR,
                NULL,
                TRUE,
                FALSE,
                EOF
            )
        val iter = answers.iterator()
        Lexer.lex(testProgram).forEach {
            it.type shouldBe iter.next()
        }
    }
    "Test identifiers" {
        val good = "a abc ah_A_a ___ a123 a__123_aaa wahoo"
        val goodIter = good.split(" ").iterator()

        Lexer.lex(good).forEach { x ->
            x.asClue {
                if (it.type != EOF) {
                    it.type shouldBe IDENTIFIER
                    it.value.shouldBeTypeOf<String>()
                    it.value shouldBe goodIter.next()
                }
            }
        }

    }
}
)