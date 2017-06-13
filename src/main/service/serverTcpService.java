package main.service;

import java.nio.channels.SelectionKey;

/**
 * function: nio的tcp接口
 */
public interface serverTcpService {

    /**
     * 处理管道(SocketChannel)accept事件,key.channel()即可取到用户管道socketChannel
     * @param key key
     * @throws Exception 异常
     * */
     void acceptHandler(SelectionKey key) throws Exception;



     /**
     * 处理管道(SocketChannel)Read事件,key.channel()即可取到用户管道socketChannel
     * @param key key
     * @throws Exception 异常
     * */
     void acceptRead(SelectionKey key) throws Exception;


     /**
     * 处理管道(SocketChannel)Write事件,key.channel()即可取到用户管道socketChannel
     * @param key key
     * @throws Exception 异常
     * */
     void acceptWrite(SelectionKey key) throws Exception;
}
