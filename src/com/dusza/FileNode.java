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
        String path1;
        String[] split = str.split(" ");
        String[] pathSplit = split[0].split("/");

        path1 = split[0];

        this.size = Integer.parseInt(split[1]);
        this.time = Integer.parseInt(split[2]);

        this.isDirectory = path1.charAt(path1.length()-1) == '/' && size == 0;
        if (isDirectory) {
            path1 = path1.substring(0,path1.length()-1); // leveszi a végéről a / jelet
            this.name = pathSplit[pathSplit.length - 1];
            this.extension = "";
        } else {
            String tempName = pathSplit[pathSplit.length - 1];
            String[] nameSplit = tempName.split("\\.");
            this.extension = nameSplit[nameSplit.length - 1];
            this.name = tempName.replaceAll("\\."+extension, "");
        }

        this.path = path1;
        this.childList = new ArrayList<>();
    }
  
    public FileNode(String str, boolean isRoot) {
        if (isRoot) {
            this.name = "root";
            this.path = "";
            this.time = 0;
            this.size = 0;
            this.childList = new ArrayList<>();
            this.extension = "";
            this.isDirectory = true;
        }
        else {
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

            this.childList = new ArrayList<>();
        }
    }

    // methods

    public boolean isTrackable() {
        return name.charAt(0) == '.' ||
                extension.equals(".tmp") ||
                size > 104857600;
    }


    public void addChild(FileNode fn) {
        childList.add(fn);
    }


    // getters

    public FileNode getChild(String name) {
        for (FileNode node: childList) {
            if(node.getName().equals(name)) return node;
        }
        return this;
    }

    public String getFullName() {
        if(!isDirectory) {
            return name + "." + extension;
        } else {
            return name + "/";
        }

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
