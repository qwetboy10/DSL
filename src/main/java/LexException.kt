class LexException(private val errorMessage: String, private val lineNumber: Int) : Throwable() {
    constructor(invalidCharacter: Char, lineNumber: Int) : this("Invalid Character $invalidCharacter", lineNumber)

    override fun toString(): String = "$errorMessage at $lineNumber"
}