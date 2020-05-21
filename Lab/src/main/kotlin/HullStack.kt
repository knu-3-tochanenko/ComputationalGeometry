class HullStack<T> {
    private var head: Node<T>? = null
    private var size = 0

    fun push(item: T) {
        val newNode = Node<T>()
        newNode.item = item
        newNode.next = head
        head = newNode
        size++
    }

    fun pop(): T? {
        if (head == null)
            return null
        val item: T? = head!!.item
        head = head!!.next
        size--
        return item
    }

    fun size(): Int {
        return size
    }

    override fun toString(): String {
        var s = ""
        var tmp = head
        while (tmp != null) {
            s += tmp.item.toString() + " -> "
            tmp = tmp.next
        }
        return s
    }

    fun peek(): T? {
        return head?.item
    }

    fun sneakyPeek(): T? {
        return if (head!!.next == null)
            null
        else head!!.next?.item
    }

    private class Node<T> {
        var item: T? = null
        var next: Node<T>? = null
    }
}