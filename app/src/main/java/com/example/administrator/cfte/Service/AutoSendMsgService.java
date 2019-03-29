package com.example.administrator.cfte.Service;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.administrator.cfte.Utils.WeChatTextWrapper;
import com.example.administrator.cfte.Utils.WechatUtils;

import java.util.ArrayList;
import java.util.List;

public class AutoSendMsgService extends AccessibilityService {

    private static final String TAG = "AutoSendMsgService";
    private List<String> allNameList = new ArrayList<>();
    private int mRepeatCount;
    public static boolean hasJump;
    public static int ServiceType;
    public static final int ServiceType_word = 0;
    public static final int ServiceType_video = 1;

    public static final int JUMP_FAIL = 0;
    public static final int JUMP_SUCCESS = 1;
    public static int JUMP_STATUS;

    /**
     * 必须重写的方法，响应各种事件。
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: {

                String currentActivity = event.getClassName().toString();
                if (hasJump) {
                    return;
                }
                if (currentActivity.equals(WeChatTextWrapper.WechatClass.WECHAT_CLASS_LAUNCHUI)) {
                    String curUserName = WechatUtils.findTextById(this, WeChatTextWrapper.WechatId.WECHATID_CHATUI_USERNAME_ID);
                    if(curUserName!=null){
                        WechatUtils.findViewIdAndClick(this, WeChatTextWrapper.WechatId.WECHATID_CHATUI_BACK_ID);
                    }else{
                        handleFlow_LaunchUI();
                    }
                } else if (currentActivity.equals(WeChatTextWrapper.WechatClass.WECHAT_CLASS_CONTACTINFOUI)) {
                    handleFlow_ContactInfoUI();
                } else if (currentActivity.equals(WeChatTextWrapper.WechatClass.WECHAT_CLASS_CHATUI)) {
                    handleFlow_ChatUI();
                }
            }
            break;
        }
    }

    private void handleFlow_ChatUI() {

        //如果微信已经处于聊天界面，需要判断当前联系人是不是需要发送的联系人
        String curUserName = WechatUtils.findTextById(this, WeChatTextWrapper.WechatId.WECHATID_CHATUI_USERNAME_ID);
        if (!TextUtils.isEmpty(curUserName) && curUserName.equals(WechatUtils.NAME)) {
            JUMP_STATUS = JUMP_SUCCESS;
            hasJump = true;
            WechatUtils.NAME=null;
        } else {
            //回到主界面
            WechatUtils.findViewIdAndClick(this, WeChatTextWrapper.WechatId.WECHATID_CHATUI_BACK_ID);
        }
    }

    private void handleFlow_ContactInfoUI() {
        if(ServiceType==ServiceType_word){
            WechatUtils.findTextAndClick(this, "发消息");
        }else{
            WechatUtils.findTextAndClick(this, "音视频通话");
            WechatUtils.findTextAndClick(this, "视频通话");
            JUMP_STATUS = JUMP_SUCCESS;
            hasJump = true;
            WechatUtils.NAME=null;
        }

    }

    private void handleFlow_LaunchUI() {

        try {
            //点击通讯录，跳转到通讯录页面
            WechatUtils.findTextAndClick(this, "通讯录");

            Thread.sleep(50);

            //再次点击通讯录，确保通讯录列表移动到了顶部
            WechatUtils.findTextAndClick(this, "通讯录");

            Thread.sleep(200);

            //遍历通讯录联系人列表，查找联系人
            AccessibilityNodeInfo itemInfo = TraversalAndFindContacts();
            if (itemInfo != null) {
                WechatUtils.performClick(itemInfo);
            } else {
                JUMP_STATUS = JUMP_FAIL;
                hasJump = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 从头至尾遍历寻找联系人
     *
     * @return
     */
    private AccessibilityNodeInfo TraversalAndFindContacts() {

        if (allNameList != null) allNameList.clear();

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId(WeChatTextWrapper.WechatId.WECHATID_CONTACTUI_LISTVIEW_ID);

        //是否滚动到了底部
        boolean scrollToBottom = false;
        if (listview != null && !listview.isEmpty()) {
            while (true) {
                //获取当前屏幕上的联系人信息
                List<AccessibilityNodeInfo> nameList = rootNode.findAccessibilityNodeInfosByViewId(WeChatTextWrapper.WechatId.WECHATID_CONTACTUI_NAME_ID);
                List<AccessibilityNodeInfo> itemList = rootNode.findAccessibilityNodeInfosByViewId(WeChatTextWrapper.WechatId.WECHATID_CONTACTUI_ITEM_ID);

                if (nameList != null && !nameList.isEmpty()) {
                    for (int i = 0; i < nameList.size(); i++) {
                        if (i == 0) {
                            //必须在一个循环内，防止翻页的时候名字发生重复
                            mRepeatCount = 0;
                        }
                        AccessibilityNodeInfo itemInfo = itemList.get(i);
                        AccessibilityNodeInfo nodeInfo = nameList.get(i);
                        String nickname = nodeInfo.getText().toString();
                        Log.d(TAG, "nickname = " + nickname);
                        if (nickname.equals(WechatUtils.NAME)) {
                            return itemInfo;
                        }
                        if (!allNameList.contains(nickname)) {
                            allNameList.add(nickname);
                        } else if (allNameList.contains(nickname)) {
                            Log.d(TAG, "mRepeatCount = " + mRepeatCount);
                            if (mRepeatCount == 3) {
                                //表示已经滑动到顶部了
                                if (scrollToBottom) {
                                    Log.d(TAG, "没有找到联系人");
                                    //此次发消息操作已经完成
                                    hasJump = true;
                                    return null;
                                }
                                scrollToBottom = true;
                            }
                            mRepeatCount++;
                        }
                    }
                }

                if (!scrollToBottom) {
                    //向下滚动
                    listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                } else {
                    return null;
                }

                //必须等待，因为需要等待滚动操作完成
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void onInterrupt() {

    }


}
