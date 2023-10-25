package com.korit.board.service;

import com.korit.board.dto.BoardCategoryRespDto;
import com.korit.board.dto.WriteBoardReqDto;
import com.korit.board.entity.Board;
import com.korit.board.entity.BoardCategory;
import com.korit.board.repository.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(rollbackFor = Exception.class)
    public boolean writeBoardContent(WriteBoardReqDto writeBoardReqDto) {
        BoardCategory boardCategory = null;

        // db에 없는 카테고리 Id이면 boardCategory Entity에 빌더를 한후 Mapper에게 넘겨줌
        if(writeBoardReqDto.getCategoryId() == 0) {
            boardCategory = BoardCategory.builder()
                    .boardCategoryName(writeBoardReqDto.getCategoryName())
                    .build();

            boardMapper.saveCategory(boardCategory); // 새로운 카테고리 추가
            // DB에 추가한 후 생성된 categoryId를 dto에 추가해줌
            writeBoardReqDto.setCategoryId(boardCategory.getBoardCategoryId());
        }
        // 토큰에 있는 사용자 email 가져옴
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);
        Board board = writeBoardReqDto.toBoardEntity(email);

        // 게시글을 DB에 저장
        return boardMapper.saveBoard(board) > 0;
    }
}
