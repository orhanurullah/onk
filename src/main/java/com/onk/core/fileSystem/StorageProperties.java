package com.onk.core.fileSystem;

import com.onk.component.MessageService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class StorageProperties {

    private final MessageService messageService;

    private String location;

    public StorageProperties(MessageService messageService) {
        this.messageService = messageService;
        location = messageService.getMessage("file.upload.directory", null);
    }
}
