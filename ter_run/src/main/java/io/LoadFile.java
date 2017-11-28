package io;

import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Search and import data from an input folder.
 */
@Immutable
public final class LoadFile {

    private final String root;

    public LoadFile(String root) {
        this.root = root;
    }

    /**
     * Import all files from the specified folder.
     * @param folder Folder to import data from.
     * @return List of file contained in the folder and subfolder.
     */
    private List<File> all(String folder) {
        try {
            return Files.walk(Paths.get(folder))
                    .filter(Files::isRegularFile)
                    .sorted()
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Import all files from the input/input_relaxation/
     * folder and subfolder.
     * @return List of file contained in the folder and subfolder.
     */
    public List<File> all() {
        return all(this.root);
    }

    /**
     * Recursively search in input/input_relaxation folder
     * and return a list containing the first file matching the file name
     * or return  an empty list if no matching file name ar found.
     * @param filename File name to search.
     * @return List of one file.
     */
    private List<File> searchFile(String filename) {
        if (filename == null) throw new NullPointerException();
        try {
            Optional<File> first = Files.walk(Paths.get(this.root))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(e -> e.getName().equals(filename))
                    .findFirst();
            List<File> res = new LinkedList<>();
            first.ifPresent(res::add);
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recursively search in input/input_relaxation folder
     * and return all the files contained in the first matching folder name
     * or return an empty list if no matching folder name ar found.
     * @param foldername Folder name to search.
     * @return List of files contained in the folder or subfolder..
     */
    private List<File> searchFolder(String foldername) {
        if (foldername == null) throw new NullPointerException();
        try {
            Optional<File> folder = Files.walk(Paths.get(this.root))
                    .filter(Files::isDirectory)
                    .map(Path::toFile)
                    .filter(e -> e.getName().equals(foldername))
                    .findFirst();
            return folder.map(file -> all(file.getAbsolutePath())).orElseGet(LinkedList::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Import one file or all files in a folder from it's name.
     * If you add a folder it will add all files in this folder and subfolder.
     *
     * In case the input name contains no absolute or relative path
     * a recursive search will occur in the input/input_relaxation/ folder.
     * This recursive search will stop on the first matching file name found.
     * If no file are found, it will stop on the first matching folder name found.
     * If no file or folder name matches, it will throw and error.
     *
     * @param strFiles File or folder name to load. If the file is in one of the
     *                 input/input_relaxation folder or subfolder the path is optional.
     * @return List of file (only one file in the list if the input is a file name).
     */
    public List<File> fromStrings(List<String> strFiles) {
        List<File> res = new LinkedList<>();

        for (String strFile : strFiles) {
            File file = new File(strFile);
            if (file.exists()) {
                if (file.isDirectory()) {
                    res.addAll(all(strFile));
                } else if (file.isFile()) {
                    res.add(file);
                }
            } else {
                List<File> sFiles = searchFile(strFile);
                List<File> sFolder = searchFolder(strFile);
                if (sFiles.isEmpty() && sFolder.isEmpty()) {
                    throw new RuntimeException("File or folder " + strFile + " doesn't exist");
                } else {
                    if (! sFiles.isEmpty()){
                        res.addAll(sFiles);
                    } else {
                        res.addAll(sFolder);

                    }
                }
            }
        }

        return res;
    }
}
