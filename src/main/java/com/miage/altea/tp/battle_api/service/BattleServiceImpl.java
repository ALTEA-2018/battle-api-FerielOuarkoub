package com.miage.altea.tp.battle_api.service;


import com.miage.altea.tp.battle_api.bo.Battle;
import com.miage.altea.tp.battle_api.bo.BattleState;
import com.miage.altea.tp.battle_api.bo.Pokemon;
import com.miage.altea.tp.battle_api.bo.Trainer;
import com.miage.altea.tp.battle_api.repository.BattleRepository;
import com.miage.altea.tp.battle_api.utils.BattleUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class BattleServiceImpl implements BattleService {
    private BattleRepository battleRepository;
    private TrainerService trainerService;

    @Override
    public List<Battle> listBattles() {
        return this.battleRepository.listBattles();
    }

    @Override
    public Battle getBattleByID(UUID id) {
        return this.battleRepository.findBattleByID(id);
    }


    //methode to create a battle between two pokemons
    @Override
    public Battle createBattle(String trainerName, String opponentName) {
        Battle battle = new Battle();
        System.out.println(trainerName);
        System.out.println(opponentName);
        Trainer trainer = this.trainerService.getTrainerByName(trainerName);
        Trainer opponent = this.trainerService.getTrainerByName(opponentName);

        UUID id = UUID.randomUUID();
        battle.setUuid(id);

        Pokemon trainerPokemon = trainer.getTeam().stream().filter(x -> x.getHp() > 0).findFirst().orElse(null);
        Pokemon opponentPokemon = opponent.getTeam().stream().filter(x -> x.getHp() > 0).findFirst().orElse(null);

        if (trainerPokemon == null || opponentPokemon == null) {
            throw new IllegalArgumentException("Can't create game: no pokemon available");
        }

        if (trainerPokemon.getSpeed() > opponentPokemon.getSpeed()) {
            trainer.setNextTurn(true);
            opponent.setNextTurn(false);
        } else if (opponentPokemon.getSpeed() > trainerPokemon.getSpeed()) {
            trainer.setNextTurn(false);
            opponent.setNextTurn(true);
        } else {
            var random = new Random();
            int index = random.nextInt(2);

            switch (index) {
                case 0: {
                    trainer.setNextTurn(true);
                    opponent.setNextTurn(false);
                    break;
                }
                case 1: {
                    trainer.setNextTurn(false);
                    opponent.setNextTurn(true);
                    break;
                }
            }
        }

        battle.setTrainer(trainer);
        battle.setOpponent(opponent);
        battle.setBattleState(BattleState.STARTING);
        this.battleRepository.updateBattle(battle);
        return battle;
    }

    @Override
    //methode to attack pokemons
    public Battle attack(UUID id, String attacker) {
        Battle battle = this.getBattleByID(id);

        if (battle == null) {
            throw new IllegalArgumentException("No battle found");
        }
        if (battle.battleState.equals(BattleState.TERMINATE)) {
            throw new IllegalArgumentException("Battle currently terminated");
        }

        Trainer trainer;
        Trainer opponent;

        if (attacker.equals(battle.getTrainer().getName())) {
            trainer = battle.getTrainer();
            opponent = battle.getOpponent();

            if (!trainer.isNextTurn()) {
                throw new IllegalArgumentException("It's not your turn! Relax!");
            }
        } else {
            trainer = battle.getOpponent();
            opponent = battle.getTrainer();

            if (!trainer.isNextTurn()) {
                throw new IllegalArgumentException("It's not your turn! Relax!");
            }
        }

        trainer.getTeam()
                .stream()
                .filter(x -> x.getHp() > 0)
                .findFirst()
                .ifPresent(one -> {
                    opponent.getTeam()
                            .stream()
                            .filter(x -> x.getHp() > 0)
                            .findFirst()
                            .ifPresent(x -> {
                                x.setHp(BattleUtilities.attack(x.getHp(), one.getLevel(), one.getAttack(), x.getDefense()));

                                if (x.getHp() == 0) {
                                    x.setKo(true);
                                    x.setAlive(false);
                                }
                            });
                });

        if (trainer.getName().equals(battle.getTrainer().getName())) {
            battle.setTrainer(trainer);
            battle.setOpponent(opponent);

            battle.getTrainer().setNextTurn(false);
            battle.getOpponent().setNextTurn(true);
        } else {
            battle.setOpponent(trainer);
            battle.setTrainer(opponent);

            battle.getOpponent().setNextTurn(false);
            battle.getTrainer().setNextTurn(true);
        }

        if (battle.getTrainer().getTeam().stream().allMatch(x -> x.getHp() == 0)
                || battle.getOpponent().getTeam().stream().allMatch(x -> x.getHp() == 0)) {
            battle.setBattleState(BattleState.TERMINATE);
        } else {
            battle.setBattleState(BattleState.INPROGRESS);
        }

        this.battleRepository.updateBattle(battle);
        return battle;
    }

    @Autowired
    public void setBattleRepository(BattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

}
