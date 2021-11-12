package com.dusza;

import javax.tools.DiagnosticListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileNode {
    private final String name;
    private final String extension;
    private final int time;
    private final int size;
    private final List<FileNode> childList;
    private final String path;
    private final boolean isDirectory;

    // constructor
    public FileNode(String str) {
        String[] split = str.split(" ");
        String[] pathSplit = split[0].split("/");

        this.path = split[0];

        this.isDirectory = Objects.equals(pathSplit[pathSplit.length - 1], "");
        if (isDirectory) {
            this.name = pathSplit[pathSplit.length - 2];
            this.extension = "";
        } else {
            this.name = pathSplit[pathSplit.length - 1];
            String[] nameSplit = name.split(" ");
            this.extension = nameSplit[nameSplit.length - 1];
        }

        this.size = Integer.parseInt(split[1]);
        this.time = Integer.parseInt(split[2]);

        this.childList = new ArrayList<FileNode>();

    }

    // methods

    public boolean isTrackable() {
        return name.charAt(0) == '.' ||
                extension.equals(".tmp") ||
                size > 104857600;
    }



    // getters

    public FileNode getChild(String name) {
        for (FileNode node: childList) {
            if(node.getName().equals(name)) return node;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public int getTime() {
        return time;
    }

    public int getSize() {
        return size;
    }

    public List<FileNode> getChildList() {
        return childList;
    }

    public String getPath() {
        return path;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
