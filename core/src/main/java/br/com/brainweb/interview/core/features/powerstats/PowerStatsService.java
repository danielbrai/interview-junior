package br.com.brainweb.interview.core.features.powerstats;

import br.com.brainweb.interview.core.exception.PowerStatsNotFoundException;
import br.com.brainweb.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PowerStatsService {

    private final PowerStatsRepository powerStatsRepository;

    @Transactional
    public UUID create(PowerStats powerStats) {
        return powerStatsRepository.create(powerStats);
    }

    private Optional<PowerStats> getById(String id) {
        return Optional.ofNullable(this.powerStatsRepository.getById(UUID.fromString(id)));
    }

    public void delete(String id) {

        Optional<PowerStats> optionalPowerStats = this.getById(id);

        optionalPowerStats.ifPresentOrElse(
                powerStats -> this.powerStatsRepository.delete(powerStats.getId()),
                () -> {
                    throw new PowerStatsNotFoundException("A informação de poder pesquisad não foi encontrado");
                }
        );
    }
}
