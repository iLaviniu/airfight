package com.game.airfight.backend;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class AirfightDataBase {

    SharedPreferences sharedPreferences = null;

    SharedPreferences.Editor myEdit = null;

    public AirfightDataBase(Context _context) {

        sharedPreferences = _context.getSharedPreferences("AirfightDataBase", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
    }

    public void updateFuselageId(int fuselageColorId) {
        myEdit.putInt("AIRPLANE_FUSELAGE_ID", fuselageColorId);
        myEdit.apply();
    }

    public int getFuselageId() {
        return sharedPreferences.getInt("AIRPLANE_FUSELAGE_ID", -1);
    }

    public void updateGameDifficulty(String newDifficulty) {
        myEdit.putString("GAME_DIFFICULTY", newDifficulty);
        myEdit.apply();
    }

    public String getGameDifficulty() {
        return sharedPreferences.getString("GAME_DIFFICULTY", "EASY");
    }


}
