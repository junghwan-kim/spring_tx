package hello.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class RollBackTest {

    @Autowired RollBackService service;

    @Test
    void runtimeException(){
        Assertions.assertThatThrownBy(()-> service.runtimeException())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedException(){
        Assertions.assertThatThrownBy(()-> service.checkedException())
                .isInstanceOf(MyException.class);
    }

    @Test
    void rollBackFor(){
        Assertions.assertThatThrownBy(()-> service.rollBackFor())
                .isInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class RollBackTestConfig {
        @Bean
        RollBackService rollBackService(){
            return new RollBackService();
        }
    }

    @Slf4j
    static class RollBackService{

        //런타임 예외 발생 : 롤백
        @Transactional
        public void runtimeException(){
            log.info("call runtimeException");
            throw new RuntimeException();
        }

        //체크 예외 발생 : 커밋
        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

        //체크 예외 rollBackFor 지정 : 롤백
        @Transactional(rollbackFor = MyException.class)
        public void rollBackFor() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

    }

    static class MyException extends Exception {

    }
}
