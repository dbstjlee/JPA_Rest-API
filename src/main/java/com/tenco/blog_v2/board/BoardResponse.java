package com.tenco.blog_v2.board;

import lombok.Getter;
import lombok.Setter;

public class BoardResponse {

    @Getter
    @Setter
    public static class DTO {
        private int id;
        private String title;
        private String content;
        
        // DTO 사용 시 사용자 정의 생성자
        public DTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }

    // 게시글 상세보기 응답 내릴 때

    // 게시글 상세보기  - 댓글 정보

    // 게시글 목록 보기 화면을 위한 DTO 클래스 만들어 보기
    @Getter
    @Setter
    public static class ListDTO {
        private int id;
        private String title;

        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();

        }
    }



}
