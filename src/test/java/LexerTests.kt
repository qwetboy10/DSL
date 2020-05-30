import TokenType.*
import io.kotest.assertions.asClue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
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
    "Test Numbers" {
        val stuff = "0 1 2 1.0 3333.3333 0x123 0xa 0xA 0Xa 0XA"
        val correct = listOf(0, 1, 2, 1.0, 3333.3333, 0x123, 0xa, 0xa, 0xa, 0xa)
        var iter = correct.iterator()
        Lexer.lex(stuff).forEach { x ->
            x.asClue {
                if (it.type != EOF) {
                    val cor = iter.next()
                    if (cor is Int) {
                        it.type shouldBe INT
                        it.value.shouldBeTypeOf<Long>()
                        it.value shouldBe cor
                    }
                    if (cor is Double) {
                        it.type shouldBe DOUBLE
                        it.value.shouldBeTypeOf<Double>()
                        it.value shouldBe cor
                    }
                }
            }
        }
    }
    "Test Chars" {
        val stuff = """'a'
                      |'0'
                      |'\t'
                      |'\n'
                      |'\\'""".trimMargin()
        val correct = listOf('a', '0', '\t', '\n', '\\')
        val iter = correct.iterator()
        Lexer.lex(stuff).forEach { x ->
            x.asClue {
                if (it.type != EOF && it.type != NEWLINE) {
                    val cor = iter.next()
                    it.type shouldBe CHAR
                    it.value.shouldBeTypeOf<Char>()
                    it.value shouldBe cor
                }
            }
        }
    }
    "Test Strings" {
        val stuff = """"a" "abc" """"".trimIndent()
        var correct = listOf("a", "abc", "").iterator()
        val lexed = Lexer.lex(stuff)
        lexed.forEach {
            if (it.type != EOF) {
                it.type shouldBe STRING
                it.value.shouldBeTypeOf<String>()
                it.value shouldBe correct.next()
            }
        }

    }
    "Unbounded String" {
        shouldThrow<LexException> {
            Lexer.lex("\"abc")
        }
    }
    "Test Commands" {
        val stuff = """$(ls) $(ls -l) $() $(abadad)""".trimIndent()
        var correct = listOf("ls", "ls -l", "", "abadad").iterator()
        val lexed = Lexer.lex(stuff)
        lexed.forEach {
            if (it.type != EOF) {
                it.type shouldBe COMMAND
                it.value.shouldBeTypeOf<String>()
                it.value shouldBe correct.next()
            }
        }
    }
    "Unbounded Command" {
        shouldThrow<LexException> {
            Lexer.lex("$(")
        }
    }
    "Invalid Command" {
        shouldThrow<LexException> {
            Lexer.lex("$")
        }
    }
})