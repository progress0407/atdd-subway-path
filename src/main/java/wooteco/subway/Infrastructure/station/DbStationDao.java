package wooteco.subway.Infrastructure.station;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Station;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DbStationDao implements StationDao {

    private static final RowMapper<Station> ROW_MAPPER = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Station(id, name);
    };
    private final JdbcTemplate jdbcTemplate;

    public DbStationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(Station station) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO STATION(name) VALUES(?)", new String[]{"id"});
            ps.setString(1, station.getName());
            return ps;
        }, keyHolder);

        long savedStationId = keyHolder.getKey().longValue();
        return savedStationId;
    }

    @Override
    public List<Station> findAll() {
        return jdbcTemplate.query("SELECT id, name FROM STATION", ROW_MAPPER);
    }

    @Override
    public List<Station> findByIdIn(Collection<Long> sortedStationIds) {
        String sortedStationsIdsString = sortedStationIds.stream()
                .map(it -> String.valueOf(it))
                .collect(Collectors.joining(", "));
        String selectInClauseQuery = String.format("SELECT id, name FROM STATION WHERE id IN (%s)", sortedStationsIdsString);

        List<Station> stations = jdbcTemplate.query(selectInClauseQuery, ROW_MAPPER);
        return sort(sortedStationIds, stations);
    }

    @Override
    public Optional<Station> findById(Long id) {
        try {
            Station station = jdbcTemplate.queryForObject("SELECT id, name FROM STATION WHERE id = ?", ROW_MAPPER, id);
            return Optional.of(station);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public boolean existByName(String name) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT id FROM STATION WHERE name = ? LIMIT 1 ) AS `exists`",
                Boolean.class, name);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM STATION WHERE id = ?", id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM STATION");
    }
}
