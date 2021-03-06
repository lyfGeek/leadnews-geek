package com.geek.common.web.app.security;

import com.alibaba.fastjson.JSON;
import com.geek.common.common.constant.Constant;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.common.enums.AppHttpCodeEnum;
import com.geek.model.user.pojos.ApUser;
import com.geek.utils.jwt.AppJwtUtil;
import com.geek.utils.threadlocal.AppThreadLocalUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(2)
@WebFilter(filterName = "appTokenFilter", urlPatterns = "/*")
public class AppTokenFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        ResponseResult<?> result = checkToken(request);
        // 测试和开发环境不过滤。
        if (true || result == null || !Constant.isProd()) {
            chain.doFilter(req, res);
        } else {
            res.setCharacterEncoding(Constant.CHARTER_NAME);
            res.setContentType("application/json");
            res.getOutputStream().write(JSON.toJSONString(result).getBytes());
        }
    }

    /**
     * 判断 TOKEN 信息，并设置为上下文。
     *
     * @param request
     * @return 如果验证不通过则返回对应的错误，否则返回 null 继续执行。
     */
    public ResponseResult checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        ResponseResult<?> responseResult = null;
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = AppJwtUtil.getClaimsBody(token);
            int result = AppJwtUtil.verifyToken(claims);
            // 有效的 token。
            if (result == 0 || result == -1) {
                ApUser user = new ApUser();
                user.setId(Long.valueOf((Integer) claims.get("id")));
                user = findUser(user);
                if (user.getId() != null) {
                    AppThreadLocalUtils.setUser(user);
                    // 验证成功，发送用户刷新消息。
                    sendUserRefresh(user);
                } else {
                    responseResult = ResponseResult.setAppHttpCodeEnum(AppHttpCodeEnum.TOKEN_INVALID);
                }
            } else if (result == 1) {
                // 过期。
                responseResult = ResponseResult.setAppHttpCodeEnum(AppHttpCodeEnum.TOKEN_EXPIRE);
            } else if (result == 2) {
                // TOKEN 错误。
                responseResult = ResponseResult.setAppHttpCodeEnum(AppHttpCodeEnum.TOKEN_INVALID);
            }
        } else {
            responseResult = ResponseResult.setAppHttpCodeEnum(AppHttpCodeEnum.TOKEN_REQUIRE);
        }
        return responseResult;
    }

    /**
     * 发送用户刷新消息。
     *
     * @param user
     */
    private void sendUserRefresh(ApUser user) {
        //http invoke send kafka
    }

    public ApUser findUser(ApUser user) {
        user.setName("test");
        return user;
    }

}
