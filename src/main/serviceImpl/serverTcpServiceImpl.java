package main.serviceImpl;

import main.service.serverTcpService;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;

import static main.constant.tcpConstant.SIZE_BUFFER;

/**
 * Created by Administrator on 2017/6/13.
 */
public class serverTcpServiceImpl implements serverTcpService {

    @Override
    public void acceptHandler(SelectionKey key) throws Exception {
        //服务端管道获取客户端管道
        SocketChannel socketChannel=((ServerSocketChannel)key.channel()).accept();
        //客户端管道注册读就绪事件(已经读到内核,要自己从内核读到应用层)
        socketChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(SIZE_BUFFER);
        //第三个参数为缓冲区大小,通常设为1024
        socketChannel.register(key.selector(),SelectionKey.OP_READ,buffer);
    }

    @Override
    public void readHandler(SelectionKey key) throws Exception {

        if (key == null)
            return;

        // 获得与客户端通信的信道
        SocketChannel clientChannel=(SocketChannel)key.channel();
        // 得到并清空缓冲区
        ByteBuffer buffer=(ByteBuffer)key.attachment();
        buffer.clear();

        // 读取信息获得读取的字节数
        long bytesRead=clientChannel.read(buffer);

        if(bytesRead==-1){
            // 没有读取到内容的情况
            key.cancel();
            clientChannel.close();
        }
        else{
            // 将缓冲区准备为数据传出状态
            buffer.flip();

            // 将字节转化为为GBK的字符串
            String receivedString= Charset.forName("GBK").newDecoder().decode(buffer).toString();

            // 控制台打印出来
            System.out.println("接收到来自"+clientChannel.socket().getRemoteSocketAddress()+"的信息:"+receivedString);

            // 准备发送的文本
            String sendString="你好,客户端. @"+new Date().toString()+"，已经收到你的信息"+receivedString;
            buffer= ByteBuffer.wrap(sendString.getBytes("GBK"));
            clientChannel.write(buffer);

            clientChannel.close();//加上这句浏览器可以访问

            // 设置为下一次读取或是写入做准备
           // key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    @Override
    public void writeHandler(SelectionKey key) throws Exception {
            //doNothing
    }
}
