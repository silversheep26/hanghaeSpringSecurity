package com.sparta.hanghaejwt.entity;
// 이 정보대로 데이터 베이스에 저장 된다.


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.hanghaejwt.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// @NoArgsConstructor : 기본 생성자를 생성
// @NoArgsConstructor(force = true) 사용시 기본 값으로 초기화
@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String title;

    @JsonIgnore
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;



    // Board 의 파라미터가 있는 생성자 (리턴 타입이 없다)
    // User 의 username, password 를 받아오기 위해 사용
    public Board(BoardRequestDto requestDto, User user){
        this.contents = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.user = user;
    }

    // Board 의 메소드
    // 게시글 업데이트시 내용과 제목 변경한 것이 저장 될 수 있도록
    public void update(BoardRequestDto requestDto) {
        this.contents = requestDto.getContent();
        this.title = requestDto.getTitle();
    }
}

//    @JsonIgnore  // 컨트롤러에서 리턴할 때 이 부분만 빼고 보내준다.
//    // 또한 데이터 받아올 때에도 빼고 받아온다. -> 그래서 Dto 에도 password 필드 넣을 것