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

    public void updateLayoutWidth(int newWidth) {
        myEdit.putInt("LAYOUT_WIDTH", newWidth);
        myEdit.apply();
    }

    public int getLayoutWidth() {
        return sharedPreferences.getInt("LAYOUT_WIDTH", 0);
    }

    public void updateBattleStatus(String battleStatus) {
        myEdit.putString("BATTLE_STATUS", battleStatus);
        myEdit.apply();
    }

    public String getBattleStatus() {
        return sharedPreferences.getString("BATTLE_STATUS", "EMPTY");
    }

    public void updateCurrentFleet(String currentFleet) {
        myEdit.putString("CURRENT_FLEET", currentFleet);
        myEdit.apply();
    }

    public String getCurrentFleet() {
        return sharedPreferences.getString("CURRENT_FLEET", "EMPTY");
    }

    public void updateMyShotStatus(String shotStatus) {
        myEdit.putString("SHOT_STATUS", shotStatus);
        myEdit.apply();
    }

    public String getMyShotStatus() {
        return sharedPreferences.getString("SHOT_STATUS", "SHOT_NOT_TAKEN");
    }

    public void updateEnemyShotStatus(String enemyShotStatus) {
        myEdit.putString("ENEMY_SHOT_STATUS", enemyShotStatus);
        myEdit.apply();
    }

    public String getEnemyShotStatus() {
        return sharedPreferences.getString("ENEMY_SHOT_STATUS", "SHOT_NOT_TAKEN");
    }


}
