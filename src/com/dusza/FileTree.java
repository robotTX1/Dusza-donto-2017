package com.dusza;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileTree {
    private final FileNode root;
    private FileNode pathNode;

    public FileTree(List<String> input) {
        root = new FileNode("r/ 0 0", true);
        pathNode = root;

        List<String> dirs = new ArrayList<>();

        String[] split;
        for (String value : input) {
            split = value.split(" ");
            if (split[1].equals("0")) dirs.add(value);
        }

        dirs.sort((o1, o2) -> {
            String splt1 = o1.split(" ")[0];
            String splt2 = o2.split(" ")[0];
            return splt1.length() - splt2.length();
        });

        for(String s : input) {
            if(!dirs.contains(s)) {
                dirs.add(s);
            }
        }


        String path;
        FileNode parent, newNode;
        // built the tree:
        for (String line : dirs) {
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
