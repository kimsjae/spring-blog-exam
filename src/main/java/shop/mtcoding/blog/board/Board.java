package shop.mtcoding.blog.board;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "board_tb")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 20)
    private String title;
    private String content;
}
