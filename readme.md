# spring-batch-study

스프링 배치에 대해 공부하고 정리합니다.

## 스프링 배치를 사용하는 이유
* 상태 관리
  * 배치 작업의 진행 상태(중단, 완료 에러 발생 여부 등)를 관리하고 중단 발생 시 다시 회복할 수 있는 기능을 제공해줌 
* 에러 분석
  * 작업의 크기가 클수록 에러를 분석하는데 시간과 노력이 더 들고 에러 분석이 힘들어짐
  * 스프링 배치는 에러 분석을 쉽게 도와줌
* 기타
  * 작업 병렬화
  * 기타 부가 기능들

## 스프링 배치 구성요소들
* Job
  * 수행하고자 하는 작업과 그 순서를 나타내며, 배치 처리 과정의 단위가 되는 인터페이스
* JobParameters
  * Job 인스턴스에 전달되는 매개변수들이며, Job을 구분한다. 따라서, JobParameter들이 같으면 같은 작업으로 간주되며, 실행 시 예외가 발생한다. 
* Step
  * Job의 순차적인 단계를 구성하는 인터페이스. Job은 하나 이상의 Step을 가진다.
  * 상태를 가짐
  * Tasklet 또는 Chunk 기반
* Tasklet
  * 실행할 작은 작업 메서드이자 함수형 인터페이스. RepeatStatus(CONTINUABLE 또는 FINISHED 상태)를 반환.
* JobRepository
  * Job을 실행하기 위한 정보를 저장하기 위해 필요한 유틸리티 클래스
* JobLauncher
  * Job 실행을 시작하기 위한 인터페이스
* JobLauncherTestUtils
  * 배치 Job들을 테스트하는 것을 도와주는 유틸리티 클래스
* ItemReader
  * 입력 데이터를 읽어들이기 위한 인터페이스
* ItemProcessor
  * 주어진 데이터를 처리하기 위한 인터페이스
* ItemWriter
  * 처리된 데이터의 출력 연산을 위한 인터페이스

## @EnableBachProcessing
사용 시 JobRepository, JobBuilderFactory, StepBuilderFactory 등 스프링 배치 실행에 필요한 빈 및 설정을 제공해줌

## ItemReader, ItemProcessor, ItemWriter
본 프로젝트 내에서는 JSON 파일을 읽고 쓰는 작업을 수행하였으므로 JsonItemReader과 JsonFileItemWriter를 사용하였음
* JsonItemReader<T>
  * JacksonJsonObjectReader라는 ObjectReader에 매핑을 위임함
* ItemProcessor<I, O>
  * I라는 타입을 처리하여 O 타입으로 반환함
* JsonFileItemWriter<T>
  * JacksonJsonObjectMarshaller라는 ObjectMarshaller에 마샬링을 위임함


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
(Job이 여러 개면 다음과 같다.)
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