package com.dusza;

import java.util.List;

public class FileTree {
    private final FileNode root;
    private FileNode pathNode;

    public FileTree(List<String> input) {
        root = new FileNode("root 0 0");
        pathNode = root;

        String path;
        FileNode parent;
        // built the tree:
        for (String line : input) {
            path = line.split(" ")[0];
            resolvePath(path).getChildList().add(new FileNode(line));
        }

    }

    private FileNode resolvePath(String path) {
        FileNode currentNode = root;
        for (String fileName : path.split("/")) {
            currentNode = root.getChild(fileName);
        }
        return currentNode;
    }

    public FileNode getCurrentDir() {
        return pathNode;
    }

    public boolean changeDirectory(String path) {
        return (pathNode = resolvePath(path)) != null;
    }

}
