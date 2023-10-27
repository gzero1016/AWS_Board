package com.korit.board.dto;

import com.korit.board.entity.Board;
import lombok.Data;

@Data
public class UpdateBoardReqDto {
    private String title;
    private String content;
    private int categoryId;
    private int boardId;

    public Board toBoardEntity() {
        return Board.builder()
                .boardTitle(title)
                .boardContent(content)
                .boardCategoryId(categoryId)
                .boardId(boardId)
                .build();
    }
}
