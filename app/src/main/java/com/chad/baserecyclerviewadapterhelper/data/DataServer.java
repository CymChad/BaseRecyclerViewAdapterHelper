package com.chad.baserecyclerviewadapterhelper.data;


import com.chad.baserecyclerviewadapterhelper.entity.DelegateMultiEntity;
import com.chad.baserecyclerviewadapterhelper.entity.DiffUtilDemoEntity;
import com.chad.baserecyclerviewadapterhelper.entity.MySection;
import com.chad.baserecyclerviewadapterhelper.entity.ProviderMultiEntity;
import com.chad.baserecyclerviewadapterhelper.entity.QuickMultipleEntity;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.baserecyclerviewadapterhelper.entity.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class DataServer {

    public static final String HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK = "https://avatars1.githubusercontent.com/u/7698209?v=3&s=460";
    public static final String CYM_CHAD = "CymChad";
    public static final String CHAY_CHAN = "ChayChan";

    private DataServer() {
    }

    public static List<Status> getSampleData(int lenth) {
        List<Status> list = new ArrayList<>();
        for (int i = 0; i < lenth; i++) {
            Status status = new Status();
            status.setUserName("Chad" + i);
            status.setCreatedAt("04/05/" + i);
            status.setRetweet(i % 2 == 0);
            status.setUserAvatar("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460");
            status.setText("BaseRecyclerViewAdpaterHelper https://www.recyclerview.org");
            list.add(status);
        }
        return list;
    }

    public static List<Status> addData(List list, int dataSize) {
        for (int i = 0; i < dataSize; i++) {
            Status status = new Status();
            status.setUserName("Chad" + i);
            status.setCreatedAt("04/05/" + i);
            status.setRetweet(i % 2 == 0);
            status.setUserAvatar("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460");
            status.setText("Powerful and flexible RecyclerAdapter https://github.com/CymChad/BaseRecyclerViewAdapterHelper");
            list.add(status);
        }

        return list;
    }

//    public static List<MySection> getSampleData() {
//        List<MySection> list = new ArrayList<>();
//        list.add(new MySection(true, "Section 1", true));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(true, "Section 2", false));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(true, "Section 3", false));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(true, "Section 4", false));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(true, "Section 5", false));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        return list;
//    }

//    public static List<SectionMultipleItem> getSectionMultiData() {
//        List<SectionMultipleItem> list = new ArrayList<>();
//        Video video = new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD);
//
//        // add section data
//        list.add(new SectionMultipleItem(true, "Section 1", true));
//        // add multiple type item data ---start---
//        list.add(new SectionMultipleItem(SectionMultipleItem.TEXT, new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, "video_id_0")));
//        list.add(new SectionMultipleItem(SectionMultipleItem.TEXT, new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, "video_id_1")));
//        list.add(new SectionMultipleItem(SectionMultipleItem.IMG_TEXT, new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, "video_id_2")));
//        // ---end---
//
//        list.add(new SectionMultipleItem(true, "Section 2", false));
//        list.add(new SectionMultipleItem(SectionMultipleItem.IMG_TEXT, video));
//        list.add(new SectionMultipleItem(SectionMultipleItem.IMG_TEXT, video));
//        list.add(new SectionMultipleItem(SectionMultipleItem.TEXT, video));
//        list.add(new SectionMultipleItem(SectionMultipleItem.TEXT, video));
//        list.add(new SectionMultipleItem(true, "Section 3", false));
//        list.add(new SectionMultipleItem(SectionMultipleItem.IMG_TEXT, video));
//        list.add(new SectionMultipleItem(true, "Section 4", false));
//        list.add(new SectionMultipleItem(SectionMultipleItem.TEXT, video));
//        list.add(new SectionMultipleItem(true, "Section 5", false));
//        list.add(new SectionMultipleItem(SectionMultipleItem.IMG_TEXT, video));
//        list.add(new SectionMultipleItem(SectionMultipleItem.IMG_TEXT, video));
//        list.add(new SectionMultipleItem(SectionMultipleItem.IMG_TEXT, video));
//        return list;
//    }

    public static List<MySection> getSectionData() {
        List<MySection> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(new MySection(true, "Section " + i));
            list.add(new MySection(false, new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
            list.add(new MySection(false, new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
            list.add(new MySection(false, new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
            list.add(new MySection(false, new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
            list.add(new MySection(false, new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
            list.add(new MySection(false, new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        }
        return list;
    }

    public static List<String> getStrData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String str = HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK;
            if (i % 2 == 0) {
                str = CYM_CHAD;
            }
            list.add(str);
        }
        return list;
    }

    public static List<QuickMultipleEntity> getMultipleItemData() {
        List<QuickMultipleEntity> list = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            list.add(new QuickMultipleEntity(QuickMultipleEntity.IMG, QuickMultipleEntity.IMG_SPAN_SIZE));
            list.add(new QuickMultipleEntity(QuickMultipleEntity.TEXT, QuickMultipleEntity.TEXT_SPAN_SIZE, CYM_CHAD));
            list.add(new QuickMultipleEntity(QuickMultipleEntity.IMG_TEXT, QuickMultipleEntity.IMG_TEXT_SPAN_SIZE));
            list.add(new QuickMultipleEntity(QuickMultipleEntity.IMG_TEXT, QuickMultipleEntity.IMG_TEXT_SPAN_SIZE_MIN));
            list.add(new QuickMultipleEntity(QuickMultipleEntity.IMG_TEXT, QuickMultipleEntity.IMG_TEXT_SPAN_SIZE_MIN));
        }

        return list;
    }

    public static List<DelegateMultiEntity> getDelegateMultiItemData() {
        List<DelegateMultiEntity> list = new ArrayList<>();
        for (int i = 0; i <= 40; i++) {
            list.add(new DelegateMultiEntity());
        }
        return  list;
    }

    public static List<ProviderMultiEntity> getProviderMultiItemData() {
        List<ProviderMultiEntity> list = new ArrayList<>();
        for (int i = 0; i <= 40; i++) {
            list.add(new ProviderMultiEntity());
        }
        return  list;
    }

//
//    public static List<NormalMultipleEntity> getNormalMultipleEntities() {
//        List<NormalMultipleEntity> list = new ArrayList<>();
//        for (int i = 0; i <= 4; i++) {
//            list.add(new NormalMultipleEntity(NormalMultipleEntity.SINGLE_IMG));
//            list.add(new NormalMultipleEntity(NormalMultipleEntity.SINGLE_TEXT,CHAY_CHAN));
//            list.add(new NormalMultipleEntity(NormalMultipleEntity.TEXT_IMG,CHAY_CHAN));
//            list.add(new NormalMultipleEntity(NormalMultipleEntity.TEXT_IMG,CYM_CHAD));
//            list.add(new NormalMultipleEntity(NormalMultipleEntity.TEXT_IMG,CHAY_CHAN));
//        }
//        return list;
//    }
//
    public static List<DiffUtilDemoEntity> getDiffUtilDemoEntities() {
        List<DiffUtilDemoEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            list.add(new DiffUtilDemoEntity(
                    i,
                    "Item " + i,
                    "This item " + i + " content",
                    "06-12")
            );
        }
        return list;
    }
}
