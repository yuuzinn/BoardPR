package com.example.boardpr.repository;

import com.example.boardpr.domain.Board;
import com.example.boardpr.service.BoardService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("Test JPA")
    void testJpa() {
        Board b1 = new Board();
        b1.setTitle("첫번째 제목");
        b1.setContent("첫번째 글에는 무엇을 작성해볼까");
        b1.setCreateDate(LocalDateTime.now());
        boardRepository.save(b1);


        Board b2 = new Board();
        b2.setTitle("두번째 제목");
        b2.setContent("두번째 글에는 무엇을 작성해볼까");
        b2.setCreateDate(LocalDateTime.now());
        boardRepository.save(b2);
    }

    @Test
    @DisplayName("페이징 처리할 데이터 넣어두기")
    void pagingData() {
        for (int i = 0; i < 300; i++) {
            String title = String.format("This is test data : [%03d]", i);
            String content = "내용은 글쎄요.. 없는 거 같은데 일단 써 보죠";
            this.boardService.create(title, content);
        }
    }
}