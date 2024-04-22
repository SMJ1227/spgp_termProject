# spgp_termProject
 
# High Concept
	* 플레이어는 달려가며 벽을 피해 적을 정확한 타이밍에 공격하여 제거하면서 최대한 오랜 시간 살아남는다.

# 핵심 메카닉
	* 플레이어는 위 아래로 현재 위치한 위치를 바꿀 수 있고, 적을 공격할 수 있다.
	* 정확한 타이밍에 공격하면 적을 제거할 수 있다.
 	* 적을 제거하지 못하거나 벽에 부딛히면 체력이 감소하고, 벽에 막히면 게임이 종료된다.
  	* 맵에 나오는 아이템을 획득하여 사용할 수 있음.

# 개발 범위
	* 플레이어 이동 범위: 3개 이상의 층
 	* 횡스크롤 맵
  		- 맵은 무한히 계속됨.
   		- 시간이 계속될수록 맵 진행 속도가 빨라짐.
	* 플레이어가 공격 가능한 몬스터
 		- 몬스터는  시간이 지날수록 더 높은 Hp를 가짐.
 	* 플레이어가 제거 불가능한 벽
  		- 벽은 정면에서 마주치면 플레이어의 Hp와 상관없이 바로 게임 종료.
    		- 벽을 밟거나 벽으로 점프하면 플레이어의 Hp가 감소.
      	* 플레이를 돕는 아이템
       		- 플레이어의 Hp를 늘려주는 아이템.      
 	* 일시정지, 메인화면과 같은 기본 UI
   
# 예상 게임 실행 흐름
![dd](https://github.com/SMJ1227/spgp_termProject/assets/112992077/10547a4b-7b3b-4b86-9d08-48a1cdf0810f)

# 개발 일정: 4월 4일에 시작하는 주를 1주차로 하여 9주간의 주단위 상세 개발 일정을 제시.
	* 1주차 게임 기획, 1차 발표 준비
 	* 2주차 게임 리소스 수집, 플레이어 이동 구현
 	* 3주차 맵 구현, 플레이어 공격 모션 구현 
 	* 4주차 몬스터 구현, 몬스터 이동
 	* 5주차 벽 구현, 중간 발표
 	* 6주차 충돌처리
 	* 7주차 인게임 ui, 시작 전 메인 화면과 일시정지 화면 구현
 	* 8주차 버그 수정, 테스트
 	* 9주차 최종 발표 준비
