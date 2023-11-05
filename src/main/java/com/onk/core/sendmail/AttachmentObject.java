package com.onk.core.sendmail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentObject {

    private String fileName;
    private String contentType;
    private byte[] data;

}
