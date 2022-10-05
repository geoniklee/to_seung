package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import com.study.board.service.BoardService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller         // 스프링이 controller임을 알 수 있도록 컨트롤러 어노테이션
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")   ///localhost:8090/board/write
    public String boardWriteForm()
    {
        return "boardwrite";
    }

    @PostMapping("/board/writepro")                       /// 해당 매개변수는 Entity를 통해 가져온다. //file 업로드를 위해서 MultipartFile 추가
    public String boardWritePro(Board board, Model model, MultipartFile file, MultipartFile file2) throws Exception
    {
        boardService.write(board,file, file2);
        model.addAttribute("message","글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");      // /board/list를 통해서 모델에 담겨서 message에 담겨진다.

        return "message";                             ///글 작성 후 메시지 출력
    }

    @PostMapping("/board/receive")
    public String boardReceive(Board board, Model model, MultipartFile file, MultipartFile file2, MultipartFile file3) throws Exception
    {
        boardService.write2(board,file,file2,file3);
        model.addAttribute("message","글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");
        return "message";
    }

    @GetMapping("/board/list")                          // 0 페이지에서 시작하고, id값을 기준으로 역순으로 10개의 글을 보여준다.
    public String boardList(Model model,
                            @PageableDefault(page=0,size=20,sort="id",direction = Sort.Direction.DESC) Pageable pageable,String searchKeyword)
    {

        Page<Board> list = null;

        if (searchKeyword == null)
        {
            list = boardService.boardList(pageable);
        }
        else
        {
            list = boardService.boardList(searchKeyword,pageable);
        }

        int nowPage =pageable.getPageNumber()+1;      ///pageable을 통해서 현재 페이지 번호를 알려준다.
        int startPage = Math.max(nowPage-4,1);
        int endPage =Math.min(nowPage+5,list.getTotalPages());
        // boardService.boardList()를 실행시켰을때 리스트가 반환되는데 "list" 라는 이름으로 반환된다.
        model.addAttribute("list", list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "boardlist";
    }


    @GetMapping("/board/view")      //localhost:8090/board/view?id= * 하면 *이라는 값이 매개변수로 들어간다.
    public String boardView(Model model, Integer id){           // 다시 넘겨줄때는 Model 을 넘겨줘야한다.
        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")    //위와 마찬가지로 ?id= * 하면 *의 값이 삭제되게 된다.
    public String boardDelete(Integer id)
    {
        boardService.boardDelete(id);       //삭제하고 다시 리스트화면으로 보내줘야한다.
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model)  //PathVariable을 통해 URL을 통해 {id}를 인식하여 두번째 매개변수 안 id로 들어간다.
    {
        model.addAttribute("board",boardService.boardView(id));     //boardService.id를 넘겨주는 이유는 상세페이지에 있는 내용과 수정 하는 내용이 같기 때문이다.
        model.addAttribute("message","글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");      // /board/list를 통해서 모델에 담겨서 message에 담겨진다.
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")                          //수정을 위해 게시글 제목과 내용을 불러와야하기 때문에 BOARD를 가져온다.
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file, MultipartFile file2) throws Exception{

        Board boardTemp = boardService.boardView(id);           //Board 객체 boardTemp 생성
        boardTemp.setTitle(board.getTitle());                   //Board 제목 가져오기 (덮어 쓰기)
        boardTemp.setContent(board.getContent());               //Board 내용 가져오기 (덮어 쓰기)
        model.addAttribute("message","글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");      // /board/list를 통해서 모델에 담겨서 message에 담겨진다.
        boardService.write(boardTemp, file, file2);                      //BoardService에 있는 write를 이용하여 수정
        return "message";
    }

    @GetMapping("/picture/{id}")
    public  String pictureView(@PathVariable("id") Integer id, Model model){

        model.addAttribute("board", boardService.boardView(id));
        return "painter";
    }



    @Autowired
    private BoardRepository boardRepository;

    @Getter @Setter
    public class HelloData {
//        private String username;
//        private int age;
        private String abc;
    }

    @GetMapping("/board/willget")                       /// 해당 매개변수는 Entity를 통해 가져온다. //file 업로드를 위해서 MultipartFile 추가
    public String willget(Board board, MultipartFile file, Text text) throws Exception {
        System.out.println(text);
        출처: https://commin.tistory.com/8 [Commin의 일상코딩:티스토리]
        boardService.write1(board,file);
        return "boardview";                             ///글 작성 후 메시지 출력
    }



    @GetMapping("/picture1")
    public  String pictureView1(){


        return "index1";
    }

    @GetMapping("/picture3")
    public String pictureView3(){

        return "index3";
    }



    @GetMapping("/main")
    public String style_Main(){

        return "main";
    }
}

