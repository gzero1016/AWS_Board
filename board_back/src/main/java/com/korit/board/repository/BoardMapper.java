package com.korit.board.repository;

import com.korit.board.entity.BoardCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    public List<BoardCategory> getBoardCategories();
}
