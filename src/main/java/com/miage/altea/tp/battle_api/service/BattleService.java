package com.miage.altea.tp.battle_api.service;

import com.miage.altea.tp.battle_api.bo.Battle;

import java.util.List;
import java.util.UUID;

public interface BattleService {
    List<Battle> listBattles();
    Battle getBattleByID(UUID id);
    Battle createBattle(String trainer, String opponent);
    Battle attack(UUID id, String trainer);
}
