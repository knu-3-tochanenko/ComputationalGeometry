class HullStack<T> {
    private var head //head of stack
            : Node<T>? = null
    private var size = 0
    fun push(item: T) { //push item onto stack
        val newNode = Node<T>()
        newNode.item = item
        newNode.next = head
        head = newNode
        size++
    }

    fun pop(): T? { //pop item off of stack
        if (head == null) return null //stack is empty
        val item: T? = head!!.item
        head = head!!.next
        size--
        return item
    }

    fun size(): Int {
        return size
    }

    val isEmpty: Boolean
        get() = size == 0

    override fun toString(): String {
        var s = ""
        var tmp = head
        while (tmp != null) {
            s += tmp.item.toString() + " -> "
            tmp = tmp.next
        }
        return s
    }

    fun peek(): T? { //look at top item in stack
        return head?.item // stack is empty
    }

    fun sneaky_peek(): T? { //look at item 2 places in
        return if (head!!.next == null) null else head!!.next?.item // stack is empty
    }

    private class Node<T> {
        var item: T? = null
        var next: Node<T>? = null
    }
}