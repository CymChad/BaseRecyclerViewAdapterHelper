package com.chad.baserecyclerviewadapterhelper.entity;

/**
 * https://github.com/chaychan
 *
 * @author ChayChan
 * @description: NormalEntity,need not implements MultiItemEntity interface
 * @date 2018/3/30  11:13
 */

public class NormalMultipleEntity {

    public static final int SINGLE_TEXT = 1;
    public static final int SINGLE_IMG = 2;
    public static final int TEXT_IMG = 3;

    public int type;
    public String content;

    public NormalMultipleEntity(int type) {
        this.type = type;
    }

    public NormalMultipleEntity(int type, String content) {
        this.type = type;
        this.content = content;
    }
}
