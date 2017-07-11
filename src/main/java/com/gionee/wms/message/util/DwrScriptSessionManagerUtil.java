package com.gionee.wms.message.util;

import com.gionee.wms.entity.User;
import org.directwebremoting.Container;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.servlet.DwrServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

/**
 * Created by gionee on 2017/7/11.
 */
public class DwrScriptSessionManagerUtil extends DwrServlet {

    public void init() throws ServletException {

        Container container = ServerContextFactory.get().getContainer();
        ScriptSessionManager manager = container.getBean(ScriptSessionManager.class);
        ScriptSessionListener listener = new ScriptSessionListener() {

            public void sessionCreated(ScriptSessionEvent ev) {
                HttpSession session = WebContextFactory.get().getSession();
                Long userId = ((User) session.getAttribute("user")).getId();
                System.out.println("ScriptSession is created!");
                ev.getSession().setAttribute("userId", userId);
            }

            public void sessionDestroyed(ScriptSessionEvent ev) {
                System.out.println("ScriptSession is distroyed");
            }

        };

        manager.addScriptSessionListener(listener);

    }
}