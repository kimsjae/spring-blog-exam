package shop.mtcoding.blog.board;

import lombok.Data;

public class BoardResponse {
    @Data
    public static class UpdateDTO {
        private String title;
        private String content;
    }
}
