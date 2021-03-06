package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.model.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HeroRepository {

    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
            " (name, race, power_stats_id)" +
            " VALUES (:name, :race, :powerStatsId) RETURNING id";

    private static final String FIND_HERO_BY_ID = "SELECT " +
            "h.id, h.name, h.race, h.power_stats_id, h.enabled, h.created_at, h.updated_at " +
            "FROM hero h " +
            "WHERE h.id =:id ";

    private static final String FIND_HERO_BY_PARAM = " SELECT " +
            "h.id, h.name, h.race, h.power_stats_id, h.enabled, h.created_at, h.updated_at " +
            "FROM hero h " +
            "WHERE LOWER(h.name) LIKE :name";

    private static final String DELETE_QUERY = "DELETE FROM hero h " +
            "WHERE h.id=:id";

    public static final String UPDATE_HERO_QUERY = "UPDATE hero " +
            "SET name =:name, race =:race " +
            "WHERE id =:id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(Hero hero) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId());

        return namedParameterJdbcTemplate.queryForObject(
                CREATE_HERO_QUERY,
                params,
                UUID.class);
    }

    Hero getById(UUID heroId) {
        final SqlParameterSource params = new MapSqlParameterSource("id", heroId);

        return this.namedParameterJdbcTemplate.queryForObject(
                FIND_HERO_BY_ID,
                params,
                new HeroRowMapper()
        );
    }

    Collection<Hero> getByName(String name) {
        final SqlParameterSource params = new MapSqlParameterSource("name", "%" + name.toLowerCase() + "%");

        return this.namedParameterJdbcTemplate.query(
                FIND_HERO_BY_PARAM,
                params,
                new HeroRowMapper()
        );
    }

    void delete(UUID id) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        this.namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
    }


    public void update(Hero hero) {

        final Map<String, Object> params = Map.of("name", hero.getName(),
                "race", hero.getRace().name(),
                "id", hero.getId());

        final SqlParameterSource namedParameters = new MapSqlParameterSource(params);

        this.namedParameterJdbcTemplate.update(
                UPDATE_HERO_QUERY,
                namedParameters
        );
    }
}
