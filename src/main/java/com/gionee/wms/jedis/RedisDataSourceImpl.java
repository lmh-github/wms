/**
* @author jay_liang
* @date 2013-11-26 下午9:26:41
*/
package com.gionee.wms.jedis;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @=======================================
 * @Description TODO redis数据源
 * @author jay_liang
 * @date 2013-11-26 下午9:26:41
 * @=======================================
 */
@Repository("redisDataSource")
public class RedisDataSourceImpl implements RedisDataSource {

    private static final Logger log = Logger.getLogger(RedisDataSourceImpl.class);

    @Autowired
    private MyShardedJedisPool myShardedJedisPool;

    public ShardedJedis getRedisClient() {
        try {
        	ShardedJedis shardedJedis = myShardedJedisPool.getInstance().getResource();
            return shardedJedis;
        } catch (Exception e) {
            log.error("获得缓存失败 " + e.getMessage());
        }
        return null;
    }

	@Override
	public void returnResource(ShardedJedis shardedJedis) {
		myShardedJedisPool.getInstance().returnResource(shardedJedis);
    }

	@Override
    public void returnResource(ShardedJedis shardedJedis, boolean broken) {
        if (broken) {
        	myShardedJedisPool.getInstance().returnBrokenResource(shardedJedis);
        } else {
        	myShardedJedisPool.getInstance().returnResource(shardedJedis);
        }
    }
}
