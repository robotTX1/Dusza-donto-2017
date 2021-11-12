package com.dusza;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.dusza.FileTreeManager.FILE_REGEX;

public class CLI {
    private final Path workDir;
    private final Scanner input = new Scanner(System.in);
    private FileTreeManager fileTreeManager;

    public CLI(Path workDir) {
        this.workDir = workDir;
    }

    public void start() {
        System.out.println("Üdv a Dusza backup alkalmazásban!\nVálasz a beolvasandó verziók közül!\n");
        List<String> list = readFiles();
        printFiles(list);


        String cmd;
        int cmdInt;
        while(true) {
            cmd = input.nextLine().trim();

            if(!cmd.matches("\\d+")) System.out.println("Érvénytelen bemenet: '" + cmd + "'");
            cmdInt = Integer.parseInt(cmd);

            if(cmdInt >= 1 && cmdInt <= list.size()) {
                fileTreeManager = new FileTreeManager(workDir, cmdInt - 1);
                browseTree();
                break;
            }
            else {
                if(cmdInt == list.size()+1) break;
                System.out.println("Érvénytelen opció: " + cmdInt);
            }

        }

        System.out.println("Köszönöm, hogy használtad a programot!");
    }

    private void browseTree() {
        String cmd;

        while(true) {
            printCurrentDir();
            cmd = input.nextLine().trim();

            if(cmd.equals("help")) {
                help();
                continue;
            }
            if(cmd.matches("^cd .+$")) {
                if(!fileTreeManager.changeDir(cmd)) {
                    System.out.println("Nincs ilyen mappa!");
                    continue;
                }
            }
            if(cmd.equals("change")) {
                return;
            }


        }
    }

    private void help() {
        System.out.println("help\t-\tSegítség kiírása.");
        System.out.println("cd mappaNév\t-\tMappa váltás.");
        System.out.println("change\t-\tverzió váltás.");
    }

    private void printChanges() {
        for(FileChange f : fileTreeManager.compare()) {
            System.out.printf("%s %s %d %d\n", f.getChangeType().getType(), f.getFile().getPath(), f.getFile().getSize(), f.getFile().getTime());
        }
    }

    private void printCurrentDir() {
        for(FileNode f : fileTreeManager.getFiles()) {
            System.out.println(f.getFullName());
        }
    }

    private void printFiles(List<String> list) {
        for(int i=0; i<list.size(); i++) {
            System.out.printf("%d. %s\n", i+1, list.get(i));
        }
        System.out.println(list.size()+1 + ". Vissza");
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