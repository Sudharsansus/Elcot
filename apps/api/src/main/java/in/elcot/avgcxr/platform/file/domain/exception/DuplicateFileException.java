package in.elcot.avgcxr.platform.file.domain.exception;



public class DuplicateFileException extends RuntimeException {
    public DuplicateFileException(String name) { super("Duplicate file: " + name); }
}
