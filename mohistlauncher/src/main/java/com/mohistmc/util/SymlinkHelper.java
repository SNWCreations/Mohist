package com.mohistmc.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Mohist+ - Resolve symbolic links in Forge installer code and libraries checker
/**
 * @author SNWCreations
 */
public class SymlinkHelper {
    public static String resolveLink(String path) {
        try {
            final Path asPath = Paths.get(path);
            final Path parent = asPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
                return parent.toRealPath().resolve(asPath.getFileName()).toString();
            }
            return asPath.toRealPath().toString();
        } catch (IOException e) {
            return path;
        }
    }

    public static File resolveLink(File file) {
        try {
            final Path asPath = file.toPath();
            final Path parent = asPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
                return parent.toRealPath().resolve(asPath.getFileName()).toFile();
            }
            return asPath.toRealPath().toFile();
        } catch (IOException e) {
            return file;
        }
    }
}
