package com.miage.altea.tp.battle_api.repository;

import com.miage.altea.tp.battle_api.bo.Battle;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BattleRepositoryImpl implements BattleRepository{

    private ConcurrentHashMap<UUID, Battle> map;

    public BattleRepositoryImpl() {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public List<Battle> listBattles() {
        return List.copyOf(this.map.values());
    }

    @Override
    public Battle findBattleByID(UUID id) {
        return this.map.get(id);
    }


    public void updateBattle(Battle battle) {
        this.map.put(battle.uuid, battle);
    }
}
