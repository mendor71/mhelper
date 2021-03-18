package ru.pack.csps.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.DocumentException;
import org.json.simple.JSONAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pack.csps.entity.DealsDocuments;
import ru.pack.csps.entity.UserDocuments;
import ru.pack.csps.entity.Users;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.service.dao.DocumentsService;
import ru.pack.csps.util.IncludeAPI;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static ru.pack.csps.util.JSONResponse.createERRResponse;
import static ru.pack.csps.util.JSONResponse.createOKResponse;

@RestController
@RequestMapping(value = "/documents")
public class DocumentsController {
    private DocumentsService documentsService;

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/users/personal_data_agreement", method = RequestMethod.POST)
    public JSONAware prepareUserDocument(@RequestBody Users users, Authentication authentication) throws PropertyFindException, NoSuchAlgorithmException, InvalidKeyException, InvalidValueException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException, IllegalBlockSizeException {
        try {
            UserDocuments userDocuments = documentsService.createUserDocument(users, "personal_data_agreement", authentication.getPrincipal().toString());
            return createOKResponse("Документ создан", userDocuments);
        } catch (IllegalStateException e) {
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/deals/{dealId}")
    public JSONAware prepareDealDocument(@PathVariable Long dealId, @RequestParam(value = "dtName") String dtName, Authentication authentication) throws InvalidValueException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, PropertyFindException, JsonProcessingException {
        try {
            DealsDocuments dealDocument = documentsService.createDealDocument(dealId, dtName, authentication.getPrincipal().toString());
            return createOKResponse("Документ создан", dealDocument);
        } catch (IllegalStateException e) {
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public JSONAware prepareUserDocument(@PathVariable Long userId, @RequestParam(value = "dtName") String dtName, Authentication authentication) throws InvalidValueException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, PropertyFindException, JsonProcessingException {
        try {
            UserDocuments userDocuments = documentsService.createUserDocument(userId, dtName, authentication.getPrincipal().toString());
            return createOKResponse("Документ создан", userDocuments);
        } catch (IllegalStateException e) {
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/user_documents/{userDocId}", method = RequestMethod.POST)
    public JSONAware confirmUserDocument(@PathVariable Long userDocId, @RequestParam(value = "code") String code, Authentication authentication) {
        try {
            documentsService.confirmUserDocument(userDocId, code, authentication.getPrincipal().toString());
            return createOKResponse("Документ подтвержден");
        } catch (IllegalStateException | AccessDeniedException | InvalidValueException e) {
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/deal_documents/{dealDocId}", method = RequestMethod.POST)
    public JSONAware confirmDealDocument(@PathVariable Long dealDocId, @RequestParam(value = "code") String code, Authentication authentication) {
        try {
            documentsService.confirmDealDocument(dealDocId, code, authentication.getPrincipal().toString());
            return createOKResponse("Документ подтвержден");
        } catch (IllegalStateException | AccessDeniedException | InvalidValueException e) {
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/user_documents/{userDocId}", method = RequestMethod.GET)
    public void printUserDocument(@PathVariable Long userDocId, Authentication authentication, HttpServletResponse response) throws InvalidValueException, PropertyFindException, DocumentException, IOException {
        documentsService.printUserDocument(userDocId, authentication.getPrincipal().toString(), response);
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/deal_documents/{dealDocId}", method = RequestMethod.GET)
    public void printDealDocument(@PathVariable Long dealDocId, Authentication authentication, HttpServletResponse response) throws InvalidValueException, PropertyFindException, DocumentException, IOException {
        documentsService.printDealDocument(dealDocId, authentication.getPrincipal().toString(), response);
    }

    @Autowired
    public void setDocumentsService(DocumentsService documentsService) {
        this.documentsService = documentsService;
    }
}
