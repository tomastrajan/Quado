package com.trajan.android.game.Quado.rest;

import com.trajan.android.game.Quado.components.Score;
import com.trajan.android.game.Quado.rest.dto.ScoreDto;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import java.util.List;

/**
 * @author Tomas Trajan
 * @creaded 2014-07-19
 */
public interface ScoreRestService {

    @GET("/score/{gameMode}")
    List<ScoreDto> findTopTenScores(@Path("gameMode") String gameMode);

    @GET("/score/{gameMode}/{playerUuid}")
    List<ScoreDto> findScoresForPlayer(@Path("gameMode") String gameMode, @Path("playerUuid") String playerUuid);

    @POST("/score")
    ScoreDto createOrUpdateScore(@Body ScoreDto score);
}
