import java.util.*

fun main() {
    val scan = Scanner(System.`in`)
    while (true) {
        Lexer.lex(scan.nextLine() + "\n").forEach { println(it) }
    }
}