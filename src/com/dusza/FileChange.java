package com.dusza;

public class FileChange {
    private final FileNode file;
    private final ChangeType changeType;

    public FileChange(FileNode file, ChangeType changeType) {
        this.file = file;
        this.changeType = changeType;
    }

    public FileNode getFile() {
        return file;
    }

    public ChangeType getChangeType() {
        return changeType;
    }
}
