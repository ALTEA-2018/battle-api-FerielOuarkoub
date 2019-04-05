package com.miage.altea.tp.battle_api.service;

import com.miage.altea.tp.battle_api.bo.Trainer;

public interface TrainerService {
    Trainer getTrainerByName(String name);
}
