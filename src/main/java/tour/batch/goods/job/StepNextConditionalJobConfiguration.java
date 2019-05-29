package tour.batch.goods.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StepNextConditionalJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalStep1())
                    .on("FAILED") //FAILED 일 경우
                    .to(conditionalStep3()) // Step3으로 이동
                    .on("*") // step3의 결과 관계없이
                    .end() // step3으로 이동하면 Flow가 종료한다.
                .from(conditionalStep1()) // step1로부터
                    .on("*") //FAILED 외에 모든 경우
                    .to(conditionalStep2()) // Step2으로 이동
                    .next(conditionalStep3()) // Step2가 정상 종료되면 step3으로 이동한다.
                    .on("*") //step3의 결과 관계없이
                    .end() // step3으로 이동하면 Flow가 종료한다.
                .end() // job 종료
                .build();
    }

    @Bean
    public Step conditionalStep1() {
        return stepBuilderFactory.get("conditionalStep1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step1");
                    /**
                         ExitStatus를 FAILED로 지정한다.
                         해당 status를 보고 flow가 진행된다
                     **/
                    //stepContribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalStep2() {
        return stepBuilderFactory.get("conditionalStep2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalStep3() {
        return stepBuilderFactory.get("conditionalStep3")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
