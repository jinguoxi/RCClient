package com.elitecrm.rcclient.util;

import android.util.Log;

import com.elitecrm.rcclient.entity.Chat;
import com.elitecrm.rcclient.entity.EliteMessage;
import com.elitecrm.rcclient.entity.Session;
import com.elitecrm.rcclient.entity.User;
import com.elitecrm.rcclient.logic.EliteSendMessageCallback;

import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by ThinkPad on 2017/2/8.
 */

public class MessageUtils {
    private static long messageId = 0l;
    public static long getNextMessageId(){
        return messageId++;
    }

    /**
     * 发出排队请求
     * @param queueId 队列号
     * @param from 来源
     */
    public static void sendChatRequest(int queueId, String from){
        try{
            JSONObject requestJSON = new JSONObject();
            requestJSON.put("messageId", MessageUtils.getNextMessageId());
            requestJSON.put("queueId", queueId);
            requestJSON.put("from", from);
            JSONObject extraJSON = new JSONObject();
            extraJSON.put("token", Chat.getInstance().getToken());
            extraJSON.put("type", Constants.RequestType.SEND_CHAT_REQUEST);

            EliteMessage eliteMessage = EliteMessage.obtain(requestJSON.toString());
            eliteMessage.setExtra(extraJSON.toString());
            Message myMessage = Message.obtain(Constants.CHAT_TARGET_ID, Conversation.ConversationType.SYSTEM, eliteMessage);
            RongIM.getInstance().sendMessage(myMessage, null, null, new EliteSendMessageCallback());
        } catch (Exception e) {}
    }

    /**
     * 发送满意度消息
     * @param ratingId 满意度id
     * @param comments 满意度comments
     */
    public static void sendRating(int ratingId, String comments) {
        try{
            JSONObject extraJSON = new JSONObject();
            extraJSON.put("token", Chat.getInstance().getToken());
            extraJSON.put("sessionId", Chat.getInstance().getSession().getId());
            extraJSON.put("type", Constants.RequestType.RATE_SESSION);
            JSONObject requestJSON = new JSONObject();
            requestJSON.put("ratingId", ratingId);
            requestJSON.put("ratingComments", comments);
            EliteMessage eliteMessage = EliteMessage.obtain(requestJSON.toString());
            eliteMessage.setExtra(extraJSON.toString());
            Message lastMessage = Message.obtain(Constants.CHAT_TARGET_ID, Conversation.ConversationType.SYSTEM, eliteMessage);
            RongIM.getInstance().sendMessage(lastMessage, null, null, new EliteSendMessageCallback());
        } catch (Exception e) {}
    }

    /**
     * 发送自定义消息，参数message可以是任何类型字符串，坐席收到该消息后，会在前台通过消息方式发出
     * @param message
     * @return 发送成功还是失败
     */
    public static boolean sendCustomMessage(String message) {
        Message custMessage = generateCustomMessage(message);
        if(custMessage != null){
            RongIM.getInstance().sendMessage(custMessage, null, null, new EliteSendMessageCallback());
            return true;
        }
        return false;
    }

    /**
     * 构造一个自定义消息对象
     * @param message
     * @return
     */
    public static Message generateCustomMessage(String message) {
        try{
            JSONObject extraJSON = new JSONObject();
            extraJSON.put("token", Chat.getInstance().getToken());
            extraJSON.put("sessionId", Chat.getInstance().getSession().getId());
            extraJSON.put("type", Constants.RequestType.SEND_CUSTOM_MESSAGE);
            EliteMessage eliteMessage = EliteMessage.obtain(message);
            eliteMessage.setExtra(extraJSON.toString());
            Message custMessage = Message.obtain(Constants.CHAT_TARGET_ID, Conversation.ConversationType.SYSTEM, eliteMessage);
            return custMessage;
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 根据发送人id，来获取对应坐席的名字
     * @param agentId
     * @return
     */
    public static String getAgentNameById(String agentId) {
        Session session = Chat.getInstance().getSession();
        if(session != null){
            User agent = session.getAgents().get(agentId);
            if(agent != null) {
                return agent.getName();
            }
        }
        return "EliteCRM";
    }
}
