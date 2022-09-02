package com.example.demo.service.Interfaces;

import com.example.demo.entity.Track;

import java.util.Collection;

public interface TrackService {

    Collection<Track> getAllTracks();

    Collection<Track> getUserRatedTracks(Long id);


}
