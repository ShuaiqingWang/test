package com.usyd.group08.ELEC5619.repositries;

import com.usyd.group08.ELEC5619.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends  JpaRepository <Booking,String>{
}