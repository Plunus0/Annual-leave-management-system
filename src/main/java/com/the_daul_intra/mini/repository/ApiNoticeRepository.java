package com.the_daul_intra.mini.repository;

import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.dto.entity.NoticeReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiNoticeRepository extends JpaRepository<Notice, Long> {
}
