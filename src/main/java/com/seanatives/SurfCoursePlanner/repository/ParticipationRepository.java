package com.seanatives.SurfCoursePlanner.repository;

import com.seanatives.SurfCoursePlanner.domain.CourseType;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.domain.Participation;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends CrudRepository<Participation, Long> {
    List<Participation> findByGuestAndCourseType(Guest guest, String courseType);

    Optional<Participation> findByGuestIdAndCourseTypeAndDate(Long guestId, String courseType, Date date);

    List<Participation> findByGuestId(Long guestId);
}
