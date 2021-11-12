package com.dusza;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
                String[] split = cmd.split(" ");
                if(split[1].equals("..")) {
                    if(!fileTreeManager.stepBack()) System.out.println("Innen már nem léphetsz vissza!");
                }else if(!fileTreeManager.changeDir(split[1])) {
                    System.out.println("Nincs ilyen mappa vagy a kiválaszott cél nem mappa!");
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
        System.out.println("Folytatáshoz nyomj ENTER-t!");
        input.nextLine();
    }

    private void printChanges() {
        for(FileChange f : fileTreeManager.compare()) {
            System.out.printf("%s %s %d %d\n", f.getChangeType().getType(), f.getFile().getPath(), f.getFile().getSize(), f.getFile().getTime());
        }
    }

    private void printCurrentDir() {
        if(!fileTreeManager.getCurrentFileTree().getCurrentDir().getPath().equals("")) System.out.println("..");

        List<FileNode> dirList = fileTreeManager.getCurrentFileTree().getCurrentDirFiles().stream().filter(FileNode::isDirectory).sorted(Comparator.comparing(FileNode::getName)).collect(Collectors.toList());
        List<FileNode> fileList = fileTreeManager.getCurrentFileTree().getCurrentDirFiles().stream().filter(f -> !f.isDirectory()).sorted(Comparator.comparing(FileNode::getName)).collect(Collectors.toList());

        dirList.forEach(d -> System.out.println(d.getFullName()));
        fileList.forEach(f -> System.out.println(f.getFullName()));
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