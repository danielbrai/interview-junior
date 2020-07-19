package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.exception.HeroNotFoudException;
import br.com.brainweb.interview.core.features.powerstats.PowerStatsService;
import br.com.brainweb.interview.model.Hero;
import br.com.brainweb.interview.model.PowerStats;
import br.com.brainweb.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
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

    public Optional<Hero> getById(String id) {
        try {
            Hero hero = this.heroRepository.getById(UUID.fromString(id));
            return Optional.of(hero);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Collection<Hero> getByParam(String name) {
        return this.heroRepository.getByName(name);
    }

    @Transactional
    public void delete(String id) {
        Optional<Hero> optionalHero = this.getById(id);

        optionalHero.ifPresentOrElse(
                hero -> {
                    this.heroRepository.delete(hero.getId());
                    this.powerStatsService.delete(hero.getPowerStatsId().toString());
                },
                () -> {
                    throw new HeroNotFoudException("O herói pesquisado não foi encontrado");
                }
        );


    }
}
