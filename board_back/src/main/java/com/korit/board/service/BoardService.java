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
        // boardCategoryRespDtos List를 생성
        List<BoardCategoryRespDto> boardCategoryRespDtos = new ArrayList<>();

        // Mapper를 통해 DB에서 가져온 category를 하나씩 꺼내서 boardCategoryRespDtos List에 추가함
        boardMapper.getBoardCategories().forEach(category -> {
            boardCategoryRespDtos.add(category.toCategoryDto());
        });

        // 추가가된 boardCategoryRespDtos List를 반환해줌
        return boardCategoryRespDtos;
    }
}
