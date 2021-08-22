package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.ws.soap.Addressing;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception{
        List<Seckill> list=seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() throws Exception{
        long seckillId=1000;
        Seckill seckill=seckillService.getById(seckillId);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void testSeckillLogic() throws Exception{
        long seckillId=1000;
        Exposer exposer=seckillService.exportSeckillurl(seckillId);
        if (exposer.isExposed())
        {
            logger.info("exposer={}",exposer);
            long userPhone=13476191896L;
            String md5=exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.excuteSeckill(seckillId, userPhone, md5);
                logger.info("result={}",seckillExecution);
            }catch (RepeatKillException e)
            {
                logger.error(e.getMessage());
            }catch (SeckillCloseException e1)
            {
                logger.error(e1.getMessage());
            }
        }else {
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }
    }

    @Test
    public void testExportSeckillUrl() throws Exception{
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillurl(id);
        logger.info("exposer={}",exposer);
        //exposer=Exposer{exposed=true, md5='e1d5d1f6ada8d54a0d06eabc8ea64c93', seckillId=1000, now=0, start=0, end=0}
        //exposer=Exposer{exposed=true, md5='329d1b656bb4f66d65f3bad0e106a790', seckillId=1001, now=0, start=0, end=0}
    }

    @Test
    public void excuteSeckill() {
        long seckillId=1001;
        long phone = 18637123369L;
        String md5="329d1b656bb4f66d65f3bad0e106a790";
        SeckillExecution execution = seckillService.excuteSeckill(seckillId,phone,md5);
        logger.info("result={}",execution);
        //result=SeckillExecution{seckillId=1001, state=1, stateInfo='秒杀成功', successKilled=SuccessKilled{seckillId=1001, userPhone=18637123369, state=0, createTime=Sun Aug 15 00:59:51 CST 2021}}
    }

    @Test
    public void executeSeckillProcedureTest(){
        long seckillId = 1001;
        long phone = 18637123380L;
        Exposer exposer = seckillService.exportSeckillurl(seckillId);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId,phone,md5);
            logger.info(execution.getStateInfo());
        }
    }
}