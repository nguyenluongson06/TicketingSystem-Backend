package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.dto.CategoryDTO;
import com.java2.ticketingsystembackend.mapper.EventMapper;
import com.java2.ticketingsystembackend.repository.CategoryRepository;
import com.java2.ticketingsystembackend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventRepository eventRepository;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    CategoryDTO dto = new CategoryDTO();
                    dto.setId(category.getId());
                    dto.setName(category.getName());
                    return dto;
                }).toList();
    }
}
