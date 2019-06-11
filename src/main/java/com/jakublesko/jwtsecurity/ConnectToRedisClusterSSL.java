package com.jakublesko.jwtsecurity;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.masterslave.MasterSlave;
import io.lettuce.core.masterslave.StatefulRedisMasterSlaveConnection;

/**
 * @author Mark Paluch
 */
public class ConnectToRedisClusterSSL {

    public static void main(String[] args) {
    	//jcredispoc.redis.cache.windows.net:6380,password=nNIk5Gla+ggWncyDWTcg3u7qf25rVGbVxDS6NE0NTR8=,ssl=True,abortConnect=False
        RedisURI redisURI = RedisURI.create("rediss://nNIk5Gla+ggWncyDWTcg3u7qf25rVGbVxDS6NE0NTR8=@jcredispoc.redis.cache.windows.net:6380/0");
        redisURI.setSsl(true);
        redisURI.setTimeout(Duration.ofSeconds(30));
        redisURI.setVerifyPeer(false); // depending on your setup, you might want to disable peer verification
        RedisClient redisClient = RedisClient.create(redisURI);
        /*RedisClient redisClient = RedisClient.create("rediss://nNIk5Gla+ggWncyDWTcg3u7qf25rVGbVxDS6NE0NTR8=@jcredispoc.redis.cache.windows.net:6380/0");
        RedisClusterClient redisClient = RedisClusterClient.create(redisURI);*/
        System.out.println("Connected to Redis");
        basicUsage(redisClient);
        masterSlaveUsage(redisClient,redisURI);
        redisClient.shutdown();
    }
    private static void masterSlaveUsage(RedisClient client, RedisURI redisURI) {
        
        StatefulRedisMasterSlaveConnection<String, String> connection = MasterSlave.connect(client, new Utf8StringCodec(), redisURI);
        connection.setReadFrom(ReadFrom.MASTER_PREFERRED);

        connection.sync().set("keyMaster", "valueMaster");
        String value = connection.sync().get("keyMaster");
        System.out.println(value);

        // Close client
        connection.close();
    }
    private static void basicUsage(RedisClient client){

        StatefulRedisConnection<String, String> connection = client.connect();
        RedisCommands<String, String> sync = connection.sync();
        
        
        sync.set("key", "value");
        String value = sync.get("key");
        System.out.println(value);

        String result = sync.ping();
        System.out.println("result :" +result);
        // Close client
        
        
        /*******************Sync options*****************************/

        sync.hset("fooTobar", "foo", "bar");
        String fooTobar = sync.hget("fooTobar","foo");
        System.out.println(fooTobar);
        /************************************************************/
        /*******************Async options*****************************/
        RedisAsyncCommands<String, String> asyncCon = connection.async();
        try {

        RedisFuture<String> future = asyncCon.hget("fooTobar","foo");
        String valueAsync = future.get(1, TimeUnit.MINUTES);
        System.out.println(valueAsync);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        connection.close();
    }
}