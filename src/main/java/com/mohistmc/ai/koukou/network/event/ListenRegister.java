package com.mohistmc.ai.koukou.network.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ListenRegister {

    private static ListenRegister listenerRegister;
    private final List<BaseListener> lstListener;
    private final Lock lock = new ReentrantLock();

    private ListenRegister() {
        lstListener = new ArrayList<>();
    }

    public static ListenRegister getInstance() {
        synchronized (ListenRegister.class) {
            if (null == listenerRegister) {
                listenerRegister = new ListenRegister();
            }
            return listenerRegister;
        }
    }

    public void registerListener(BaseListener listenter) {
        try {
            boolean lockSuccess = lock.tryLock(5, TimeUnit.SECONDS);
            if (lockSuccess) {
                lstListener.add(listenter);
            }

        } catch (Exception ignored) {

        } finally {
            lock.unlock();
        }
    }

    public void onEvent(HttpPostEvent baseEvent) {
        for (BaseListener listenter : lstListener) {
            listenter.onEvent(baseEvent);
        }
    }

}
