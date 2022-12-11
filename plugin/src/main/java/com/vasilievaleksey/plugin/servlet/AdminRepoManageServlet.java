package com.vasilievaleksey.plugin.servlet;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
public class AdminRepoManageServlet extends HttpServlet {
    @ComponentImport("com.atlassian.sal.api.user.UserManager")
    private final UserManager userManager;
    @ComponentImport
    private final LoginUriProvider loginUriProvider;
    @ComponentImport
    private final TemplateRenderer renderer;

    private final static String ADMIN_TEMPLATE_NAME = "/templates/admin-repo-manage/admin.vm";

    @Autowired
    public AdminRepoManageServlet(UserManager userManager, LoginUriProvider loginUriProvider, TemplateRenderer renderer) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.renderer = renderer;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isCurrentUserAdmin(request)) {
            response.setContentType("text/html;charset=utf-8");
            renderer.render(ADMIN_TEMPLATE_NAME, response.getWriter());
        } else {
            response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
        }
    }

    private Boolean isCurrentUserAdmin(HttpServletRequest request) {
        return Optional.ofNullable(userManager.getRemoteUserKey(request))
                .map(userManager::isSystemAdmin)
                .orElse(false);
    }

    private URI getUri(HttpServletRequest request) {
        StringBuffer builder = request.getRequestURL();
        if (request.getQueryString() != null) {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }
}
