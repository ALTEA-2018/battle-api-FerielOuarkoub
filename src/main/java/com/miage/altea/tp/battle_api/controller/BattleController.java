package com.miage.altea.tp.battle_api.controller;

import com.miage.altea.tp.battle_api.bo.Battle;
import com.miage.altea.tp.battle_api.bo.Fight;
import com.miage.altea.tp.battle_api.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController("/api")
public class BattleController {
    private BattleService battleService;

    @GetMapping("/battles")
    public Iterable<Battle> battles() {
        return this.battleService.listBattles();
    }

    @GetMapping("/battles/{uuid}")
    public Battle battle(@PathVariable UUID uuid) {
        return this.battleService.getBattleByUUID(uuid);
    }

    @PostMapping(value = "/battles", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Battle> battles(@RequestBody Fight fight) {
        if (fight.getOpponent().equals(fight.getTrainer())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.battleService.createBattle(fight.getTrainer(), fight.getOpponent()));
    }

    @PostMapping("/battles/{uuid}/{trainerName}/attack")
    public ResponseEntity<Battle> attack(@PathVariable UUID uuid, @PathVariable String trainerName) {
        try {
            return ResponseEntity.ok(this.battleService.attack(uuid, trainerName));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Autowired
    public void setBattleService(BattleService battleService) {
        this.battleService = battleService;
    }
}
