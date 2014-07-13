package com.qixi.db.entity.extend;

import com.qixi.db.entity.VoteItem;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-7
 * Time: 下午11:11
 * To change this template use File | Settings | File Templates.
 */
public class VoteItemExtend extends VoteItem {
    private int itemSelectCount ;


    public int getItemSelectCount() {
        return itemSelectCount;
    }

    public void setItemSelectCount(int itemSelectCount) {
        this.itemSelectCount = itemSelectCount;
    }
}
