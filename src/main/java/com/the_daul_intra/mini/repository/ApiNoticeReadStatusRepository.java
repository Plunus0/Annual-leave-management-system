package com.the_daul_intra.mini.repository;

import com.the_daul_intra.mini.dto.entity.NoticeReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiNoticeReadStatusRepository extends JpaRepository<NoticeReadStatus, Long> {
    Optional<NoticeReadStatus> findByNoticeIdAndEmployeeId(Long noticeId, Long employeeId);
}
