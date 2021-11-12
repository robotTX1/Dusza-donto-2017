package com.dusza;

import java.util.List;

public class FileTree {
    private final FileNode root;
    private FileNode pathNode;

    public FileTree(List<String> input) {
        root = new FileNode("r/ 0 0", true);
        pathNode = root;

        String path;
        FileNode parent, newNode;
        // built the tree:
        for (String line : input) {
            newNode = new FileNode(line);

            resolvePath(newNode.getPath()).addChild(new FileNode(line));
        }
        System.out.println();

    }

    private FileNode resolvePath(String path) {
        FileNode currentNode = root;
        if (path.equals("") || path.equals("/")) return root;
        path = path.substring(1); // Leveszi az elejéről a / jelet
        for (String fileName : path.split("/")) {
            currentNode = currentNode.getChild(fileName);

        }
        return currentNode;
    }

    public FileNode getCurrentDir() {
        return pathNode;
    }

    public List<FileNode> getCurrentDirFiles() {
        return pathNode.getChildList();
    }

    public boolean changeDirectory(String path) {
        pathNode = resolvePath(path);
        return pathNode.getPath().equals(path);
    }

    public boolean stepBack() {
        if(pathNode == root) return false;
        pathNode = resolvePath(pathNode.getPath().replaceAll("/" + pathNode.getName(), ""));
        return true;
    }

}
