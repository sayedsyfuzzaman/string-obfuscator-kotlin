import java.util.Random

fun main() {
    val obfuscatedString = obfuscateString("1234567891234567")
    println(obfuscatedString)
}

fun obfuscateString(input: String): String {
    val random = Random(System.currentTimeMillis())
    val bytes = input.toByteArray(Charsets.UTF_8)
    val length = bytes.size
    val buf = ByteArray(length)

    val stringBuilder = StringBuilder()
    stringBuilder.append("val obfuscatedString = object : Any() { \n")
    stringBuilder.append("\tvar t = 0 \n")
    stringBuilder.append("\toverride fun toString(): String { \n")
    stringBuilder.append("\t\tval buf = ByteArray($length) \n")

    for (i in 0 until length) {
        var t = random.nextInt()
        val f = random.nextInt(24) + 1

        t = (t and (0xff shl f).inv()) or (bytes[i].toInt() shl f)

        stringBuilder.append("\t\tt = $t \n")
        stringBuilder.append("\t\tbuf[$i] = (t ushr $f).toByte() \n")
    }

    stringBuilder.append("\t\treturn String(buf) \n")
    stringBuilder.append("\t}\n}.toString()")

    return stringBuilder.toString()
}
