package br.com.brainweb.interview.core.features.powerstats;

import br.com.brainweb.interview.model.PowerStats;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class PowerStatsRowMapper implements RowMapper<PowerStats> {

    @Override
    public PowerStats mapRow(ResultSet rs, int i) throws SQLException {
        return PowerStats.builder()
                .id(UUID.fromString(rs.getString("id")))
                .strength(rs.getInt("strength"))
                .agility(rs.getInt("agility"))
                .dexterity(rs.getInt("dexterity"))
                .intelligence(rs.getInt("intelligence"))
                .build();
    }
}
