package ua.alisasira.rest.controller;

import ua.alisasira.rest.facade.ExportFacade;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private ExportFacade exportFacade;

    @RequestMapping(value = "/{number}", method = RequestMethod.GET)
    public String exportAccountReport(@PathVariable String number, HttpServletResponse response) throws IOException {
        StringWriter writer = exportFacade.exportAccountTransaction(number);

        try (OutputStream out = response.getOutputStream(); InputStream in = new ByteArrayInputStream(writer.toString().getBytes())) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("text/csv");
            response.setHeader("Content-Length", String.valueOf(in.available()));
            IOUtils.copy(in, out);
        } catch (Exception ioe) {
            ioe.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return null;
    }
}