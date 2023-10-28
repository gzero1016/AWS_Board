package com.korit.board.repository;

import com.korit.board.entity.Board;
import com.korit.board.entity.BoardCategory;
import com.korit.board.entity.UserPoint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    public List<BoardCategory> getBoardCategories();
    public int saveCategory(BoardCategory boardCategory);
    public int saveBoard(Board board);
    public List<Board> getBoardList(Map<String, Object> paramsMap);
    public int getBoardCount(Map<String, Object> paramsMap);
    public Board getBoardByBoardId(int boardId);
    public int getLikeState(Map<String, Object> paramsMap);
    public int insertLikeState(Map<String, Object> paramsMap);
    public int deleteLike(Map<String, Object> paramsMap);
    public int deleteBoard(int boardId);
    public int updateBoard(Board board);
    public int usePoint(UserPoint userPoint);
    public int getHitsState(int boardId);
    public int CheckHitsAndEmail(Map<String, Object> paramsMap);
    public int saveHitsState(Map<String, Object> paramsMap);
}
