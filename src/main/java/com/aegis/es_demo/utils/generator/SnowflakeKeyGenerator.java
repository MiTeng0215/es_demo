//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.aegis.es_demo.utils.generator;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 雪花算法ID生成器实现IdentifierGenerator，Configurable，以便加入hibernate自定义的id生成策略
 */
@Slf4j
public final class SnowflakeKeyGenerator implements IdentifierGenerator {
  public static final long EPOCH;
  private static final long SEQUENCE_BITS = 12L;//序列号
  private static final long WORKER_ID_BITS = 10L;//机器id
  private static final long SEQUENCE_MASK = 4095L;//最大序列号，序列号：0-4095，一个是2^12个序列号
  private static final long WORKER_ID_LEFT_SHIFT_BITS = 12L;//机器码左移
  private static final long TIMESTAMP_LEFT_SHIFT_BITS = 22L;//时间戳左移
  private static final long WORKER_ID_MAX_VALUE = 1024L;//最大机器码数量，2^10
  private static long workerId;
  private static int maxTolerateTimeDifferenceMilliseconds = 10;//针对回拨问题，最大容忍回拨时间是10ms
//设置起始时间，可以用到2010+69=2079年
  static {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2010, 0, 1);
    EPOCH = calendar.getTimeInMillis();
  }

  private byte sequenceOffset;
  private long sequence;
  private long lastMilliseconds;


  public SnowflakeKeyGenerator() {
  }

  public static void setWorkerId(long workerId) {
    Preconditions.checkArgument(workerId >= 0L && workerId < WORKER_ID_MAX_VALUE);
    SnowflakeKeyGenerator.workerId = workerId;
  }

  /**
   * 设置最大容忍回拨时间
   * @param maxTolerateTimeDifferenceMilliseconds
   */
  public static void setMaxTolerateTimeDifferenceMilliseconds(int maxTolerateTimeDifferenceMilliseconds) {
    SnowflakeKeyGenerator.maxTolerateTimeDifferenceMilliseconds = maxTolerateTimeDifferenceMilliseconds;
  }

  public synchronized Number generateKey() {
    long currentMilliseconds = System.currentTimeMillis();
    if (this.waitTolerateTimeDifferenceIfNeed(currentMilliseconds)) {
      currentMilliseconds = System.currentTimeMillis();
    }
//如果上一次时间与当前时间相同
    if (this.lastMilliseconds == currentMilliseconds) {
      if (0L == (this.sequence = this.sequence + 1L & SEQUENCE_MASK)) {
//        如果
        currentMilliseconds = this.waitUntilNextTime(currentMilliseconds);
      }
    } else {
      this.vibrateSequenceOffset();
      this.sequence = (long) this.sequenceOffset;
    }

    this.lastMilliseconds = currentMilliseconds;
    return currentMilliseconds - EPOCH << TIMESTAMP_LEFT_SHIFT_BITS | workerId << WORKER_ID_LEFT_SHIFT_BITS | this.sequence;
  }

  /**
   * 等待容差时间，如果当前时间小于上一次时间，那肯定是发生了时间回拨，如果时间在10ms以后我们可以通过线程等待的方式让机器把时间追上来
   * @param currentMilliseconds
   * @return
   */
  private boolean waitTolerateTimeDifferenceIfNeed(long currentMilliseconds) {
    try {
      if (this.lastMilliseconds <= currentMilliseconds) {//没有发生时间回拨，直接返回false
        return false;
//        发生了时间回拨
      } else {
        long timeDifferenceMilliseconds = this.lastMilliseconds - currentMilliseconds;//当前时间与上一次时间的时间差
        try {
          //校验回拨时间
          Preconditions.checkState(timeDifferenceMilliseconds < (long) maxTolerateTimeDifferenceMilliseconds, "Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", new Object[]{this.lastMilliseconds, currentMilliseconds});
        } catch (IllegalStateException var5) {
          log.error("回拨时间大于maxTolerateTimeDifferenceMilliseconds", var5);
        }
        Thread.sleep(timeDifferenceMilliseconds);//让机器追上当前时间
        return true;
      }
    } catch (InterruptedException e) {
      log.error("Thread.sleep出错", e);
      return true;
    }
  }

  private long waitUntilNextTime(long lastTime) {
    long result;
    for (result = System.currentTimeMillis(); result <= lastTime; result = System.currentTimeMillis()) {
    }

    return result;
  }

  private void vibrateSequenceOffset() {
    this.sequenceOffset = (byte) (~this.sequenceOffset & 1);
  }


  @Override
  public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
    return generateKey().longValue();

  }
}

