package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.exception.HeroNotFoudException;
import br.com.brainweb.interview.core.features.powerstats.PowerStatsService;
import br.com.brainweb.interview.model.Hero;
import br.com.brainweb.interview.model.PowerStats;
import br.com.brainweb.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;

    private final PowerStatsService powerStatsService;

    @Transactional
    public UUID create(CreateHeroRequest createHeroRequest) {
        UUID poweStatsId = this.powerStatsService.create(new PowerStats(createHeroRequest));
        return heroRepository.create(new Hero(createHeroRequest, poweStatsId));
    }

    public Hero getById(String id) {
        try {
            Hero hero = this.heroRepository.getById(UUID.fromString(id));
            return hero;
        } catch (EmptyResultDataAccessException e) {
            throw new HeroNotFoudException("O herói pesquisado não foi encontrado");
        }
    }

    public Collection<Hero> getByParam(String name) {
        return this.heroRepository.getByName(name);
    }

    @Transactional
    public void delete(String id) {
        Hero hero = this.getById(id);
        this.heroRepository.delete(hero.getId());
        this.powerStatsService.delete(hero.getPowerStatsId().toString());
    }

    public void put(CreateHeroRequest heroPayload, String id) {

        Hero hero = this.getById(id);
        PowerStats powerStats = this.powerStatsService.getById(hero.getPowerStatsId().toString());

        powerStats.setAgility(Objects.nonNull(heroPayload.getAgility()) ? heroPayload.getAgility() : powerStats.getAgility());
        powerStats.setDexterity(Objects.nonNull(heroPayload.getDexterity()) ? heroPayload.getDexterity() : powerStats.getDexterity());
        powerStats.setStrength(Objects.nonNull(heroPayload.getStrength()) ? heroPayload.getStrength() : powerStats.getStrength());
        powerStats.setIntelligence(Objects.nonNull(heroPayload.getIntelligence()) ? heroPayload.getIntelligence() : powerStats.getIntelligence());

        this.powerStatsService.update(powerStats);

        hero.setName(Objects.nonNull(heroPayload.getName()) ? heroPayload.getName() : hero.getName());
        hero.setRace(Objects.nonNull(heroPayload.getRace()) ? heroPayload.getRace() : hero.getRace());

        this.heroRepository.update(hero);
    }
}
