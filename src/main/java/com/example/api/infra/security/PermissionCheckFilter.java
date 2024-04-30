//package com.example.api.infra.security;
//
//import com.example.api.domain.users.User;
//import com.example.api.repositories.UserPermissionRepository;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class PermissionCheckFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private UserPermissionRepository userPermissionRepository;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String method = request.getMethod();
////        String uri = request.getRequestURI();
//        String uri = request.getRequestURI().replaceAll("/\\d+", "/id"); // if the route has parameter, such as "DELETE /api/v1/doctors/1", replaces the "1" for the word "id"
//
//        System.out.println(method);
//        System.out.println(uri);
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.getPrincipal() instanceof User userDetails) {
//
//            if (!userPermissionRepository.existsByUserIdAndPermission(userDetails.getId(), method + " " + uri)) { // the value is "GET /api/v1/doctors"
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.getWriter().write("User has no permission to perform this action");
//                return;
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
