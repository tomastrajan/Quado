package com.trajan.android.game.Quado.rest;

import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.model.GameMode;
import com.trajan.android.game.Quado.rest.dto.ScoreDto;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

import java.util.Collections;
import java.util.List;

/**
 * @author Tomas Trajan
 * @creaded 2014-07-19
 */
public class DefaultRestService {

    private static final String API_KEY = "apiKey";
    private static final String LOCAL = "http://192.168.0.105:1337";
    private static final String LOCAL_API_KEY = "testKey1";
    private static final String PROD = "secret";
    private static final String PROD_API_KEY = "even bigger secret";

    private MainGamePanel game;
    private RestAdapter restAdapter;
    private ScoreRestService scoreService;

    public DefaultRestService(MainGamePanel game) {
        this.game = game;
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(PROD)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader(API_KEY, PROD_API_KEY);
                    }
                })
                .build();
        scoreService = restAdapter.create(ScoreRestService.class);
    }



    public List<ScoreDto> findTopTenScores(GameMode gameMode) {
        if (game.isNetworkAvailable()) {
            return scoreService.findTopTenScores(gameMode.name().toLowerCase());
        }
        return Collections.emptyList();
    }

    public List<ScoreDto> findScoresForPlayer(GameMode gameMode, String playerUuid) {
        if (game.isNetworkAvailable()) {
            return scoreService.findScoresForPlayer(gameMode.name().toLowerCase(), playerUuid);
        }
        return Collections.emptyList();
    }

    public void createOrUpdateScore(ScoreDto score) {
        if (game.isNetworkAvailable()) {
            scoreService.createOrUpdateScore(score);
        }
    }

}
