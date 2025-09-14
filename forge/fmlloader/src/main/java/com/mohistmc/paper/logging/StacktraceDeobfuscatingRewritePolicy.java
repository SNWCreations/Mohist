package com.mohistmc.paper.logging;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

import cpw.mods.modlauncher.Launcher;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;

@Plugin(
        name = "StacktraceDeobfuscatingRewritePolicy",
        category = Core.CATEGORY_NAME,
        elementType = "rewritePolicy",
        printObject = true
)
public final class StacktraceDeobfuscatingRewritePolicy implements RewritePolicy {
    private static class MethodHandleHolder {
    private static final MethodHandle DEOBFUSCATE_THROWABLE;

    static {
        try {
            final Field clField = Launcher.class.getDeclaredField("classLoader");
            boolean accessible = clField.isAccessible();
            clField.setAccessible(true);
            final ClassLoader mcLoader = (ClassLoader) clField.get(Launcher.INSTANCE);
            clField.setAccessible(accessible);
            final Class<?> cls = Class.forName("com.mohistmc.paper.util.StacktraceDeobfuscator", true, mcLoader);
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final VarHandle instanceHandle = lookup.findStaticVarHandle(cls, "INSTANCE", cls);
            final Object deobfuscator = instanceHandle.get();
            DEOBFUSCATE_THROWABLE = lookup
                    .unreflect(cls.getDeclaredMethod("deobfuscateThrowable", Throwable.class))
                    .bindTo(deobfuscator);
        } catch (final ReflectiveOperationException ex) {
            System.err.println("Error loading stacktrace deobfuscator");
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
    }
    }

    private StacktraceDeobfuscatingRewritePolicy() {
    }

    @Override
    public LogEvent rewrite(final LogEvent rewrite) {
        final Throwable thrown = rewrite.getThrown();
        if (thrown != null) {
            deobfuscateThrowable(thrown);
            return new Log4jLogEvent.Builder(rewrite)
                    .setThrownProxy(null)
                    .build();
        }
        return rewrite;
    }

    private static void deobfuscateThrowable(final Throwable thrown) {
        try {
            MethodHandleHolder.DEOBFUSCATE_THROWABLE.invoke(thrown);
        } catch (final Error e) {
            throw e;
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @PluginFactory
    public static StacktraceDeobfuscatingRewritePolicy createPolicy() {
        return new StacktraceDeobfuscatingRewritePolicy();
    }
}