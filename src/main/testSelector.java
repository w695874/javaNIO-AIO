package main;

import main.service.serverTcpService;
import main.serviceImpl.serverTcpServiceImpl;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/12.
 */
public class testSelector {

    private testInterface  testInterface;

    private serverTcpService serverTcpService;

    @Test
    public void testServerFunc() throws Exception{
        serverTcpService=new serverTcpServiceImpl();

        /**
         * SocketChannel: Socket 的替代类, 支持阻塞通信与非阻塞通信.
         * ServerSocketChannel: ServerSocket 的替代类, 支持阻塞通信与非阻塞通信.
         * SelectionKey: 代表 ServerSocketChannel 及 SocketChannel 向 Selector 注册事件的句柄.
         * 当一个 SelectionKey 对象位于Selector 对象的 selected-keys 集合中时, 就表示与这个 SelectionKey 对象相关的事件发生了
         * 在SelectionKey 类中有几个静态常量:下面的四个注解
         * SelectionKey.OP_ACCEPT       ->客户端连接就绪事件 等于监听serversocket.accept()返回一个socket
         * SelectionKey.OP_CONNECT   ->准备连接服务器就绪          跟上面类似,只不过是对于socket的 相当于监听了 socket.connect()
         * SelectionKey.OP_READ            ->读就绪事件,  表示输入流中已经有了可读数据, 可以执行读操作了
         * SelectionKey.OP_WRITE           ->写就绪事件
         *
         * SelectionKey是个set类型,set是不包含重复的list
         *
         *
         * SelectionKey.OP_ACCEPT的处理事件往往是先读到内核,让其就绪.注册一个可read事件(就绪事件)
         * */

        System.out.println("服务端代码运行!!!");
        SelectorProvider selectorProvider=SelectorProvider.provider();
        Selector selector=selectorProvider.openSelector();

        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(6000));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);



        new  Thread(()->{
            try {
                System.out.println("线程中开始select");
                while (true){
                    System.out.println("select!!!");
                    selector.select();
                    Set<SelectionKey> selectionKeys=selector.selectedKeys();
                    Iterator<SelectionKey> iter = selectionKeys.iterator();
                    SocketChannel sc ;
                    while(iter.hasNext())
                    {
                        SelectionKey key = iter.next();
                        if(key.isAcceptable()){
                            System.out.println("得到连接,开始处理");
                            serverTcpService.acceptHandler(key);

                            // 新的连接
                        }
                        if(key.isReadable()){

                            serverTcpService.readHandler(key);
                            // 可读
                        }
                        if (key.isWritable()){

                            serverTcpService.writeHandler(key);
                        }
                        iter.remove(); //处理完事件的要从keys中删去
                        key.channel().close();
                    }
                    System.out.println("select之后执行");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        blockThread();
    }

    @Test
    public void testClientFunc() throws Exception{

        System.out.println("客户端代码运行!!!");
        Selector selector=Selector.open();
        SocketChannel socketChannel=SocketChannel.open(new InetSocketAddress(6000));
        socketChannel.configureBlocking(false);

        socketChannel.register(selector,SelectionKey.OP_CONNECT);

        new  Thread(()->{
            System.out.println("线程中开始select");
            try {
                selector.select();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("select之后执行");
        }).start();

        blockThread();
    }


    @Test
    public void testE(){

        System.out.println("aaaaaa");
        new Thread(()->{
            System.out.println("线程运行");
        }).start();

        blockThread();
    }

    public void blockThread(){
        while (true);
    }


}
