package assignment2.pattern_match.util;

import assignment2.pattern_match.model.Edge;
import assignment2.pattern_match.model.TreeNode;

public class SuffixTreeWithEdges {
    private String text;
    private TreeNode root;
    private int nodesCount;

    public SuffixTreeWithEdges(String text) {
        nodesCount = 0;
        this.text = text;
        root = new TreeNode(this, null);

        Suffix active = new Suffix(root, 0, -1);
        for (int i = 0; i < text.length(); i++) {
            addPrefix(active, i);
        }
    }

    private void addPrefix(Suffix active, int endIndex) {
        TreeNode lastParentNode = null;
        TreeNode parentNode;

        while (true) {
            Edge edge;
            parentNode = active.getOriginNode();

            // Step 1 is to try and find a matching edge for the given node.
            // If a matching edge exists, we are done adding edges, so we break out of this big loop.
            if (active.isExplicit()) {
                edge = active.getOriginNode().findEdge(text.charAt(endIndex));
                if (edge != null)
                    break;
            } else {
                //implicit node, a little more complicated
                edge = active.getOriginNode().findEdge(text.charAt(active.getBeginIndex()));
                int span = active.getSpan();
                if (text.charAt(edge.getBeginIndex() + span + 1) == text.charAt(endIndex))
                    break;
                parentNode = edge.splitEdge(active);
            }

            // We didn't find a matching edge, so we create a new one, add it to the tree at the parent node position,
            // and insert it into the hash table.  When we create a new node, it also means we need to create
            // a suffix link to the new node from the last node we visited.
            Edge newEdge = new Edge(endIndex, text.length() - 1, parentNode);
            newEdge.insert();
            updateSuffixNode(lastParentNode, parentNode);
            lastParentNode = parentNode;

            // This final step is where we move to the next smaller suffix
            if (active.getOriginNode() == root)
                active.incBeginIndex();
            else
                active.changeOriginNode();
            active.canonize();
        }
        updateSuffixNode(lastParentNode, parentNode);
        active.incEndIndex();   //Now the endpoint is the next active point
        active.canonize();
    }

    private void updateSuffixNode(TreeNode node, TreeNode suffixNode) {
        if ((node != null) && (node != root)) {
            node.setSuffixNode(suffixNode);
        }
    }

    public int getNewNodeNumber() {
        return nodesCount++;
    }

    public boolean contains(String str) {
        return indexOf(str) >= 0;
    }

    public int indexOf(String str) {
        if (str.length() == 0)
            return -1;

        int index = -1;
        TreeNode node = root;

        int i = 0;
        while (i < str.length()) {
            if ((node == null) || (i == text.length()))
                return -1;

            Edge edge = node.findEdge(str.charAt(i));
            if (edge == null)
                return -1;

            index = edge.getBeginIndex() - i;
            i++;

            for (int j = edge.getBeginIndex() + 1; j <= edge.getEndIndex(); j++) {
                if (i == str.length())
                    break;
                if (text.charAt(j) != str.charAt(i))
                    return -1;
                i++;
            }
            node = edge.getEndNode();
        }
        return index;
    }

    public String getText() {
        return text;
    }

    public TreeNode getRootNode() {
        return root;
    }
}
