package com.korit.board.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetBoardRespDto {
    private int boardId;
    private String boardTitle;
    private int boardCategoryId;
    private String boardContent;
    private String email;
    private String createDate;
    private String nickname;
    private int boardHitsCount;
    private int boardLikeCount;
}
