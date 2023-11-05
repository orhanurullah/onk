package com.onk.core.fileSystem;

import com.onk.component.MessageService;
import com.onk.core.results.Result;
import com.onk.core.results.SuccessResult;
import com.onk.core.utils.DbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    private final MessageService messageService;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, MessageService messageService) {
        this.messageService = messageService;
        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException(this.messageService.getMessage("file.name.isEmpty.message", null));
        }
        this.rootLocation = Paths.get(properties.getLocation());
    }


    @Override
    public Result store(MultipartFile file) {
        try{
            if(file.isEmpty()){
                throw new StorageException(messageService.getMessage("file.empty.message", new Object[]{file.getOriginalFilename()}));
            }
            Path destinationFile = rootLocation.resolve(
                    Paths.get(Objects.requireNonNull(file.getOriginalFilename()))
            ).normalize().toAbsolutePath();
            if(!destinationFile.getParent()
                    .equals(rootLocation.toAbsolutePath())){
                throw new StorageException(
                        messageService.getMessage("file.upload.directory.security.control.message", null)
                );
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                return new SuccessResult(
                        messageService.getMessage("file.upload.success.message", null)
                );
            }catch (IOException e){
                throw new StorageException(
                        messageService.getMessage("file.upload.failed.message", null)
                );
            }
        }catch (MaxUploadSizeExceededException e){
            throw new StorageException(
                    messageService.getMessage("file.upload.maxSize.failed.message", null)
            );
        }
    }
    @Override
    public boolean isImageFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        for (String extension : DbConstants.allowedFileExtension) {
            if (fileExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            Stream<Path> pathStream = Files.list(rootLocation);
            return pathStream.onClose(pathStream::close)
                    .filter(Files::isRegularFile)
                    .map(rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException(
                    messageService.getMessage("file.read.failed.message", null)
            );
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                            messageService.getMessage("file.read.resource.failed.message", new Object[]{filename})
                        );

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(
                    messageService.getMessage("file.read.resource.failed.message", new Object[]{filename})
                    , e
            );
        }
    }
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException(
                    messageService.getMessage("file.notCreate.directory.message", null)
                    , e
            );
        }
    }
}
