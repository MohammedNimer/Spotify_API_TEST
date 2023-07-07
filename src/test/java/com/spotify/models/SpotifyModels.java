package com.spotify.models;

import java.util.List;

public class SpotifyModels {
    private List<String> ids;

    public SpotifyModels (){} //default Method


    public SpotifyModels(List<String> ids){
        this.ids=ids;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

}
