data class Token(val type: TokenType, val lineNumber: Int, val value: Any?) {
    constructor(type: TokenType, lineNumber: Int) : this(type, lineNumber, null)
}