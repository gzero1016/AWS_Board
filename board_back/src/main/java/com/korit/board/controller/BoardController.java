package com.korit.board.controller;

import com.korit.board.aop.annotation.ArgsAop;
import com.korit.board.aop.annotation.ValidAop;
import com.korit.board.dto.WriteBoardReqDto;
import com.korit.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> writeBoard(@Valid  @RequestBody WriteBoardReqDto writeBoardReqDto,
                                        BindingResult bindingResult){
        return ResponseEntity.ok(boardService.writeBoardContent(writeBoardReqDto));
    }
}
