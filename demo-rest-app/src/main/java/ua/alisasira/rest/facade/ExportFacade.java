package ua.alisasira.rest.facade;

import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;

public interface ExportFacade {

    @Transactional
    StringWriter exportAccountTransaction(String accountNumber) throws IOException;
}