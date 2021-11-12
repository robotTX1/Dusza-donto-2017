package com.dusza;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        Path workDir = FileSystems.getDefault().getPath("Data");
	    CLI cli = new CLI(workDir);
        cli.start();
    }
}
