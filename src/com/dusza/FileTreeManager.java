package com.dusza;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FileTreeManager {
    private static final String FILE_NAME = "filerendszer-";
    private static final String FILE_EXTENSION = ".txt";
    public static final String FILE_REGEX = "^" + FILE_NAME + "\\d+" + FILE_EXTENSION + "$";

    private final Path workDir;
    private int currentIndex, previousIndex;
    private FileTree currentFileTree;

    public FileTreeManager(Path workDir, int index) {
        this.workDir = workDir;
        loadFile(index);

    }

    public void loadFile(int index) {
        try {

            List<String> readLines = Files.readAllLines(workDir.resolve(FILE_NAME + index + FILE_EXTENSION));
            currentFileTree = new FileTree(readLines);
            currentIndex = index;
            previousIndex = index - 1;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<FileChange> compare() {
        if (currentIndex == 0) return null;
        List<FileChange> changes = new ArrayList<>();
        List<FileChange> changes2 = new ArrayList<>();

        try {
            List<String> tCurrent = Files.readAllLines(workDir.resolve(FILE_NAME + currentIndex + FILE_EXTENSION));
            List<String> tPrevious = Files.readAllLines(workDir.resolve(FILE_NAME + previousIndex + FILE_EXTENSION));

            List<FileNode> current = new ArrayList<>();
            FileNode test;
            for (String s : tCurrent) {
                test = new FileNode(s);
                if (test.isTrackable()) current.add(test);
            }
            List<FileNode> previous = new ArrayList<>();
            for (String s : tPrevious) {
                test = new FileNode(s);
                if (test.isTrackable()) previous.add(test);
            }

            boolean isTheSame;
            FileNode n1, n2;

            List<FileNode> files = new ArrayList<>();

            for (int i = 0; i < current.size(); i++) {
                n1 = current.get(i);
                isTheSame = false;

                for (int j = 0; j < previous.size(); j++) {
                    n2 = previous.get(j);
                    if (n1.getPath().equals(n2.getPath())) {
                        if (n1.getTime() == n2.getTime()) {
                            isTheSame = true;
                            break;
                        } else {
                            changes.add(new FileChange(n1, ChangeType.MODIFIED));
                            break;
                        }
                    }
                }
                if (!isTheSame) {
                    files.add(n1);
                }
            }
            files.forEach(f -> changes.add(new FileChange(f, ChangeType.CREATED)));
            files.clear();

            for (int i = 0; i < previous.size(); i++) {
                n2 = previous.get(i);
                isTheSame = false;

                for (int j = 0; j < current.size(); j++) {
                    n1 = current.get(j);
                    if (n1.getPath().equals(n2.getPath())) {
                        isTheSame = true;
                        break;
                    }
                }
                if (!isTheSame) {
                    files.add(n2);
                }
            }

            // rekurzív

            List<FileNode> dirs = files.stream().filter(FileNode::isDirectory).collect(Collectors.toList());
            List<FileNode> recursivelyDeleted = new ArrayList<>();

            dirs.sort(Comparator.comparingInt(f -> f.getPath().split("/").length));

            // Previous
            // /c/a/a/a/a/file.txt
            // /a/b/
            // /c/a/
            // /a/
            // /c/

            // dirs
            // /a/
            // /c/
            // /a/b/
            // /c/a/



//            for(FileNode d : dirs) {
//                for(FileNode d2 : previous) {
//                    if(d == d2) continue;
//                    if(!d2.isDirectory()) continue;
//                    if(d2.getPath().matches("^" + d.getPath() + ".*" + d2.getFullName() + "$")) {
//                        recursivelyDeleted.add(d2);
//                    }
//                }
//            }


            String[] recurPath, testPath;
            FileTree previousFileTree = new FileTree(tPrevious);
            boolean pathPartEqual;
            for (FileNode f : dirs) {
                previousFileTree.changeDirectory(f.getPath());

                if (!previousFileTree.getCurrentDirFiles().isEmpty()) {

                    changes.add(new FileChange(f, ChangeType.RECURSIVE_DELETE));
                    recurPath = f.getPath().split("/");

                    for (FileNode file : previous) {
                        // ha az f mappán belül van:
                        testPath = file.getPath().split("/");
                        if (testPath.length >= recurPath.length) {

                            pathPartEqual = true;
                            for (int i = 0; i < recurPath.length; i++) {
                                if (!recurPath[i].equals(testPath[i])) {
                                    pathPartEqual = false;
                                    break;
                                }
                            }
                            if (pathPartEqual) {
                                recursivelyDeleted.add(file);
                            }
                        }
                    }
                }
            }

            boolean isRecurDeleted;
            for (FileNode f : previous) {
                isRecurDeleted = false;
                for (FileNode f2 : recursivelyDeleted) {
                    if (f.getPath().equals(f2.getPath())) {
                        isRecurDeleted = true;
                        break;
                    }
                }
                if (!isRecurDeleted) {
                    changes.add(new FileChange(f, ChangeType.DELETED));
                }

            }

            for (FileChange f : changes) {
                testPath = f.getFile().getPath().split("/");
                if (f.getChangeType() == ChangeType.RECURSIVE_DELETE) {
                    for (FileChange f2 : changes) {
                        if(f == f2) continue;
                        recurPath = f2.getFile().getPath().split("/");
                        if (testPath.length <= recurPath.length) {
                            pathPartEqual = true;
                            for (int i = 0; i < testPath.length; i++) {
                                if (!recurPath[i].equals(testPath[i])) {
                                    pathPartEqual = false;
                                    break;
                                }
                            }
                            if (pathPartEqual) {
                                changes2.add(f2);
                            }
                        }
                    }
                }
            }

            for(FileChange f : changes2) {
                changes.remove(f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


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
