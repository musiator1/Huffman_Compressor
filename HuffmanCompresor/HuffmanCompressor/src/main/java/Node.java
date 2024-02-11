public class Node implements Comparable<Node> {
    public int count = 0;
    public char value;
    public Node left;
    public Node right;

    public Node() {

    }

    public Node(int count, Node left, Node right) {
        this.count = count;
        this.left = left;
        this.right = right;
    }

    public Node(char value, Node left, Node right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public void incrementCounter() {
        this.count++;
    }

    public static Node makeTree(PriorityQueue<Node> forest) {
        while (forest.getSize() > 1) {
            Node min1 = forest.getMin();
            Node min2 = forest.getMin();

            Node combined = new Node(min1.count + min2.count, min1, min2);
            forest.add(combined);
        }
        return forest.getMin();
    }

    public static void generateCodes(String[] codes, StringBuilder buff, Node node) {
        if (node == null)
            return;

        if (node.left != null) {
            buff.append('0');
            generateCodes(codes, buff, node.left);
            buff.deleteCharAt(buff.length() - 1);

            buff.append('1');
            generateCodes(codes, buff, node.right);
            buff.deleteCharAt(buff.length() - 1);
        } else {
            codes[node.value] = buff.toString();
        }
    }

    @Override
    public int compareTo(Node o) {
        return this.count - o.count;
    }
}
