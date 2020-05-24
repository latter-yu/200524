public class ListTree {
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) {
            val = x;
        }
    }

    private TreeNode lca = null;
    //保存最近公共祖先
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        //给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
        //最近公共祖先的定义为：对于有根树 T 的两个结点 p、q，最近公共祖先表示为一个结点 x
        // 满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）

        //找最近公共祖先：从根节点出发，同时找给定的两个节点
        //查找 => 比较根节点 + 左子树中查找 + 右子树中查找
        //若找到公共节点，若在以上三处中的任意两处，该节点一定是最近公共祖先
        if(root == null) {
            return null;
        }
        findNode(root, p ,q);
        return lca;
    }
    //[约定]如果在 root 中找到 p 或者 q ,返回 true;否则返回 false.
    private boolean findNode(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null) {
            return false;
        }
        //后序遍历查找
        int left = findNode(root.left, p, q) ? 1 : 0;
        int right = findNode(root.right, p, q) ? 1 : 0;
        //访问根节点
        int mid = (root == p || root == q) ? 1 : 0;
        //注意（难理解）：三处中的任意两处
        //left, right, mid 取值都只有 0、1 两种情况
        //三者相加为 2, 意味着三个变量中一定有两个取值为 1, 一个为 0
        if(left + right + mid == 2) {
            lca = root;
        }
        return (left + right + mid) > 0;
        //找到 p 或者 q 任一个，返回 true; 否则返回 false
    }

    public TreeNode Convert(TreeNode pRootOfTree) {
        //输入一棵二叉搜索树，将该二叉搜索树转换成一个排序的双向链表（用中序遍历）
        // 要求不能创建任何新的结点，只能调整树中结点指针的指向（相当于 left = prev, right = next）

        //二叉搜索树：
        // 左子树中所有节点都小于根节点，右子树中所有节点都大于根节点（不能存在两个值相同的节点）
        //多应用于 查找 ，过程类似二分查找
        //其中序遍历结果是有序的

        if(pRootOfTree == null) {
            //返回值表示链表的头节点
            return null;
        }
        if(pRootOfTree.right == null && pRootOfTree.left == null) {
            //只有根节点, 链表也只有一个节点
            return pRootOfTree;
        }
        //先递归左子树，相当于把左子树完整的转换成双向链表了
        //left 就是左子树链表的头节点
        TreeNode left = Convert(pRootOfTree.left);
        //处理根节点：把根节点加到左子树链表的末尾（尾插法）
        TreeNode leftTail = left;
        while (leftTail != null && leftTail.right != null) {
            leftTail = leftTail.right;
            // right 相当于 next
        }
        //循环结束后，leafTail 就是 left 这个链表的最后一个节点
        //尾插根节点
        if(leftTail != null) {
            //leftTail = null,意味着左子树转换的链表为空
            //此时 root 为空
            leftTail.right = pRootOfTree;
            pRootOfTree.left = leftTail;
        }
        //最后递归处理右子树
        TreeNode right = Convert(pRootOfTree.right);
        //right 是右子树链表的头节点
        if(right != null) {
            //把根节点和右子树链接到一起
            //left = prev
            right.left = pRootOfTree;//(节点root)
            pRootOfTree.right = right;
        }
        return left != null ? left : pRootOfTree;
    }

    private int index = 0;//preorder 长度和 index 相同
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        //preorder 前序遍历
        //inorder 中序遍历
        index = 0;
        return buildTreeHelper(preorder, inorder, 0, inorder.length);
        //为了更好地描述子树
        //加入一对参数表示当前子树对应的中序遍历结果的区间 [0, inorder.length)
    }
    private TreeNode buildTreeHelper(int[] preorder, int[] inorder, int inorderLeft, int inorderRight) {
        if (inorderLeft >= inorderRight) {
            //中序区间为空区间，当前子树为空树
            return null;
        }
        if(index >= preorder.length) {
            //先序中所有元素都访问完毕了
            return  null;
        }
        //根据 index 取出当前树的根节点的值，并构造根节点
        TreeNode newNode = new TreeNode(preorder[index]);
        //根据根节点，在中序结果中，找出左子树和右子树的范围
        //确定位置
        //左子树对应的中序区间：[inorderLeft, pos)
        //右子树对应的中序区间：[pos + 1, inorderRight)
        int pos = find(inorder, inorderLeft, inorderRight, newNode.val);
        index++;
        newNode.left = buildTreeHelper(preorder, inorder, inorderLeft, pos);
        newNode.right = buildTreeHelper(preorder, inorder, pos+1, inorderRight);
        return newNode;
    }
    private int find(int[] inorder, int inorderLeft, int inorderRight, int val) {
        for(int i = inorderLeft; i < inorderRight; i++) {
            if(inorder[i] == val) {
                return i;
            }
        }
        return -1;
    }
}
