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
        path = path.substring(1);
        if (path.equals("")) return root;
        for (String fileName : path.split("/")) {
            currentNode = currentNode.getChild(fileName);

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
