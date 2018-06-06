package org.starlightfinancial.deductiongateway.common;

import org.starlightfinancial.deductiongateway.domain.local.SysUser;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session管理器
 *
 * @author senlin.deng
 * @date 2018年4月11日11:38:38
 */
public class SessionManager {

    /**
     * 用户和Session绑定关系
     */
    private static final Map<Integer, HttpSession> USER_SESSION = new ConcurrentHashMap<>();

    /**
     * SessionId和用户的绑定关系
     */
    private static final Map<String, Integer> SESSIONID_USER = new ConcurrentHashMap<>();

    /**
     * 用户登录时的处理
     *
     * @param user
     * @param session
     */
    public synchronized static void userLoginHandle(SysUser user, HttpSession session) {

        //当前sessionId
        String sessionId = session.getId();
        //如果当前sessionId绑定了用户,删除当前sessionId绑定的用户
        Integer userId = null;
        if (SESSIONID_USER.containsKey(sessionId)) {
            userId = SESSIONID_USER.remove(sessionId);
        }

        if (userId != null) {
            USER_SESSION.remove(userId);
        }

        //删除当前登录用户绑定的HttpSession
        HttpSession existSession = USER_SESSION.remove(user.getId());
        if (existSession != null) {
            SESSIONID_USER.remove(existSession.getId());
            existSession.removeAttribute("loginUser");
            existSession.setAttribute("msg", "您的账号已经在另一处登录了,您被迫下线!");
        }

        //添加用户与HttpSession的绑定
        USER_SESSION.put(user.getId(), session);
        //添加sessionId和用户的绑定
        SESSIONID_USER.put(session.getId(), user.getId());
        session.setAttribute("loginUser", user);
        session.removeAttribute("msg");

    }


    /**
     * 销毁session
     *
     * @param session
     */
    public static void destroySession(HttpSession session) {
        String sessionId = session.getId();
        //当前session销毁时删除当前session绑定的用户信息,同时删除当前session绑定用户的HttpSession
        Integer userId = null;
        if (SESSIONID_USER.containsKey(sessionId)) {
            userId = SESSIONID_USER.remove(sessionId);
        }
        if (userId != null) {
            USER_SESSION.remove(userId);
        }

        //当前session移除当前用户信息
        session.removeAttribute("loginUser");
    }

}
