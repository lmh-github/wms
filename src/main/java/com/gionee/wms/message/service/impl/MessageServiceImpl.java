package com.gionee.wms.message.service.impl;

import com.gionee.wms.message.service.MessageService;
import com.gionee.wms.message.util.DwrScriptSessionManagerUtil;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.util.Collection;

/**
 * Created by gionee on 2017/7/11.
 */
@RemoteProxy(name = "MessageService")
@Component
public class MessageServiceImpl implements MessageService {

    @Override
    public void push(final String userId) {
        ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
        scriptSession.setAttribute(userId, userId);
        DwrScriptSessionManagerUtil dwrScriptSessionManagerUtil = new DwrScriptSessionManagerUtil();
        try {
            dwrScriptSessionManagerUtil.init();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessageAuto(String messageAuto) {
        sendMessageAuto(messageAuto, null);
    }

    @Override
    public void sendMessageAuto(final String messageAuto, final String userId) {

        Runnable run = new Runnable() {
            private ScriptBuffer script = new ScriptBuffer();

            public void run() {
                // 设置要调用的 js及参数
                script.appendCall("showMessage", messageAuto);
                // 得到所有ScriptSession
                Collection<ScriptSession> sessions = Browser
                    .getTargetSessions();
                // 遍历每一个ScriptSession
                for (ScriptSession scriptSession : sessions) {
                    if (userId == null || scriptSession.getAttribute(userId) != null) {
                        scriptSession.addScript(script);
                    }
                }
            }
        };
        // 执行推送
        Browser.withAllSessions(run);

    }

    @Override
    public String load() {
        return "测试";
    }
}
