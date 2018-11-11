package org.chim.altass.executor.redis.support;


import org.chim.altass.base.utils.type.ArrayUtil;
import org.chim.altass.executor.redis.bean.Param;
import org.chim.altass.executor.redis.constant.OpType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class Name: JedisOperatorTool
 * Create Date: 18-3-26 上午11:36
 * Creator: chenchenghao
 * Version: v1.0
 * Updater:cch
 * Date Time:
 * Description:
 * <p>
 * Package Accessible
 */
@SuppressWarnings("unchecked")
class Tool {

    static Operator<Boolean> exists() {
        return new Operator<Boolean>() {
            @Override
            public Boolean exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.exists(param.buildKey());
            }
        };
    }

    static Operator<Long> ttl() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.ttl(param.buildKey());
            }
        };
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    static Operator<Long> incr() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.incr(param.buildKey());
            }
        };
    }

    static Operator<Long> incrBy() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                checkParams(param, 1);
                return jedis.incrBy(param.buildKey(),
                        Long.valueOf(param.getValues()[0].toString()));
            }
        };
    }

    static Operator<Long> decr() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.decr(param.buildKey());
            }
        };
    }

    static Operator<Long> decrBy() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                checkParams(param, 1);
                return jedis.decrBy(param.buildKey(),
                        Long.valueOf(param.getValues()[0].toString()));
            }
        };
    }

    static Operator<Long> del() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.DEL);
                return jedis.del(param.buildKey());
            }
        };
    }

    static Operator<String> set() {
        return new Operator<String>() {
            @Override
            public String exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                checkParams(param, 1);
                return jedis.set(param.buildKey(), String.valueOf(param.getValues()[0]));
            }
        };
    }

    static Operator<String> get() {
        return new Operator<String>() {
            @Override
            public String exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.get(param.buildKey());
            }
        };
    }


    ///////////////////////////////////////////////////////////////////////////


    static Operator<Long> hset() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                checkParams(param, 2);
                return jedis.hset(param.buildKey(),
                        param.getValues()[0].toString(),
                        param.getValues()[1].toString());
            }
        };
    }

    static Operator<String> hmset() {
        return new Operator<String>() {
            @Override
            public String exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                checkParams(param, 1);
                return jedis.hmset(param.buildKey(),
                        (Map<String, String>) param.getValues()[0]);
            }
        };
    }

    static Operator<String> hget() {
        return new Operator<String>() {
            @Override
            public String exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                checkParams(param, 1);
                return jedis.hget(param.buildKey(), param.getValues()[0].toString());
            }
        };
    }

    static Operator<List<String>> hmget() {
        return new Operator<List<String>>() {
            @Override
            public List<String> exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.hmget(param.buildKey(), ArrayUtil.objArr2StrArr(param.getValues()));
            }
        };
    }

    static Operator<Map<String, String>> hgetAll() {
        return new Operator<Map<String, String>>() {
            @Override
            public Map<String, String> exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.hgetAll(param.buildKey());
            }
        };
    }

    static Operator<Boolean> hexists() {
        return new Operator<Boolean>() {
            @Override
            public Boolean exec(Jedis jedis, Param param) {
                checkParams(param, 1);
                return jedis.hexists(param.buildKey(), param.getValues()[0].toString());
            }
        };
    }

    static Operator<Long> hincrby() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                checkParams(param, 2);
                return jedis.hincrBy(param.buildKey(),
                        param.getValues()[0].toString(),
                        Long.valueOf(param.getValues()[1].toString()));
            }
        };
    }

    static Operator<Long> hlen() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.hlen(param.buildKey());
            }
        };
    }

    static Operator<Long> hdel() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.hdel(param.buildKey(), ArrayUtil.objArr2StrArr(param.getValues()));
            }
        };
    }

    static Operator<Long> rpush() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.rpush(param.buildKey(), ArrayUtil.objArr2StrArr(param.getValues()));
            }
        };
    }

    static Operator<Long> lpush() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.lpush(param.buildKey(), ArrayUtil.objArr2StrArr(param.getValues()));
            }
        };
    }

    /**
     * 按照列表索引获得列表的值
     *
     * @return -
     */
    static Operator<String> lindex() {
        return new Operator<String>() {
            @Override
            public String exec(Jedis jedis, Param param) {
                return jedis.lindex(param.buildKey(), (Long) param.getValues()[0]);
            }
        };
    }

    static Operator<String> rpop() {
        return new Operator<String>() {
            @Override
            public String exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.rpop(param.buildKey());
            }
        };
    }

    static Operator<String> lpop() {
        return new Operator<String>() {
            @Override
            public String exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.lpop(param.buildKey());
            }
        };
    }

    static Operator<Long> llen() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.llen(param.buildKey());
            }
        };
    }

    static Operator<List<String>> lrange() {
        return new Operator<List<String>>() {
            @Override
            public List<String> exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.lrange(param.buildKey(),
                        Long.valueOf(param.getValues()[0].toString()),
                        Long.valueOf(param.getValues()[1].toString())
                );
            }
        };
    }

    static Operator<Long> lrem() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.lrem(param.buildKey(), Long.valueOf(param.getValues()[0].toString()),
                        param.getValues()[1].toString());
            }
        };
    }

    static Operator<String> lset() {
        return new Operator<String>() {
            @Override
            public String exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.lset(param.buildKey(), Long.valueOf(param.getValues()[0].toString()),
                        param.getValues()[1].toString());
            }
        };
    }

    ///////////////////////////////////////////////////////////////////////////////////

    static Operator<Long> sadd() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.sadd(param.buildKey(), ArrayUtil.objArr2StrArr(param.getValues()));
            }
        };
    }

    static Operator<String> spop() {
        return new Operator<String>() {
            @Override
            public String exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.spop(param.buildKey());
            }
        };
    }


    static Operator<Set<String>> smembers() {
        return new Operator<Set<String>>() {
            @Override
            public Set<String> exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.smembers(param.buildKey());
            }
        };
    }

    static Operator<Boolean> sismember() {
        return new Operator<Boolean>() {
            @Override
            public Boolean exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.sismember(param.buildKey(),
                        param.getValues()[0].toString());
            }
        };
    }

    static Operator<Long> scard() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.scard(param.buildKey());
            }
        };
    }

    static Operator<String> srandmember() {
        return new Operator<String>() {
            @Override
            public String exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.srandmember(param.buildKey());
            }
        };
    }

    static Operator<Long> srem() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.srem(param.buildKey(),
                        ArrayUtil.objArr2StrArr(param.getValues()));
            }
        };
    }

    static Operator<List<String>> sort() {
        return new Operator<List<String>>() {
            @Override
            public List<String> exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.sort(param.buildKey());
            }
        };
    }

    static Operator<List<String>> sortByParam() {
        return new Operator<List<String>>() {
            @Override
            public List<String> exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.sort(param.buildKey(),
                        (SortingParams) param.getValues()[0]);
            }
        };
    }

    static Operator<Long> zadd() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.zadd(param.buildKey(),
                        Double.valueOf(param.getValues()[0].toString()),
                        param.getValues()[1].toString());
            }
        };
    }

    static Operator<Long> zaddMap() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.zadd(param.buildKey(),
                        (Map<String, Double>) param.getValues()[0]);
            }
        };
    }

    static Operator<Long> zcard() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.zcard(param.buildKey());
            }
        };
    }

    static Operator<Double> zincrby() {
        return new Operator<Double>() {
            @Override
            public Double exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.zincrby(param.buildKey(),
                        Double.valueOf(param.getValues()[0].toString()),
                        param.getValues()[1].toString());
            }
        };
    }

    static Operator<Set<String>> zrange() {
        return new Operator<Set<String>>() {
            @Override
            public Set<String> exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.zrange(param.buildKey(),
                        Long.valueOf(param.getValues()[0].toString()),
                        Long.valueOf(param.getValues()[1].toString()));
            }
        };
    }

    static Operator<Set<Tuple>> zrangeWithScores() {
        return new Operator<Set<Tuple>>() {
            @Override
            public Set<Tuple> exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.zrangeWithScores(param.buildKey(),
                        Long.valueOf(param.getValues()[0].toString()),
                        Long.valueOf(param.getValues()[1].toString()));
            }
        };
    }

    static Operator<Set<String>> zrevrange() {
        return new Operator<Set<String>>() {
            @Override
            public Set<String> exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.zrevrange(param.buildKey(),
                        Long.valueOf(param.getValues()[0].toString()),
                        Long.valueOf(param.getValues()[1].toString()));
            }
        };
    }

    static Operator<Set<Tuple>> zrevrangeWithScores() {
        return new Operator<Set<Tuple>>() {
            @Override
            public Set<Tuple> exec(Jedis jedis, Param param) {
                param.setOp(OpType.QUERY);
                return jedis.zrevrangeWithScores(param.buildKey(),
                        Long.valueOf(param.getValues()[0].toString()),
                        Long.valueOf(param.getValues()[1].toString()));
            }
        };
    }

    static Operator<Long> zrem() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.zrem(param.buildKey(),
                        ArrayUtil.objArr2StrArr(param.getValues()));
            }
        };
    }

    static Operator<Long> zremrangebyrank() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.zremrangeByRank(param.buildKey(),
                        Long.valueOf(param.getValues()[0].toString()),
                        Long.valueOf(param.getValues()[1].toString()));
            }
        };
    }

    static Operator<Long> zremrangebyscore() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.zremrangeByScore(param.buildKey(),
                        Double.valueOf(param.getValues()[0].toString()),
                        Double.valueOf(param.getValues()[1].toString()));
            }
        };
    }

    static Operator<Long> expire() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                return jedis.expire(param.buildKey(),
                        Integer.valueOf(param.getValues()[0].toString()));
            }
        };
    }

    static Operator<Long> hsetnx() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.hsetnx(param.buildKey(),
                        param.getValues()[0].toString(),
                        param.getValues()[1].toString());
            }
        };
    }

    static Operator<Long> setnx() {
        return new Operator<Long>() {
            @Override
            public Long exec(Jedis jedis, Param param) {
                param.setOp(OpType.CHANGE);
                return jedis.setnx(param.buildKey(),
                        param.getValues()[0].toString());
            }
        };
    }

    private static void checkParams(Param param, int expectCnt) {
        if (param == null || param.getValues() == null || param.getValues().length < expectCnt) {
            throw new IllegalArgumentException("Expect " + expectCnt + " params, but found " +
                    (param == null || param.getValues() == null ? 0 : param.getValues().length));
        }
    }
}
