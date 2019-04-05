package com.miage.altea.tp.battle_api.repository;

import com.miage.altea.tp.battle_api.bo.Battle;

import java.util.List;
import java.util.UUID;

public interface BattleRepository {
    List<Battle> listBattles();
    Battle findBattleByID(UUID uuid);
    void updateBattle(Battle battle);
}
