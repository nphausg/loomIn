fun main() {
    val trie = Trie()
    trie.insert("apple")
    trie.insert("app")
    trie.insert("apply")
    trie.insert("apricot")
    trie.insert("banana")
    println(trie.autocomplete("app")) // Output: [apple, app, apply]
}