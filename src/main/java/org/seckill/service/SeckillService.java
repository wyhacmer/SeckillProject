package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

public interface SeckillService {

    List<Seckill> getSeckillList();

    Seckill getById(long seckillId);

    Exposer exportSeckillurl(long seckillId);

    SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5) throws SeckillException,RepeatKillException, SeckillCloseException;

    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
