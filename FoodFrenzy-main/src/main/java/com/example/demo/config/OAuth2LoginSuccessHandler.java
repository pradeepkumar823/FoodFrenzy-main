package com.example.demo.config;

import com.example.demo.entities.Admin;
import com.example.demo.repositories.AdminRepository;
import com.example.demo.utils.EmailNormalizer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        email = EmailNormalizer.normalize(email);

        Optional<Admin> adminOpt = adminRepository.findByAdminEmailIgnoreCase(email);

        FlashMap flashMap = new FlashMap();
        FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setVerified(true);
            admin.setVerificationCode(null);
            adminRepository.save(admin);

            flashMap.put("success", "Account verified with Google! You can now login.");
            if (flashMapManager != null) {
                flashMapManager.saveOutputFlashMap(flashMap, request, response);
            }
            response.sendRedirect("/login");
        } else {
            String errorMessage = "The Google account (" + email
                    + ") is not registered as an Admin. Please register with this email on our platform first.";
            flashMap.put("error", errorMessage);
            if (flashMapManager != null) {
                flashMapManager.saveOutputFlashMap(flashMap, request, response);
            }
            response.sendRedirect("/login");
        }
    }
}
