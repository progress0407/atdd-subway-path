# 지하철 노선도 미션

> [API 문서](https://techcourse-storage.s3.ap-northeast-2.amazonaws.com/c4c291f19953498e8eda8a38253eed51#Path)

# 요구사항 정리
## 1단계
- 출발역과 도착역 사이의 최단 경로 및 요금을 구하는 API만들기
  - [x] 최단 경로를 구한다.
    - 한 노선뿐만 아니라 환승도 고려한다.
    - 최단 경로는 다익스트라 라이브러리를 활용한다.
  - [x] 거리에 따른 요금을 계산한다.
    - 10km 까지는 1250원이다.
    - 10~50km 까지는 5km마다 100원씩 증액한다.
    - 50km 초과부터는 8km마다 100원씩 증액한다.

## 2 단계

- [x] 노선별 추가 요금
  - 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
  - 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
- [x] 연령별 요금 할인
  - 청소년: 운임에서 350원을 공제한 금액의 20%할인
  - 어린이: 운임에서 350원을 공제한 금액의 50%할인