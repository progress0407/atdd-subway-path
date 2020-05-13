package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.ShortestDistanceResponse;
import wooteco.subway.admin.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {
/*
### Feature: 지하철 경로 조회
```
  Scenario: 지하철 경로를 조회한다.
    Given 여러 개의 노선에 여러 개의 지하철역이 추가되어있다.

    When 시작역과 도착역의 최단 거리 조회를 요청한다.

    Then 시작역과 도착역의 최단 거리를 응답 받는다.
```
 */

    @Test
    void searchShortestPath() {
        //given
        LineResponse line = createLine("2호선");
        StationResponse station1 = createStation("잠실역");
        StationResponse station2 = createStation("삼성역");
        StationResponse station3 = createStation("선릉역");

        addLineStation(line.getId(), null, station1.getId(), 10, 10);
        addLineStation(line.getId(), station1.getId(), station2.getId(), 10, 10);
        addLineStation(line.getId(), station2.getId(), station3.getId(), 10, 10);
        //when
        ShortestDistanceResponse path = getShortestDistancePath(station1.getId(), station3.getId());
        //then
        List<StationResponse> stations = path.getStations();
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations.get(0).getId()).isEqualTo(station1.getId());
        assertThat(stations.get(1).getId()).isEqualTo(station2.getId());
        assertThat(stations.get(2).getId()).isEqualTo(station3.getId());
        assertThat(path.getDistance()).isEqualTo(30);
        assertThat(path.getDuration()).isEqualTo(30);
    }

    private ShortestDistanceResponse getShortestDistancePath(Long sourceId, Long targetId) {
        return given().
            queryParam("source", sourceId).
            queryParam("target", targetId).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/path/distance").
            then().
            log().all().
            statusCode(HttpStatus.OK.value()).
            extract().as(ShortestDistanceResponse.class);
    }
}
