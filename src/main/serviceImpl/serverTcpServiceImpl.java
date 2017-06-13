package main.serviceImpl;

import main.service.serverTcpService;

import java.nio.channels.SelectionKey;

/**
 * Created by Administrator on 2017/6/13.
 */
public class serverTcpServiceImpl implements serverTcpService {

    @Override
    public void acceptHandler(SelectionKey key) throws Exception {

    }

    @Override
    public void readHandler(SelectionKey key) throws Exception {

    }

    @Override
    public void writeHandler(SelectionKey key) throws Exception {
            //doNothing
    }
}
