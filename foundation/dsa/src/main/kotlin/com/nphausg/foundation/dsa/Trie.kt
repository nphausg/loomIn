class TrieNode {
    val children = mutableMapOf<Char, TrieNode>()
    var isEndOfWord = false
}

class Trie {
    private val root = TrieNode()

    // Insert a word into the Trie
    fun insert(word: String) {
        var current = root
        for (char in word) {
            current = current.children.getOrPut(char) { TrieNode() }
        }
        current.isEndOfWord = true
    }

    fun search(word: String): Boolean {
        var node = root
        for (c in word) {
            if (!node.children.containsKey(c)) {
                return false
            }
            node = node.children[c]!!
        }
        return node.isEndOfWord
    }

    fun startsWith(prefix: String): Boolean {
        var node = root
        for (c in prefix) {
            if (!node.children.containsKey(c)) {
                return false
            }
            node = node.children[c]!!
        }
        return true
    }

    // Retrieve words with a given prefix
    fun autocomplete(prefix: String): List<String> {
        val result = mutableListOf<String>()
        var current = root
        for (char in prefix) {
            current = current.children[char] ?: return emptyList()
        }
        collectWords(current, StringBuilder(prefix), result)
        return result
    }

    private fun collectWords(node: TrieNode, prefix: StringBuilder, result: MutableList<String>) {
        if (node.isEndOfWord) result.add(prefix.toString())
        for ((char, child) in node.children) {
            collectWords(child, prefix.append(char), result)
            prefix.deleteCharAt(prefix.length - 1)
        }
    }
}