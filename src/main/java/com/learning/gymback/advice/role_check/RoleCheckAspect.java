package com.learning.gymback.advice.role_check;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.learning.gymback.security.constants.Role;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class RoleCheckAspect {

    @Around("@annotation(com.learning.gymback.advice.role_check.RoleCheck) || within(@com.learning.gymback.advice.role_check.RoleCheck *)")
    public Object checkRoles(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Method method = sig.getMethod();

        RoleCheck roleCheck = method.getAnnotation(RoleCheck.class);
        if (roleCheck == null) {
            roleCheck = method.getDeclaringClass().getAnnotation(RoleCheck.class);
        }

        if (roleCheck == null || roleCheck.value().length == 0) {
            return pjp.proceed();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        Set<Role> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .map(Role::valueOf)
                .collect(Collectors.toSet());


        boolean allowed = Arrays.stream(roleCheck.value())
                .anyMatch(authorities::contains);

        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return pjp.proceed();
    }
}

