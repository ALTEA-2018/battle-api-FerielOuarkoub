package com.miage.altea.tp.battle_api.bo;
import java.util.UUID;

public class Battle {
    public UUID uuid;
    public Trainer trainer;
    public Trainer opponent;
    public BattleState battleState;

    public Battle() {
    }

    public Battle(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainer getOpponent() {
        return opponent;
    }

    public void setOpponent(Trainer opponent) {
        this.opponent = opponent;
    }

    public BattleState getBattleState() {
        return battleState;
    }

    public void setBattleState(BattleState battleState) {
        this.battleState = battleState;
    }
}