package com.onk.core.sendmail;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailObject {

    @NotNull
    private String from;
    @NotEmpty
    private Set<String> to;
    @NotEmpty
    private Set<String> cc;
    @NotEmpty
    private Set<String> bcc;
    @NotNull
    private String subject;
    private String body;
    private List<AttachmentObject> attachments;

}
