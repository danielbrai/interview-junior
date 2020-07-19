package br.com.brainweb.interview.core.features.powerstats;

import br.com.brainweb.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PowerStatsRepository {

    private static final String CREATE_POWER_STATS_QUERY = "INSERT INTO power_stats" +
            " (strength, agility, dexterity, intelligence)" +
            " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";

    private static final String DELETE_QUERY = "DELETE FROM power_stats p " +
            "WHERE p.id=:id";

    private static final String FIND_POWER_STATS_BY_ID = "SELECT " +
            "p.id, p.strength, p.agility, p.dexterity, p.intelligence, p.created_at, p.updated_at " +
            "FROM power_stats p " +
            "WHERE p.id =:id";

    private static final String UPDATE_POWER_STATS_QUERY = "UPDATE power_stats " +
            "SET strength =:strength, agility =:agility, dexterity =:dexterity, intelligence =:intelligence " +
            "WHERE id =:id";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(PowerStats powerStats) {
        return namedParameterJdbcTemplate.queryForObject(
                CREATE_POWER_STATS_QUERY,
                new BeanPropertySqlParameterSource(powerStats),
                UUID.class);
    }

    void delete(UUID id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        this.namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
    }

    PowerStats getById(UUID id) {
        final SqlParameterSource params = new MapSqlParameterSource("id", id);

        return this.namedParameterJdbcTemplate.queryForObject(
                FIND_POWER_STATS_BY_ID,
                params,
                new PowerStatsRowMapper()
        );
    }

    void update(PowerStats powerStats) {

        final Map<String, Object> params = Map.of("strength", powerStats.getStrength(),
                "agility", powerStats.getAgility(),
                "dexterity", powerStats.getDexterity(),
                "intelligence", powerStats.getIntelligence(),
                "id", powerStats.getId());

        this.namedParameterJdbcTemplate.update(
                UPDATE_POWER_STATS_QUERY,
                params
        );
    }
}
