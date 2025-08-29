package com.example.stardewvalley.model.achievements;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> {
    private TreeNode<T> root;

    public void insert(T value) {
        root = insertRec(root, value);
    }

    private TreeNode<T> insertRec(TreeNode<T> root, T value) {
        if (root == null) {
            return new TreeNode<>(value);
        }

        int compareResult = value.compareTo(root.value);
        if (compareResult < 0) {
            root.left = insertRec(root.left, value);
        } else if (compareResult > 0) {
            root.right = insertRec(root.right, value);
        }

        return root;
    }

    public T search(T value) {
        return searchRec(root, value);
    }

    private T searchRec(TreeNode<T> root, T value) {
        if (root == null) {
            return null;
        }

        int compareResult = value.compareTo(root.value);
        if (compareResult == 0) {
            return root.value;
        } else if (compareResult < 0) {
            return searchRec(root.left, value);
        } else {
            return searchRec(root.right, value);
        }
    }

    public List<T> getAllAchievements() {
        List<T> achievements = new ArrayList<>();
        inorderRec(root, achievements);
        return achievements;
    }

    private void inorderRec(TreeNode<T> root, List<T> achievements) {
        if (root != null) {
            inorderRec(root.left, achievements);
            achievements.add(root.value);
            inorderRec(root.right, achievements);
        }
    }

    public void inorderTraversal() {
        inorderTraversalRec(root);
    }

    private void inorderTraversalRec(TreeNode<T> root) {
        if (root != null) {
            inorderTraversalRec(root.left);
            System.out.println(root.value);
            inorderTraversalRec(root.right);
        }
    }

    private static class TreeNode<T> {
        T value;
        TreeNode<T> left, right;

        TreeNode(T value) {
            this.value = value;
            left = right = null;
        }
    }
}