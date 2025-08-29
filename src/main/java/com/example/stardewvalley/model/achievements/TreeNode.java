package com.example.stardewvalley.model.achievements;

class TreeNode<T extends Comparable<T>> {
    T value;
    TreeNode<T> left, right;

    TreeNode(T value) {
        this.value = value;
        this.left = this.right = null;
    }
}