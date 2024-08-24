package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.VocaContentControllerDocs;
import com.example.vocatest.dto.VocaContentDto;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.service.UserService;
import com.example.vocatest.service.VocaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/vocacontent")
public class VocaContentController implements VocaContentControllerDocs {

    private final VocaService vocaService;
    private final UserService userService;

    public VocaContentController(VocaService vocaService, UserService userService){
        this.vocaService = vocaService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public List<VocaContentEntity> getAllVocasByVocaListId(@PathVariable("id") Long id) { //단어장에 있는 모든 단어를 보여주는 메소드
        List<VocaContentEntity> vocas = vocaService.findAllVocasByVocaListId(id);
        return vocas;
    }

    @GetMapping("/word/{wordid}")// 특정 단어 조회
    public ResponseEntity<VocaContentEntity> showVocaContent(@PathVariable("wordid") Long wordid){
        VocaContentEntity target = vocaService.getVocaContentId(wordid);
        if (target == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(target);
    }


    @PostMapping("/{id}")
    public VocaContentEntity addWord(@PathVariable("id") Long id, @RequestBody VocaContentDto vocaContentDto){ // 단어장에 단어 등록
        VocaListEntity vocaListEntity = vocaService.findVocaListById(id);
        VocaContentEntity vocaContentEntity = vocaContentDto.toEntity(vocaListEntity);
        return vocaService.saveVocaContent(vocaContentEntity);
    }

    @PatchMapping("/{id}/{wordid}")//단어수정
    public ResponseEntity<VocaContentEntity> updateVocaContent(@PathVariable("id")Long id, @PathVariable("wordid") Long wordid, @RequestBody VocaContentDto vocaContentDto){
        VocaContentEntity target = vocaService.getVocaContentId(wordid);
        if (target == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        target.setText(vocaContentDto.getText());
        target.setTranstext(vocaContentDto.getTranstext());
        VocaContentEntity updated = vocaService.saveVocaContent(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}/{wordid}")//단어 삭제
    public ResponseEntity<String> deleteVocaContent(@PathVariable("id")Long id, @PathVariable("wordid")Long wordid){
        VocaContentEntity target = vocaService.getVocaContentId(wordid);
        if(target == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제 실패");
        }
        vocaService.deleteVocaContent(target);
        return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
    }
}
