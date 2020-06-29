package main.java;

public class RBTree {

    /**
     * 根节点
     */
    private Node root;

    /**
     * 打印，先先序打印，再中序打印
     */
    public void print() {
        if (root == null) {
            System.out.println("null");
            return;
        }
        System.out.println("pre:");
        this.prePrint(root);
        System.out.println();
        System.out.println("mid:");
        this.midPrint(root);
        System.out.println();
    }

    /**
     * 插入数据
     *
     * @param val 插入的数据
     */
    public void put(Integer val) {
        // 如果根节点为空，直接设置为根节点
        if (root == null) {
            Node head = new Node(val);
            head.setRed(false);
            this.root = head;
            return;
        }
        addNode(val);
    }

    /**
     * 删除数据
     *
     * @param val 要删除的数据
     */
    public void remove(Integer val) {
        // 查找要删除的数据存不存在
        Node node = getNode(val);
        // 不存在直接返回
        if (node == null) {
            return;
        }
        this.fixAndRemoveNode(node);
    }

    /**
     * 修复删除节点
     *
     * @param node 要修复或删除的节点
     */
    private void fixAndRemoveNode(Node node) {
        if (node.getRed()) {
            // 节点为红色
            if (node.getLeft() == null && node.getRight() == null) {
                // 如果节点是红色的且没有子节点，直接删除该节点
                this.removeNode(node);
            } else {
                // 否则一定是两个黑色子节点，把后继节点的值赋值给该节点，将后继节点当作要删除的节点再执行该方法
                Node successorNode = this.getSuccessorNode(node);
                node.setVal(successorNode.getVal());
                this.fixAndRemoveNode(successorNode);
            }
        } else {
            // 节点为黑色
            // 如果只有一个子节点，则子节点一定为红色的，将子节点替代为当前节点并变为黑色
            if (node.getLeft() == null && node.getRight() != null) {
                Node parent = node.getParent();
                Node right = node.getRight();
                if (this.removeNode(node)) {
                    parent.setLeft(right);
                } else {
                    parent.setRight(right);
                }
                right.setParent(parent);
            } else if (node.getLeft() != null && node.getRight() == null) {
                Node parent = node.getParent();
                Node left = node.getLeft();
                if (this.removeNode(node)) {
                    parent.setLeft(left);
                } else {
                    parent.setRight(left);
                }
                left.setParent(parent);
            } else if (node.getLeft() != null && node.getRight() != null) {
                // 如果有两个子节点，同样把后继节点的值赋值给该节点，将后继节点当作要删除的节点再执行该方法
                Node successorNode = this.getSuccessorNode(node);
                node.setVal(successorNode.getVal());
                this.fixAndRemoveNode(successorNode);
            } else {
                // 没有子节点
                this.fixNoSon(node, true);
            }
        }
    }

    /**
     * 修复没有子节点的情况
     *
     * @param node 要修复或者删除的节点
     * @param del 是否需要删除该节点
     */
    private void fixNoSon(Node node, Boolean del) {
        Node siblingNode = this.getSiblingNode(node);
        Node parent = node.getParent();
        if (siblingNode.getRed()) {
            // 如果兄弟节点是红色的，将父节点变为红色，兄弟节点变为黑色，左旋/右旋父节点，再次执行该方法
            parent.setRed(true);
            siblingNode.setRed(false);
            if (this.isLeft(node)) {
                this.rotateLeft(parent);
            } else {
                this.rotateRight(parent);
            }
            this.fixNoSon(node, del);
        } else {
            // 兄弟节点是黑色的
            if (siblingNode.getLeft() != null && siblingNode.getLeft().getRed()) {
                // 如果兄弟节点的左子节点是红色的
                if (this.isLeft(node)) {
                    // 该节点也是其父节点的左子节点，右旋兄弟节点，将新的兄弟节点的颜色设置为父节点的颜色，父节点设置为黑色，左旋父节点
                    this.rotateRight(siblingNode);
                    Node newSiblingNode = this.getSiblingNode(node);
                    newSiblingNode.setRed(parent.getRed());
                    parent.setRed(false);
                    this.rotateLeft(parent);
                } else {
                    // 该节点是其父节点的右子节点，将兄弟节点的颜色设置为父节点的颜色，父节点设置为黑色，右旋父节点
                    siblingNode.setRed(parent.getRed());
                    parent.setRed(false);
                    siblingNode.getLeft().setRed(false);
                    this.rotateRight(parent);
                }
                if (del) {
                    // 如果是需要删除的，则删除该节点
                    this.removeNode(node);
                }
            } else if (siblingNode.getRight() != null && siblingNode.getRight().getRed()) {
                // 如果兄弟节点的右子节点是红色的
                if (this.isLeft(node)) {
                    // 该节点是其父节点的左子节点，将兄弟节点的颜色设置为父节点的颜色，父节点设置为黑色，左旋父节点
                    siblingNode.setRed(parent.getRed());
                    parent.setRed(false);
                    siblingNode.getRight().setRed(false);
                    this.rotateLeft(parent);
                } else {
                    // 该节点也是其父节点的右子节点，左旋兄弟节点，将新的兄弟节点的颜色设置为父节点的颜色，父节点设置为黑色，右旋父节点
                    this.rotateLeft(siblingNode);
                    Node newSiblingNode = this.getSiblingNode(node);
                    newSiblingNode.setRed(parent.getRed());
                    parent.setRed(false);
                    this.rotateRight(parent);
                }
                if (del) {
                    // 如果是需要删除的，则删除该节点
                    this.removeNode(node);
                }
            } else {
                // 兄弟节点的子节点都为黑色，如果需要删除，则先删除该节点，将兄弟节点设置为红色，如果父节点为红色，则将父节点设置为黑色，
                // 结束；否则，如果父节点不是根节点，将父节点当作待修复的节点，再次执行该方法
                if (del) {
                    this.removeNode(node);
                }
                siblingNode.setRed(true);
                if (parent.getRed()) {
                    parent.setRed(false);
                } else {
                    if (parent != root) {
                        // 节点已删除，只修复红黑树，不再删除节点
                        this.fixNoSon(parent, false);
                    }
                }
            }
        }
    }

    /**
     * 获取兄弟节点
     *
     * @param node 待获取兄弟节点的节点
     * @return 兄弟节点
     */
    private Node getSiblingNode(Node node) {
        if (this.isLeft(node)) {
            return node.getParent().getRight();
        } else {
            return node.getParent().getLeft();
        }
    }

    /**
     * 是否是父节点的左子节点
     *
     * @param node 待确认节点
     * @return 是否是左子节点
     */
    private Boolean isLeft(Node node) {
        return node == node.getParent().getLeft();
    }

    /**
     * 删除节点
     *
     * @param node 待删除的节点
     * @return 删除的节点是否是其父节点的左子节点
     */
    private Boolean removeNode(Node node) {
        if (this.isLeft(node)) {
            node.getParent().setLeft(null);
            return true;
        } else {
            node.getParent().setRight(null);
            return false;
        }
    }

    /**
     * 获取后继节点
     *
     * @param node 待获取后继节点的节点
     * @return 后继节点
     */
    private Node getSuccessorNode(Node node) {
        if (node.getRight() == null) {
            return null;
        }
        Node tmp = node.getRight();
        while (tmp.getLeft() != null) {
            tmp = tmp.getLeft();
        }
        return tmp;
    }

    /**
     * 获取对应数值的节点
     *
     * @param val 数值
     * @return 返回对应节点，没有则为null
     */
    private Node getNode(Integer val) {
        Node tmp = root;
        while (tmp != null) {
            if (tmp.getVal().equals(val)) {
                return tmp;
            } else if (tmp.getVal() > val) {
                tmp = tmp.getLeft();
            } else if (tmp.getVal() < val) {
                tmp = tmp.getRight();
            }
        }
        return null;
    }

    /**
     * 添加节点
     *
     * @param val 插入的数据
     */
    private void addNode(Integer val) {
        Node tmp = root;
        Node n;
        // 判断插入数据大小，和树中的数据比较，插入相应的节点中
        while (true) {
            if (tmp.getVal() >= val) {
                if (tmp.getLeft() == null) {
                    n = new Node(val);
                    n.setParent(tmp);
                    tmp.setLeft(n);
                    break;
                } else {
                    tmp = tmp.getLeft();
                }
            }
            if (tmp.getVal() < val) {
                if (tmp.getRight() == null) {
                    n = new Node(val);
                    n.setParent(tmp);
                    tmp.setRight(n);
                    break;
                } else {
                    tmp = tmp.getRight();
                }
            }
        }
        // 修复红黑树
        fixNode(n);
    }

    /**
     * 修复红黑树
     *
     * @param node 待修复的节点
     */
    private void fixNode(Node node) {
        // 如果待修复的节点是根节点，直接将根节点设置为黑色
        if (node == root) {
            node.setRed(false);
            return;
        }
        // 如果父节点是黑色，直接返回
        if (!node.getParent().getRed()) {
            return;
        }
        Node uncle = this.getUncle(node);
        if (uncle != null && uncle.getRed()) {
            // 如果叔父节点是红色的，将父节点和叔父节点都设置为黑色，将祖父节点设置为红色，再将祖父节点当作新插入的节点再次修复红黑树
            node.parent.setRed(false);
            uncle.setRed(false);
            this.getGrandParent(node).setRed(true);
            this.fixNode(this.getGrandParent(node));
        } else if (uncle == null || !uncle.getRed()) {
            // 如果叔父节点为空或者是黑色的
            if (this.isLeft(node) && node.getParent() == this.getGrandParent(node).getLeft()) {
                // 1、三点一线（新增节点和父节点都是它们父节点的左节点），
                // 将新增节点的父节点设置为黑色，祖父节点设置为红色，再将祖父节点右旋
                this.fixLeftNode(node);
            } else if (!this.isLeft(node) && node.getParent() == this.getGrandParent(node).getRight()) {
                // 2、三点一线（新增节点和父节点都是它们父节点的右节点），
                // 将新增节点的父节点设置为黑色，祖父节点设置为红色，再将祖父节点左旋
                this.fixRightNode(node);
            } else if (!this.isLeft(node) && node.getParent() == this.getGrandParent(node).getLeft()) {
                // 3、三点不在一条线上（新增节点是父节点的右子节点，父节点是祖父节点的左子节点），
                // 先将父节点左旋，再进行1的操作
                rotateLeft(node.getParent());
                this.fixLeftNode(node.getLeft());
            } else if (this.isLeft(node) && node.getParent() == this.getGrandParent(node).getRight()) {
                // 4、三点不在一条线上（新增节点是父节点的左子节点，父节点是祖父节点的右子节点），
                // 先将父节点右旋，再进行2的操作
                rotateRight(node.getParent());
                this.fixRightNode(node.getRight());
            }
        }
    }

    /**
     * 左三点一线修复
     *
     * @param node 待修复的节点
     */
    private void fixLeftNode(Node node) {
        node.getParent().setRed(false);
        this.getGrandParent(node).setRed(true);
        this.rotateRight(this.getGrandParent(node));
    }

    /**
     * 右三点一线修复
     *
     * @param node 待修复的节点
     */
    private void fixRightNode(Node node) {
        node.getParent().setRed(false);
        this.getGrandParent(node).setRed(true);
        this.rotateLeft(this.getGrandParent(node));
    }

    /**
     * 左旋
     *
     * @param node 待旋转的节点
     */
    private void rotateLeft(Node node) {
        Node right = node.getRight();
        // 将该节点的右子节点的父节点设置为该节点的父节点
        right.setParent(node.getParent());
        // 如果该节点不是根节点，将该节点的父节点的子节点设置为该节点的右节点
        if (node != root) {
            if (this.isLeft(node)) {
                node.getParent().setLeft(right);
            } else if (!this.isLeft(node)) {
                node.getParent().setRight(right);
            }
        } else {
            // 如果该节点是根节点，设置根节点为该节点的右节点
            root = right;
        }
        // 将该节点的右节点设置为该节点的右节点的左节点
        node.setRight(right.getLeft());
        if (right.getLeft() != null) {
            right.getLeft().setParent(node);
        }
        // 将该节点的父节点设置为该节点的右节点
        node.setParent(right);
        // 将该节点的右节点的左节点设置为该节点
        right.setLeft(node);
    }

    /**
     * 右旋
     *
     * @param node 待旋转的节点
     */
    private void rotateRight(Node node) {
        Node left = node.getLeft();
        // 将该节点的左子节点的父节点设置为该节点的父节点
        left.setParent(node.getParent());
        // 如果该节点不是根节点，将该节点的父节点的子节点设置为该节点的左节点
        if (node != root) {
            if (this.isLeft(node)) {
                node.getParent().setLeft(left);
            } else if (!this.isLeft(node)) {
                node.getParent().setRight(left);
            }
        } else {
            // 如果该节点是根节点，设置根节点为该节点的左节点
            root = left;
        }
        // 将该节点的左节点设置为该节点的左节点的左节点
        node.setLeft(left.getRight());
        if (left.getRight() != null) {
            left.getRight().setParent(node);
        }
        // 将该节点的父节点设置为该节点的左节点
        node.setParent(left);
        // 将该节点的左节点的左节点设置为该节点
        left.setRight(node);
    }

    /**
     * 获取叔父节点，如果父节点是祖父节点的左节点，返回祖父节点的右节点，反之亦然
     *
     * @param node 待获取叔父节点的节点
     * @return 叔父节点
     */
    private Node getUncle(Node node) {
        if (node.getParent() != root && node.getParent() == this.getGrandParent(node).getLeft()) {
            return this.getGrandParent(node).getRight();
        }
        if (node.getParent() != root && node.getParent() == this.getGrandParent(node).getRight()) {
            return this.getGrandParent(node).getLeft();
        }
        return null;
    }

    /**
     * 获取祖父节点
     *
     * @param node 待获取祖父节点的节点
     * @return 祖父节点
     */
    private Node getGrandParent(Node node) {
        return node.getParent().getParent();
    }

    /**
     * 先序遍历
     *
     * @param node 待遍历的节点
     */
    private void prePrint(Node node) {
        System.out.print(node.getVal() + "," + (node.getRed() ? "red" : "black") + "  ");
        if (node.getLeft() != null) {
            this.prePrint(node.getLeft());
        }
        if (node.getRight() != null) {
            this.prePrint(node.getRight());
        }
    }

    /**
     * 中序遍历
     *
     * @param node 待遍历的节点
     */
    private void midPrint(Node node) {
        if (node.getLeft() != null) {
            this.midPrint(node.getLeft());
        }
        System.out.print(node.getVal() + "," + (node.getRed() ? "red" : "black") + "  ");
        if (node.getRight() != null) {
            this.midPrint(node.getRight());
        }
    }

    static class Node {
        /**
         * 节点的值
         */
        private Integer val;

        /**
         * 红黑节点（true为红色节点，false为黑色节点）
         */
        private Boolean red;

        /**
         * 左子节点
         */
        private Node left;

        /**
         * 右子节点
         */
        private Node right;

        /**
         * 父节点
         */
        private Node parent;

        /**
         * 新增节点都先设置为红色
         *
         * @param val 新增节点的值
         */
        public Node(Integer val) {
            this.val = val;
            this.red = true;
        }

        public Integer getVal() {
            return val;
        }

        public void setVal(Integer val) {
            this.val = val;
        }

        public Boolean getRed() {
            return red;
        }

        public void setRed(Boolean red) {
            this.red = red;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }
    }
}
