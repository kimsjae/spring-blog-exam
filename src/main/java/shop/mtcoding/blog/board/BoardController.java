package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;

    @GetMapping({ "/", "/board" })
    public String index(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        List<Board> boardList = boardRepository.findAll(page);

        int currentPage = page;
        int nextPage = currentPage + 1;
        int prevPage = currentPage - 1;

        boolean first = currentPage == 0 ? true : false;

        int totalCount = boardRepository.countBoardId();
        int paging = 5;
        int remainCount = totalCount % paging;
        int totalPage;

        if (remainCount == 0) {
            totalPage = totalCount / paging;
        } else {
            totalPage = totalCount / paging+1;
        }

        boolean last = currentPage+1 == totalPage ? true : false;

        int[] selectPage;

        if (remainCount == 0) {
            selectPage = new int[totalCount/paging];
        } else {
            selectPage = new int[totalCount/paging+1];
        }

        for (int i = 0; i < totalPage; i++) {
            selectPage[i] = i;
        }

        Map<String, Object> key = new HashMap<>();
        key.put("boardList", boardList);
        key.put("nextPage", nextPage);
        key.put("prevPage", prevPage);
        key.put("first", first);
        key.put("last", last);
        key.put("selectPage", selectPage);

        request.setAttribute("key", key);

//        request.setAttribute("boardList", boardList);
//        request.setAttribute("nextPage", nextPage);
//        request.setAttribute("prevPage", prevPage);
//        request.setAttribute("first", first);
//        request.setAttribute("last", last);
//        request.setAttribute("selectPage", selectPage);

        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request) {
        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);
        return "board/updateForm";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO updateDTO){
        boardRepository.update(updateDTO, id);
        return "redirect:/";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO saveDTO, HttpServletRequest request){
        if (saveDTO.getTitle().length() > 20 || saveDTO.getContent().length() > 20) {
            request.setAttribute("status", 400);
            request.setAttribute("msg", "제목과 내용은 20자 이내로 작성하세요.");
            return "error/40x";
        }

        boardRepository.save(saveDTO);
        return "redirect:/";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id){
        boardRepository.delete(id);
        return "redirect:/";
    }
}
