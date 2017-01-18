/**
* @author jay_liang
* @date 2014-4-4 下午4:16:41
*/
package com.gionee.wms.jedis;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-4-4 下午4:16:41
 * @=======================================
 */
public class MyShardedJedisPool {
	
	private ShardedJedisPool shardedJedisPool;
	private JedisPoolConfig jedisPoolConfig;	
	private String servers;
	
	public ShardedJedisPool getInstance() {
		if(null==shardedJedisPool) {
			System.out.println("初始化redis连接池:" + servers);
			List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
			String[] serverList = servers.split(",");
			for (String server : serverList) {
				String[] temp = server.split(":");
				if(temp.length==2) {
					JedisShardInfo shardInfo = new JedisShardInfo(temp[0], temp[1]);
					shards.add(shardInfo);
				}
			}
			shardedJedisPool = new ShardedJedisPool(jedisPoolConfig, shards);
		}
		return shardedJedisPool;
	}

	public String getServers() {
		return servers;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}

	public JedisPoolConfig getJedisPoolConfig() {
		return jedisPoolConfig;
	}

	public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
	}
}
