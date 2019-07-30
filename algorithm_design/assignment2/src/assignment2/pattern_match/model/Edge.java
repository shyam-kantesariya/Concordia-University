package assignment2.pattern_match.model;

import assignment2.pattern_match.util.Suffix;

public class Edge {

        private int beginIndex;     // can't be changed
        private int endIndex;
        private TreeNode startNode;
        private TreeNode endNode;       // can't be changed, could be used as edge id

        // each time edge is created, a new end node is created
        public Edge(int beginIndex, int endIndex, TreeNode startNode) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.startNode = startNode;
            this.endNode = new TreeNode(startNode, null);
        }

        public TreeNode splitEdge(Suffix suffix) {
            remove();
            Edge newEdge = new Edge(beginIndex, beginIndex + suffix.getSpan(), suffix.getOriginNode());
            newEdge.insert();
            newEdge.endNode.setSuffixNode(suffix.getOriginNode());
            beginIndex += suffix.getSpan() + 1;
            startNode = newEdge.getEndNode();
            insert();
            return newEdge.getEndNode();
        }

        public void insert() {
            startNode.addEdge(beginIndex, this);
        }

        public void remove() {
            startNode.removeEdge(beginIndex);
        }

        public int getSpan() {
            return endIndex - beginIndex;
        }

        public int getBeginIndex() {
            return beginIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public void setEndIndex(int endIndex) {
            this.endIndex = endIndex;
        }

        public TreeNode getStartNode() {
            return startNode;
        }

        public void setStartNode(TreeNode startNode) {
            this.startNode = startNode;
        }

        public TreeNode getEndNode() {
            return endNode;
        }

        @Override
        public String toString() {
            return endNode.toString();
        }

}
