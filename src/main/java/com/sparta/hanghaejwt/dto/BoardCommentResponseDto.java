package com.sparta.hanghaejwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
@Getter
@NoArgsConstructor
public class BoardCommentResponseDto {

    private BoardResponseDto board;
    private List<CommentResponseDto> comments = new ArrayList();

    public BoardCommentResponseDto(BoardResponseDto board, List<CommentResponseDto> comments) {
        this.board = board;
        this.comments = comments;
    }
}
