package com.korit.board.entity;

import com.korit.board.dto.BoardListRespDto;
import com.korit.board.dto.GetBoardRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Board {
    private int boardId;
    private String boardTitle;
    private int boardCategoryId;
    private String boardContent;
    private String email;
    private LocalDateTime createDate;
    private String nickname;
    private int boardHitsCount;
    private int boardLikeCount;

    public BoardListRespDto toBoardListDto() {
        return BoardListRespDto.builder()
                .boardId(boardId)
                .title(boardTitle)
                .nickname(nickname)
                // DateTimeFormatter : 년도, 월, 일 까지만 출력
                .createDate(createDate.format(DateTimeFormatter.ISO_DATE))
                .hitsCount(boardHitsCount)
                .likeCount(boardLikeCount)
                .build();
    }

    public GetBoardRespDto toBoardDto() {
        return GetBoardRespDto.builder()
                .boardId(boardId)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardCategoryId(boardCategoryId)
                .email(email)
                .nickname(nickname)
                .createDate(createDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)))
                .boardHitsCount(boardHitsCount)
                .boardLikeCount(boardLikeCount)
                .build();
    }
}
