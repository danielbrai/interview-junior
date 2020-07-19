package br.com.brainweb.interview.core.features.powerstats;

import br.com.brainweb.interview.core.exception.PowerStatsNotFoundException;
import br.com.brainweb.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PowerStatsService {

    private final PowerStatsRepository powerStatsRepository;

    @Transactional
    public UUID create(PowerStats powerStats) {
        return powerStatsRepository.create(powerStats);
    }

    public PowerStats getById(String id) {
        try {
            return this.powerStatsRepository.getById(UUID.fromString(id));
        } catch (EmptyResultDataAccessException e) {
            throw new PowerStatsNotFoundException("A informação de status pesquisa não foi encontrado");
        }
    }

    public void delete(String id) {
        PowerStats powerStats = this.getById(id);
        this.powerStatsRepository.delete(powerStats.getId());
    }

    @Transactional
    public void update(PowerStats powerStats) {
        this.powerStatsRepository.update(powerStats);
    }
}
