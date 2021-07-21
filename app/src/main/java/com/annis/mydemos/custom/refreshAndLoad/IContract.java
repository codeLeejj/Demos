package com.annis.mydemos.custom.refreshAndLoad;

/**
 * author:Lee
 * date:2021/7/17
 * Describe:
 */
public interface IContract {
    void refresh();

    void isRefreshing();

    void refreshFinish();

    void load();

    void isLoading();

    void loadFinish();
}
