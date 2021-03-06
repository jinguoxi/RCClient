package com.elitecrm.rcclient.robot;

import com.elitecrm.rcclient.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import io.rong.imkit.utils.StringUtils;

/**
 * Created by Loriling on 2018/7/3.
 *
 * 100025	系统繁忙
 100026	salt不合法
 100027	签名错误
 100028	参数错误
 100029	channel_id或者unit_id错误
 100030	question长度不能超过1000个字符
 */

public class RobotMessageHandler {
    private static final String AUTO_ZRG = "AUTO_ZRG";
    private static final String ZRG = "CLIENT_ZRG";

    public static String handleMessage(String content, int robotType) {
        String robotContent = "";
        if (robotType == Constants.RobotEngine.ROBOT_TYPE_XIAODUO) {
            try {
                JSONObject contentJSON = new JSONObject(content);
                String type = contentJSON.getString("type");
                if ("error".equals(type)) {
                    if (contentJSON.has("message")) {
                        robotContent = contentJSON.getString("message");
                    }
                }
                else if ("text".equals(type)) {
                    if (contentJSON.has("content")) {
                        robotContent = contentJSON.getString("content");
                    }
                    if (contentJSON.has("relatedQuestions")) {
                        JSONArray relatedQuestions = contentJSON.getJSONArray("relatedQuestions");
                        if (relatedQuestions.length() > 0) {
                            for (int i = 0; i < relatedQuestions.length(); i++) {
                                JSONObject relatedQuestion = relatedQuestions.getJSONObject(i);
                                if (relatedQuestion.has("relates")) {
                                    JSONArray relates = relatedQuestion.getJSONArray("relates");
                                    if (relates.length() > 0) {
                                        if (relatedQuestion.has("title")) {
                                            robotContent += relatedQuestion.getString("title");
                                        }
                                        for (int j=0; j < relates.length(); j++) {
                                            JSONObject relate = relates.getJSONObject(j);
                                            robotContent += "\n" + (j + 1) + "【" + relate.getString("name") + "】";
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else if ("image".equals(type)) { //TODO 解析图片
                    JSONObject imageJSON = contentJSON.getJSONObject("content");

                }
                else if ("image-text".equals(type)) { //TODO 解析图片和文字混合的形式
                    JSONObject imageTextJSON = contentJSON.getJSONObject("content");

                }
                else if ("news".equals(type)) { //TODO 解析图文
                    JSONObject newsJSON = contentJSON.getJSONObject("content");

                }
                else if ("command".equals(type)) {  //TODO 解析命令
                    JSONObject commandJSON = contentJSON.getJSONObject("content");
                    if (contentJSON.has("extra")) {
                        robotContent = contentJSON.getString("extra");
                    }

                    if (ZRG.equals(commandJSON.getString("code"))) {
                        if (null != content && !"".equals(content)) {
                            robotContent += "\n【转人工】";
                        } else {
                            robotContent += "【转人工】";
                        }
                    }
                } else {
                    content = "未定义的消息";
                }

//                int errorCode = contentJSON.getInt("error_code");
//                if (errorCode == 0) {//success
////                    {
////                        "error_code": 0,
////                        "info": "",
////                        "lp": "ec_0",
////                        "data": {
////                            "msg_id": 32483988,
////                            "user_id": "16e81433-b4ba-4cd2-945b-3aee250147dc",
////                            "question": "无法识别",
////                            "answers": [],
////                            "options": [],
////                            "recommend": [],
////                            "related_questions": [],
////                            "hot_questions": [],
////                            "inspects": [],
////                            "state": 3
////                        }
////                    }
//                    JSONObject dataJSON = contentJSON.getJSONObject("data");
//                    int state = dataJSON.getInt("state");
//                    if (state == 1) {//state=1时表示问题成功识别。answers中为所识别问题的答案列表，recommend为空，hot_questions为空，related_questions可能非空（若改问题配置了关联问题）
//                        JSONArray answersJSON = dataJSON.getJSONArray("answers");
//                        if (answersJSON != null && answersJSON.length() > 0) {
//                            content = answersJSON.get(0).toString();
//                        }
//                        String question = dataJSON.optString("question");
//                        if ("转接人工".equals(question)) {
//                            content += "\n【转人工】";
//                        }
//                    } else if (state == 2) {//state=2时表示问题无法进准识别。可推荐相识问题。answers为空，hot_questions为空，recommend可能非空
//                        JSONArray recommendJSON = dataJSON.getJSONArray("recommend");
//                        if (recommendJSON != null && recommendJSON.length() > 0) {
//                            content = "亲，你是不是要咨询以下问题:";
//                            int i = 0;
//                            for (; i < recommendJSON.length(); i++) {
//                                content += "\n" + (i + 1) + "【" + recommendJSON.get(i).toString() + "】";
//                            }
//                            content += "\n" + (i + 1) + "【转人工】";
//                        }
//                    } else if (state == 3) {//state=3时表示问题无法识别。answers为空，recommend可能非空，hot_questions可能非空（如果配置了渠道热门问题则非空）
//                        boolean transToHuman = dataJSON.optBoolean("trans_to_human");
//                        if (transToHuman) {
//                            content = "亲，由于这个问题暂时无法解答，给你带来不便非常抱歉，是否为您转人工服务呢！ 【转人工】";
//                        } else {
//                            content = "亲，能否重新阐述一下，这能让我更好的为您解答问题！";
//                        }
//                    }
//                } else if (errorCode == 1) {
//                    content = "其他错误";
//                } else if (errorCode == 100025) {
//                    content = "系统繁忙";
//                } else if (errorCode == 100026) {
//                    content = "salt不合法";
//                } else if (errorCode == 100027) {
//                    content = "签名错误";
//                } else if (errorCode == 100028) {
//                    content = "参数错误";
//                } else if (errorCode == 100029) {
//                    content = "channel_id或者unit_id错误";
//                } else if (errorCode == 100030) {
//                    content = "question长度不能超过1000个字符";
//                }
            } catch (Exception e) {
                robotContent = "内部错误: " + e.getMessage();
                e.printStackTrace();
            }
        } else {
            robotContent = content;
        }

        return robotContent;
    }
}
