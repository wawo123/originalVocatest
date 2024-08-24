package com.example.vocatest.repository;

import com.example.vocatest.entity.UserVocaListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserVocaListRepository extends JpaRepository<UserVocaListEntity, Long> {
    List<UserVocaListEntity> findByUserEntityEmail(String userId);

    UserVocaListEntity findByVocaListEntityId(Long vocaId);


}
