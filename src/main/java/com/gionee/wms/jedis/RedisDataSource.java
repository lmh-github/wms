/**
 * @author jay_liang
 * @date 2013-11-26 下午9:26:50
 */
package com.gionee.wms.jedis;

import redis.clients.jedis.ShardedJedis;

/**
 * @=======================================
 * @Description TODO redis客户端接口
 * @author jay_liang
 * @date 2013-11-26 下午9:26:50
 * @=======================================
 */
public interface RedisDataSource {
	public abstract ShardedJedis getRedisClient();

	public void returnResource(ShardedJedis shardedJedis);

	public void returnResource(ShardedJedis shardedJedis, boolean broken);
}
