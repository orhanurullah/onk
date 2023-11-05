package com.onk.core.fileSystem;

import com.onk.core.results.Result;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    Result store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String fileName);

    Resource loadAsResource(String fileName);

    boolean isImageFile(MultipartFile file);

    void deleteAll();
}
