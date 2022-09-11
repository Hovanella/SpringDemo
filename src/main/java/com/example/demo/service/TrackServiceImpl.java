package com.example.demo.service;

import com.example.demo.dto.RatingDto;
import com.example.demo.dto.RatingWithNewMark;
import com.example.demo.dto.TrackDto;
import com.example.demo.entity.Author;
import com.example.demo.entity.Genre;
import com.example.demo.entity.Rating;
import com.example.demo.entity.Track;
import com.example.demo.repository.RatingRepository;
import com.example.demo.repository.TrackRepository;
import com.example.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class TrackServiceImpl {

    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TrackServiceImpl(TrackRepository trackRepository, UserRepository userRepository, RatingRepository ratingRepository, ModelMapper modelMapper) {
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.modelMapper = modelMapper;
    }

    public Collection<TrackDto> getAllTracksForCurrentUser(Long id) {

        var currentUser = userRepository.findById(id).orElse(null);

        if (currentUser == null)
            throw new IllegalArgumentException("User with id " + id + " not found");

        var allTracks = new ArrayList<TrackDto>();
        trackRepository.findAll().forEach(track -> allTracks.add(modelMapper.map(track, TrackDto.class)));

        var userRatings = new ArrayList<RatingDto>();
        ratingRepository.findByUser(currentUser).forEach(rating -> userRatings.add(modelMapper.map(rating, RatingDto.class)));

        for (var track : allTracks) {
            for (var rating : userRatings) {
                if (track.getId().equals(rating.getTrack().getId())) {
                    track.setRating(rating.getMark());
                }
            }
        }

        return allTracks;
    }


    public TrackDto addOrUpdateTrackUserRating(Long trackId, int rating, Long id) {

        var user = userRepository.findById(id).orElse(null);
        if (user == null)
            throw new IllegalArgumentException("User with id " + id + " not found");

        var track = trackRepository.findById(trackId).orElse(null);
        if (track == null)
            throw new IllegalArgumentException("Track with id " + trackId + " not found");

        var ratingToUpdate = ratingRepository.findByUserAndTrack(user, track);

        if (ratingToUpdate == null) {
            var newRating = new RatingWithNewMark(rating, user, track);
            ratingRepository.save(modelMapper.map(newRating, Rating.class));
        } else {
            ratingToUpdate.setMark(rating);
            ratingRepository.save(ratingToUpdate);
        }

        var trackDto = modelMapper.map(track, TrackDto.class);
        trackDto.setRating(rating);
        return trackDto;
    }

    public TrackDto createTrack(String name, String path, Author author, Genre genre) {
        var track = new Track();
        track.setName(name);
        track.setPath(path);
        track.setAuthor(author);
        track.setGenre(genre);
        trackRepository.save(track);

        return modelMapper.map(track, TrackDto.class);
    }
}
