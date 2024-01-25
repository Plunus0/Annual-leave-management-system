package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.entity.DetailsLeaveAbsence;
import com.the_daul_intra.mini.dto.request.ApiLeaveSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LeaveSpecifications {
    public static Specification<DetailsLeaveAbsence> withCriteria(ApiLeaveSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("processingStatus"), request.getStatus()));
            }
            if (request.getType() != null) {
                predicates.add(cb.equal(root.get("absenceType"), request.getType()));
            }
            if (request.getRegDate() != null) {
                LocalDateTime regDate = LocalDateTime.parse(request.getRegDate());
                predicates.add(cb.equal(root.get("applicationDate"), regDate));
            }
            if (request.getWriterId() != null) {
                predicates.add(cb.equal(root.get("employee").get("id"), request.getWriterId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<DetailsLeaveAbsence> withAdminCriteria(String absenceType, String status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (absenceType != null && !absenceType.isEmpty()) {
                predicates.add(cb.equal(root.get("absenceType"), absenceType));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("processingStatus"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
