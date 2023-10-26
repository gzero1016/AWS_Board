package com.korit.board.controller;

import com.korit.board.aop.annotation.ArgsAop;
import com.korit.board.aop.annotation.ValidAop;
import com.korit.board.dto.SearchBoardListReqDto;
import com.korit.board.dto.WriteBoardReqDto;
import com.korit.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(boardService.getBoardCategoriesAll());
    }

    @ArgsAop // 자동 print 찍어주는거
    @ValidAop // 널값확인
    @PostMapping("/board/content")
    public ResponseEntity<?> writeBoard(@Valid  @RequestBody WriteBoardReqDto writeBoardReqDto, BindingResult bindingResult){
        return ResponseEntity.ok(boardService.writeBoardContent(writeBoardReqDto));
    }

    // PathVariable: 값추출
    @ArgsAop
    @GetMapping("/boards/{categoryName}/{page}")
    public ResponseEntity<?> getBoardList(@PathVariable String categoryName, @PathVariable int page, SearchBoardListReqDto searchBoardListReqDto) {
        return ResponseEntity.ok(boardService.getBoardList(categoryName, page, searchBoardListReqDto));
    }

    // PathVariable: 값추출
    @GetMapping("/boards/{categoryName}/count")
    public ResponseEntity<?> getBoardCount(@PathVariable String categoryName, SearchBoardListReqDto searchBoardListReqDto) {
        return ResponseEntity.ok(boardService.getBoardCount(categoryName, searchBoardListReqDto));
    }

    // PathVariable: 값추출
    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable int boardId){
        return ResponseEntity.ok(boardService.getBoardByBoardId(boardId));
    }

    @GetMapping("/board/like/{boardId}")
    public ResponseEntity<?> getLikeState(@PathVariable int boardId) {

        return ResponseEntity.ok(boardService.getLikeState(boardId));
    }

    @PostMapping("/board/like/{boardId}")
    public ResponseEntity<?> setLikeState(@PathVariable int boardId) {

        return ResponseEntity.ok(boardService.setlLike(boardId));
    }

    @DeleteMapping("/board/like/{boardId}")
    public ResponseEntity<?> cancelLike(@PathVariable int boardId) {

        return ResponseEntity.ok(boardService.cancelLike(boardId));
    }

}
