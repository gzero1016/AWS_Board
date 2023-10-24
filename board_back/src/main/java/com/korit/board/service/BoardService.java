package com.korit.board.service;

import com.korit.board.dto.BoardCategoryRespDto;
import com.korit.board.repository.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    public List<BoardCategoryRespDto> getBoardCategoriesAll() {
        List<BoardCategoryRespDto> boardCategoryRespDtos = new ArrayList<>();

        boardMapper.getBoardCategories().forEach(category -> {
            boardCategoryRespDtos.add(category.toCategoryDto());
        });

        return boardCategoryRespDtos;
    }
}
