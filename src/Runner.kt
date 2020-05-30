fun main() {
    val lexer = Lexer("3 + 4 - 2")

    lexer.lex().forEach {
        println(it)
    }
}