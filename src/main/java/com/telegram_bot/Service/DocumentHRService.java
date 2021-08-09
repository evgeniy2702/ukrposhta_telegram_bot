package com.telegram_bot.Service;

import com.telegram_bot.model.DocumentHR;
import com.telegram_bot.repositories.DocumentHRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentHRService {

    private DocumentHRRepository documentHRRepository;

    @Autowired
    public void setDocumentHRRepository(DocumentHRRepository documentHRRepository) {
        this.documentHRRepository = documentHRRepository;
    }

    public List<DocumentHR> getList() {
        return documentHRRepository.findAll();
    }
}
