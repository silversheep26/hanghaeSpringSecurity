package com.sparta.hanghaejwt.controller;

// 전역 예외 처리
import com.sparta.hanghaejwt.dto.*;
import com.sparta.hanghaejwt.security.UserDetailsImpl;
import com.sparta.hanghaejwt.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 게시물 저장
    @PostMapping("/create")  // post 는 body 가 있음
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createBoard(requestDto, userDetails.getUser());
    }

    // 게시물 전체 보기
    @GetMapping("/list")  // get 은 body 가 없다
    public List<BoardAndComment> getBoardList() { // 데이터 베이스에 저장 된 전체 게시물 전부다 가져오는 API
        return boardService.getBoardList();
    }

    // 게시물 하나만 보기
    @GetMapping("/{board_id}")  // http://localhost:8080/board/{id}
    public BoardCommentResponseDto getBoard(@PathVariable Long board_id) {
        return boardService.getBoard(board_id);
    }

    // 게시물 수정  = post + get(게시물하나보기)
    @PutMapping("/update/{board_id}")
    public BoardResponseDto updateBoard(@PathVariable Long board_id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updateBoard(board_id, requestDto, userDetails.getUser());
    }

    // 게시물 삭제
    @DeleteMapping("/delete/{board_id}")
    public MessageStatusResponseDto deleteBoard(@PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(board_id, userDetails.getUser());
    }
}


/*
Request 가 들어오는 타입에 따라 받는 방법은 4가지 정도로 달라진다.
@PathVariable
맨 뒤에 처리해주는 것
 - URL 변수
@RequestParam
 - Query String
@RequestBody
 - Body
@ModelAttribute
 - Form

@AuthenticationPrincipal
인증 객체에 담겨져 있는 UserDetailsImpl 을
파라미터(매개변수)로 요청 받아
그 내용물을 사용할 수 있다.
 */