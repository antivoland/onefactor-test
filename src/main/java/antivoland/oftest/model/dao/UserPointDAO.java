package antivoland.oftest.model.dao;

import antivoland.oftest.model.Tile;
import antivoland.oftest.model.UserPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserPointDAO {
    private static final RowMapper<UserPoint> ROW_MAPPER = (rs, i) -> {
        int userId = rs.getInt("user_id");
        double lat = rs.getDouble("lat");
        double lon = rs.getDouble("lon");
        return new UserPoint(userId, lat, lon);
    };

    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserPoint find(int userId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM user_point WHERE user_id = ?",
                ROW_MAPPER,
                userId
        );
    }

    public void insert(UserPoint userPoint) {
        jdbcTemplate.update(
                "INSERT INTO user_point(user_id, lat, lon) VALUES (?, ?, ?)",
                userPoint.userId,
                userPoint.lat,
                userPoint.lon
        );
    }

    public int update(UserPoint userPoint) {
        return jdbcTemplate.update(
                "UPDATE user_point SET lat = ?, lon = ? WHERE user_id = ?",
                userPoint.lat,
                userPoint.lon,
                userPoint.userId
        );
    }

    public void delete(int userId) {
        jdbcTemplate.update(
                "DELETE FROM user_point WHERE user_id = ?",
                userId
        );
    }

    public int population(int tileY, int tileX) {
        int[] tileYBounds = Tile.bounds(tileY);
        int[] tileXBounds = Tile.bounds(tileX);
        return jdbcTemplate.queryForObject(
                "SELECT count(*) FROM user_point WHERE (lat > ? AND lat < ? OR lat = ?) AND (lon > ? AND lon < ? OR lon = ?)",
                Integer.class,
                tileYBounds[0],
                tileYBounds[1],
                tileY,
                tileXBounds[0],
                tileXBounds[1],
                tileX
        );
    }
}
