package com.dusza;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.dusza.FileTreeManager.FILE_REGEX;

public class CLI {
    private final Path workDir;

    public CLI(Path workDir) {
        this.workDir = workDir;
    }

    public void start() {

    }

    private void printFiles() {
        for(String s : readFiles()) {
            //System.out.printf("%d");
        }
    }

    private List<String> readFiles() {
        List<String> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(workDir, entry -> entry.getFileName().toString().matches(FILE_REGEX) && !Files.isDirectory(entry))) {
            for (Path p : stream) {
                result.add(p.getFileName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}