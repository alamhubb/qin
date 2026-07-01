package com.qin.web;

import com.qin.lang.runtime.JavaEsmGlobal;
import com.qin.runtime.core.QinHttpApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class QinWebApplicationAssembler {
    public QinHttpApp assemble(Object appObject) {
        Objects.requireNonNull(appObject, "appObject cannot be null");
        QinWebApp app = QinWeb.create().health();
        String webRoot = classAwareStringMember(appObject, "__qinWebRoot");
        for (Object controller : controllers(appObject)) {
            registerController(app, webRoot, controller);
        }
        String rpcPath = joinPath(webRoot, "/rpc/{method}");
        return app.rpcPath(rpcPath).toHttpApp();
    }

    private void registerController(QinWebApp app, String webRoot, Object controller) {
        if (controller == null) {
            throw new IllegalArgumentException("Qin App controllers cannot contain null entries");
        }
        List<RouteInfo> routes = routes(controller);
        if (routes.isEmpty()) {
            throw new IllegalArgumentException("Qin controller has no @GetMapping/@PostMapping/@DeleteMapping routes: "
                    + controller);
        }
        String basePath = joinPath(webRoot, classAwareStringMember(controller, "basePath"));
        String controllerName = controllerName(controller);
        for (RouteInfo route : routes) {
            String fullPath = joinPath(basePath, route.path());
            app.route(route.method(), fullPath, request -> JavaEsmGlobal.__qin_call_method__(controller, route.handler(), request));
            if ("GET".equals(route.method())) {
                app.query(controllerName + "." + route.handler(),
                        request -> JavaEsmGlobal.__qin_call_method__(controller, route.handler(), request));
            } else {
                app.mutation(controllerName + "." + route.handler(),
                        request -> JavaEsmGlobal.__qin_call_method__(controller, route.handler(), request));
            }
        }
    }

    private List<Object> controllers(Object appObject) {
        Object value = member(appObject, "controllers");
        if (value instanceof List<?> list) {
            return new ArrayList<>(list);
        }
        if (value == null) {
            return List.of();
        }
        throw new IllegalArgumentException("Qin App controllers must be an array");
    }

    private List<RouteInfo> routes(Object controller) {
        Object value = member(controller, "__qinWebRoutes");
        if (!(value instanceof List<?>)) {
            Object controllerType = member(controller, "constructor");
            value = member(controllerType, "__qinWebRoutes");
            if (!(value instanceof List<?>)) {
                Object prototype = member(controllerType, "prototype");
                value = member(prototype, "__qinWebRoutes");
            }
        }
        if (!(value instanceof List<?> list)) {
            return List.of();
        }
        List<RouteInfo> routes = new ArrayList<>();
        for (Object item : list) {
            routes.add(routeInfo(item));
        }
        return routes;
    }

    private RouteInfo routeInfo(Object item) {
        if (!(item instanceof Map<?, ?> map)) {
            throw new IllegalArgumentException("Qin controller route metadata must be an object");
        }
        String method = routeField(map, "method");
        String path = routeField(map, "path");
        String handler = routeField(map, "handler");
        if (method.isBlank() || handler.isBlank()) {
            throw new IllegalArgumentException("Qin controller route metadata requires method and handler");
        }
        return new RouteInfo(method.toUpperCase(java.util.Locale.ROOT), path, handler);
    }

    private String routeField(Map<?, ?> map, String name) {
        Object value = map.get(name);
        return value == null ? "" : String.valueOf(value);
    }

    private String controllerName(Object controller) {
        String controllerName = firstNonBlank(
                stringMember(controller, "controllerName"),
                stringMember(controller, "rpcName"));
        if (!controllerName.isBlank()) {
            return controllerName;
        }
        String typeName = controller.getClass().getSimpleName();
        return typeName.startsWith("__QinObject_") ? typeName.substring("__QinObject_".length()) : typeName;
    }

    private String stringMember(Object target, String name) {
        Object value = member(target, name);
        return value == null ? "" : String.valueOf(value);
    }

    private String classAwareStringMember(Object target, String name) {
        String value = stringMember(target, name);
        if (!value.isBlank()) {
            return value;
        }
        Object constructor = member(target, "constructor");
        return stringMember(constructor, name);
    }

    private Object member(Object target, String name) {
        if (target == null) {
            return null;
        }
        Object value = JavaEsmGlobal.__qin_member_get__(target, name);
        if (value != null) {
            return value;
        }
        String getter = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        try {
            return JavaEsmGlobal.__qin_call_method__(target, getter);
        } catch (RuntimeException error) {
            return null;
        }
    }

    private String firstNonBlank(String first, String second) {
        return first == null || first.isBlank() ? second == null ? "" : second : first;
    }

    private String joinPath(String basePath, String childPath) {
        String base = normalizePath(basePath);
        String child = normalizePath(childPath);
        if ("/".equals(child)) {
            return base;
        }
        if ("/".equals(base)) {
            return child;
        }
        return base + child;
    }

    private String normalizePath(String path) {
        String value = path == null ? "" : path.trim();
        if (value.isEmpty()) {
            return "/";
        }
        return value.startsWith("/") ? value : "/" + value;
    }

    private record RouteInfo(String method, String path, String handler) {
    }
}
