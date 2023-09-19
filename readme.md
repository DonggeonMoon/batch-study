# spring-batch-study

스프링 배치에 대해 공부하고 정리합니다.

## 스프링 배치를 사용하는 이유
* 상태 관리
  * 배치 작업의 진행 상태(중단, 완료 에러 발생 여부 등)를 관리하고 중단 발생 시 다시 회복할 수 있는 기능을 제공해줌 
* 에러 분석
  * 작업의 크기가 클수록 에러를 분석하는데 시간과 노력이 더 들고 에러 분석이 힘들어짐
  * 스프링은 에러 분석을 쉽게 도와줌
* 기타
  * 작업 병렬화
  * 기타 부가 기능들

## 스프링 배치 구성요소들
* Job
* Step
* ItemReader
* ItemProcessor
* ItemWriter

## 표준 스코프 순서
1. Job 생성
2. Reader 생성 
3. Writer 생성
4. Test/Job 실행

## @StepScope 사용 시 순서
@StepScope가 붙으면 Job 실행 시마다 객체가 생성된다.
(아래는 Reader, Writer에 붙어 있는 경우)
1. Job 생성 
2. Test/Job 실행
3. Reader 생성
4. Writer 생성
(Job이 여러 개이면 다음과 같다.)
1. Job 생성
2. Test/Job 실행
3. Job1 시작 
4. Reader1 생성
5. Writer1 생성 
6. Job2 시작 
7. Reader2 생성
8. Writer2 생성

## 스코프
* StepScope
  * Job과 Step 실행 시마다 새로운 Reader, Writer 등을 생성
  * Reader나 Writer, Processor 등 정의 시에 사용
* JobScope
  * Job 실행 시마다 새로운 Step 빈을 생성
  * Step 등 정의 시에 사용