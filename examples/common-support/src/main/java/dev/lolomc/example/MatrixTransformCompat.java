package dev.lolomc.example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** Resolves the 3D PoseStack and 2D Matrix3x2fStack method names once per Minecraft API family. */
final class MatrixTransformCompat {
    private static final ConcurrentMap<Class<?>, Operations> CACHE = new ConcurrentHashMap<Class<?>, Operations>();

    private MatrixTransformCompat() {}

    static void pushScale(Object matrixStack, float scale) {
        operations(matrixStack).pushScale(matrixStack, scale);
    }

    static void pop(Object matrixStack) {
        operations(matrixStack).pop(matrixStack);
    }

    private static Operations operations(Object matrixStack) {
        if (matrixStack == null) throw new IllegalArgumentException("Minecraft matrix stack cannot be null");
        return CACHE.computeIfAbsent(matrixStack.getClass(), Operations::new);
    }

    private static final class Operations {
        private final Method push;
        private final Method scale2d;
        private final Method scale3d;
        private final Method pop;

        private Operations(Class<?> stackType) {
            push = method(stackType, "pushMatrix", "push");
            scale2d = optionalMethod(stackType, "scale", float.class, float.class);
            scale3d = optionalMethod(stackType, "scale", float.class, float.class, float.class);
            if (scale2d == null && scale3d == null) {
                throw new IllegalStateException("Unsupported Minecraft matrix scale API: " + stackType.getName());
            }
            pop = method(stackType, "popMatrix", "pop");
        }

        private void pushScale(Object stack, float scale) {
            invoke(push, stack);
            if (scale2d != null) invoke(scale2d, stack, scale, scale);
            else invoke(scale3d, stack, scale, scale, 1.0f);
        }

        private void pop(Object stack) {
            invoke(pop, stack);
        }

        private static Method method(Class<?> type, String preferred, String fallback) {
            Method result = optionalMethod(type, preferred);
            if (result == null) result = optionalMethod(type, fallback);
            if (result == null) throw new IllegalStateException("Unsupported Minecraft matrix API: " + type.getName());
            return result;
        }

        private static Method optionalMethod(Class<?> type, String name, Class<?>... parameters) {
            try {
                return type.getMethod(name, parameters);
            } catch (NoSuchMethodException ignored) {
                return null;
            }
        }

        private static void invoke(Method method, Object target, Object... arguments) {
            try {
                method.invoke(target, arguments);
            } catch (IllegalAccessException | InvocationTargetException error) {
                throw new IllegalStateException("Unable to apply Minecraft GUI matrix transform", error);
            }
        }
    }
}
