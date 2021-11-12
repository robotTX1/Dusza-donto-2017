package com.dusza;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileTreeManager {
    private static final String FILE_NAME = "filerendszer-";
    private static final String FILE_EXTENSION = ".txt";
    public static final String FILE_REGEX = "^"+ FILE_NAME +"\\d+"+FILE_EXTENSION+"$";

    private final Path workDir;
    private FileTree previousFileTree;
    private FileTree currentFileTree;

    public FileTreeManager(Path workDir, int index) {
        this.workDir = workDir;
        loadFile(index);
    }

    public void loadFile(int index) {
        try {
            System.out.println(index);
            List<String> readLines = Files.readAllLines(workDir.resolve(FILE_NAME+index+FILE_EXTENSION));
            currentFileTree = new FileTree(readLines);

            if(Files.exists(workDir.resolve(FILE_NAME+(index-1)+FILE_EXTENSION))) {
                readLines = Files.readAllLines(workDir.resolve(FILE_NAME+(index-1)+FILE_EXTENSION));
                previousFileTree = new FileTree(readLines);
            } else {
                previousFileTree = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<FileChange> compare() {
        List<FileChange> changes = new ArrayList<>();


        return changes;
    }

    public boolean changeDir(String fileName) {
        return currentFileTree.changeDirectory(currentFileTree.getCurrentDir().getPath() + "/" + fileName);
    }

    public List<FileNode> getFiles() {
        return currentFileTree.getCurrentDirFiles();
    }

    public FileTree getCurrentFileTree() {
        return currentFileTree;
    }

    public boolean stepBack() {
        return currentFileTree.stepBack();
    }
}
