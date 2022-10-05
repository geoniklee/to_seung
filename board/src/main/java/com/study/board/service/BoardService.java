package com.study.board.service;


import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository; //원래 객체 처리를 위해서 new를 사용해야하지만 Autowired를 통해서 자동으로 지정 가능하다.

    // 글 작성 처리
    public void write(Board board, MultipartFile file, MultipartFile file2) throws Exception    //글 작성시 file을 추가하기 위해서 MultipartFile 추가
    {
        String projectpath = System.getProperty("user.dir")+ "/src/main/resources/static/files";    // user.dir은 프토젝트 경로를 담아주게 된다.
        UUID uuid = UUID.randomUUID(); // 랜덤으로 이름 생성
        String filename = uuid+"_"+file.getOriginalFilename();   //파일 이름은 UUID의 있는 랜덤값 + 원래 파일 이름으로 설정된다.
        File saveFile = new File(projectpath,filename);         // 위에 젃힌 경로에, name으로 저장된다.
        file.transferTo(saveFile);
                           //DB에 파일 경로 저장

//        UUID uuid2 = UUID.randomUUID(); // 랜덤으로 이름 생성
        String filename2 = uuid+"_"+file2.getOriginalFilename();   //파일 이름은 UUID의 있는 랜덤값 + 원래 파일 이름으로 설정된다.
        File saveFile2 = new File(projectpath,filename2);         // 위에 젃힌 경로에, name으로 저장된다.
        file2.transferTo(saveFile2);


        board.setFilename(filename);                            //DB에 파일 이름 저장
        board.setFilepath("/files/"+filename);
        board.setFilename2(filename2);                            //DB에 파일 이름 저장
        board.setFilepath2("/files/"+filename2);                   //DB에 파일 경로 저장

        boardRepository.save(board);
    }

    public void write2(Board board, MultipartFile file, MultipartFile file2, MultipartFile file3) throws Exception    //글 작성시 file을 추가하기 위해서 MultipartFile 추가
    {
        String projectpath = System.getProperty("user.dir")+ "/src/main/resources/static/files";    // user.dir은 프토젝트 경로를 담아주게 된다.
        UUID uuid = UUID.randomUUID(); // 랜덤으로 이름 생성
        String filename = uuid+"_"+file.getOriginalFilename();   //파일 이름은 UUID의 있는 랜덤값 + 원래 파일 이름으로 설정된다.
        File saveFile = new File(projectpath,filename);         // 위에 젃힌 경로에, name으로 저장된다.
        file.transferTo(saveFile);
        //DB에 파일 경로 저장

//        UUID uuid2 = UUID.randomUUID(); // 랜덤으로 이름 생성
        String filename2 = uuid+"_"+file2.getOriginalFilename();   //파일 이름은 UUID의 있는 랜덤값 + 원래 파일 이름으로 설정된다.
        File saveFile2 = new File(projectpath,filename2);         // 위에 젃힌 경로에, name으로 저장된다.
        file2.transferTo(saveFile2);

        String filename3 = uuid+"_"+file2.getOriginalFilename();   //파일 이름은 UUID의 있는 랜덤값 + 원래 파일 이름으로 설정된다.
        File saveFile3 = new File(projectpath,filename2);         // 위에 젃힌 경로에, name으로 저장된다.
        file2.transferTo(saveFile3);


        board.setFilename(filename);                            //DB에 파일 이름 저장
        board.setFilepath("/files/"+filename);
        board.setFilename2(filename2);                            //DB에 파일 이름 저장
        board.setFilepath2("/files/"+filename2);        //DB에 파일 경로 저장
        board.setOutput("/files"+filename3);

        boardRepository.save(board);
    }


    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable)          // Board 타입의 리스트를 사용한다. Board는 Entity에 있는 Board
    {
        return boardRepository.findAll(pageable);    // 해당 테이블에 있는 모든것을 가져온다.
    }

    // 특정 게시글 불러오기
    public Board boardView(Integer id)
    {
        // id 값으로 게시글을 불러온다.
        return boardRepository.findById(id).get();
    }

    public Page<Board> boardList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id)     //매개변수로 id를 받아서 그 값을 삭제하게 된다.
    {
        boardRepository.deleteById(id);     //번호에 맞게 삭제

    }

    public void write1(Board board, MultipartFile file) throws Exception{




        String projectpath = System.getProperty("user.dir")+ "/src/main/resources/static/files";    // user.dir은 프토젝트 경로를 담아주게 된다.

        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectpath, fileName);

        file.transferTo(saveFile);


        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);
        board.setId(board.getId());
        board.setTitle("새롭게");
        board.setContent("와우");
        board.setOutput("/files/" + fileName);

        boardRepository.save(board);
    }
}


