package com.usyd.group08.elec5619.rest;

import com.usyd.group08.elec5619.aop.ValidateUserType;
import com.usyd.group08.elec5619.models.User;
import com.usyd.group08.elec5619.models.Venue;
import com.usyd.group08.elec5619.repositries.VenueRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/venues")
public class VenueController {
    @Autowired
    VenueRepository venueRepository;

    @Autowired
    HttpSession httpSession;



    /**
     * Find all Venue
     *
     * @return List of all venues
     */
    @GetMapping
    @Operation(summary = "Find all venues", description = "Returns a list of all venues")
    public List<Venue> getVenues() {
        return venueRepository.findAll();
    }



    /**
     * Create Stall Venue
     *
     * @param addVenueWrapper
     * @return
     */
    @PostMapping
    @ValidateUserType(type = "admin,organiser") // 允许 admin 和 organiser
    @Operation(summary = "Add a new venue", description = "Pass in a venue without venueID")
    public Venue addVenue(@RequestBody VenueWrapper addVenueWrapper) {
        User user = (User) httpSession.getAttribute("currentUser");
        Venue venue = new Venue();
        venue.setState(addVenueWrapper.getState());
        venue.setStreet(addVenueWrapper.getStreet());
        venue.setSuburb(addVenueWrapper.getSuburb());
        venue.setDescription(addVenueWrapper.getDescription());
        venue.setPicture(addVenueWrapper.getPicture());
        venue.setLongitude(addVenueWrapper.getLongitude());
        venue.setLatitude(addVenueWrapper.getLatitude());

        venue.setUser(user);
        return venueRepository.save(venue);
    }

    /**
     * Delete venue
     *
     * @param venueID
     * @return
     */
    @DeleteMapping
    @ValidateUserType(type = "admin,organiser") // 只允许登陆的用户中的： admin 和 organiser执行此次操作
    @Operation(summary = "Delete venue by venueID", description = "Pass in venueID, and will return true(delete success) or false(delete not success)")
    public boolean deleteVenue(@RequestParam String venueID) {
        User user = (User) httpSession.getAttribute("currentUser");
        Optional<Venue> optionalVenue = venueRepository.findById(Integer.parseInt(venueID));
        if (optionalVenue.isPresent()) {
            Venue venue = optionalVenue.get();
            //判断当前得到的VenueID 是否是属于当前的organiser的，如果是的话删除不是的话返还false, admin可以删除所有的venue
            if (venue.getUser().getId().equals(user.getId()) || user.getType().equals("admin")) {
                venueRepository.delete(venue);
                return true;
            }
        }
        return false;
    }

    /**
     * Update venue information
     *
     * @param venueWrapper
     * @return
     */
    @PutMapping
    @ValidateUserType(type = "admin,organiser") // 只允许登陆的用户中的： admin 和 organiser执行此次操作
    @Operation(summary = "Update venue info", description = "Pass the updated venue object, and will return the updated venue object.")
    public Venue updateVenue(@RequestBody VenueWrapper venueWrapper) {
        User user = (User) httpSession.getAttribute("currentUser");
        Optional<Venue> optionalVenue = venueRepository.findById(Integer.valueOf(venueWrapper.getVenueId()));
        if (optionalVenue.isPresent()) {
            Venue venue = optionalVenue.get();
            if (venue.getUser().getId().equals(user.getId()) || user.getType().equals("admin")) {
                venue.setStreet(venueWrapper.getStreet());
                venue.setSuburb(venueWrapper.getSuburb());
                venue.setState(venueWrapper.getState());
                venue.setDescription(venueWrapper.getDescription());
                venue.setPicture(venueWrapper.getPicture());
                venue.setLongitude(venueWrapper.getLongitude());
                venue.setLatitude(venueWrapper.getLatitude());
                return venueRepository.save(venue);
            }
        }
        return null;
    }


    //自制一个只有我需要的  venue信息  的class
    private static class VenueWrapper {
        private int venueId;
        private String street;
        private String suburb;
        private String state;
        private String description;
        private String picture;
        private double longitude;
        private double latitude;

        public int getVenueId() {
            return venueId;
        }

        public void setVenueId(int venueId) {
            this.venueId = venueId;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getSuburb() {
            return suburb;
        }

        public void setSuburb(String suburb) {
            this.suburb = suburb;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }


}
